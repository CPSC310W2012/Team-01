package com.cs310.ubc.meetupscheduler.server;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.cs310.ubc.meetupscheduler.client.DataObjectService;
import com.cs310.ubc.meetupscheduler.client.ServerException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This is the server side bit of our DataObject service that 
 * creates/stores/retrieves/modifies JDOs.
 */
public class DataObjectServiceImpl extends RemoteServiceServlet implements DataObjectService {
	private static final long serialVersionUID = 4058514716973575670L;
	private static final Logger LOG = Logger.getLogger(DataObjectServiceImpl.class.getName());
	private static final Map<String, Class<? extends DataObject>> tableMap = getTables();

	/**
	 * A method to add a new persistent object.
	 * @param table The table to add the new object to.
	 * @param fields A HashMap of Strings of the fields of the new object, where the key is the field name and value is its value.
	 * @throws ServerException 
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> add(String table, HashMap<String, String> fields) throws ServerException {
		PersistenceManager pm = getPersistenceManager();
		Class<? extends DataObject> tableClass = tableMap.get(table);
		@SuppressWarnings("rawtypes")
		Class<Map>[] conArgs = new Class[1];
		conArgs[0] = Map.class;
		Constructor<? extends DataObject> tableConstructor;
		HashMap<String, String> newObjMap = new HashMap<String, String>();
		try {
			tableConstructor = tableClass.getConstructor(conArgs);
			DataObject newObj = tableConstructor.newInstance(fields);
			pm.makePersistent(newObj);
			newObjMap = newObj.formatForTable();
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			throw new ServerException(e.getMessage(), e.getStackTrace());
		} finally {
			pm.close();
		}
		return newObjMap;
	}

	/**
	 * Method to remove persistent objects. Note that one or multiple objects can be removed depending on the
	 * query.
	 * @param table The type of object to remove.
	 * @param query A query to filter removed objects (i.e. "name==\"My Lame Park\"")
	 */
	@SuppressWarnings("unchecked")
	public void remove(String table, String query) {
		PersistenceManager pm = getPersistenceManager();
		Class<? extends DataObject> tableClass = tableMap.get(table);
		try {
			Query q;
			if (query.equals("*")) 
				q = pm.newQuery(tableClass);
			else
				q = pm.newQuery(tableClass, query);
			List<DataObject> objects = (List<DataObject>) q.execute();
			pm.deletePersistentAll(objects);
		} finally {
			pm.close();
		}
	}

	/**
	 * Method to remove all persistent objects of a type.
	 * @param table The type of object to remove.
	 */
	public void remove(String table) {
		remove(table, "*");
	}

	/**
	 * Updates the value a field of one or more objects.
	 * @param table The type of object to update.
	 * @param query A filter to find the appropriate object(s)
	 * @param column The field to update.
	 * @param newValue The new value to use.
	 * @returns A list of Maps of the fields of the object(s) that have been updated. Can be used to quickly refresh tables after a change.
	 * @throws ServerException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, String>> update(String table, String query, HashMap<String, String> valueMap) throws ServerException {
		PersistenceManager pm = getPersistenceManager();
		Class<? extends DataObject> tableClass = tableMap.get(table);
		ArrayList<HashMap<String, String>> changedObjs = new ArrayList<HashMap<String, String>>();
		try {
			Query q;
			if (query.equals("*"))
				q = pm.newQuery(tableClass);
			else
				q = pm.newQuery(tableClass, query);
			List<DataObject> objects = (List<DataObject>) q.execute();
			for (DataObject object: objects) {
				for (Entry<String, String> value: valueMap.entrySet()) {
					if (!value.getValue().equals(object.getField(value.getKey()))) {
						object.setField(value.getKey(), value.getValue());
						JDOHelper.makeDirty(object, value.getKey());
					}
				}
				changedObjs.add(object.formatForTable());
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage());
			throw new ServerException(e.getMessage(), e.getStackTrace());
		}  finally {
			pm.close();
		}
		return changedObjs;
	}

	/**
	 * Updates the value a field of all objects.
	 * @param table The type of object to update.
	 * @param column The field to update.
	 * @param newValue The new value to use.
	 * @returns A list of Maps of the fields of the object(s) that have been updated. Can be used to quickly refresh tables after a change.
	 * @throws ServerException
	 * 
	 */
	public ArrayList<HashMap<String, String>> update(String table, HashMap<String, String> newValues) throws ServerException {
		return update(table, "*", newValues);
	}

	/**
	 * A method to get the fields of one or more objects specified by a query.
	 * @param table The type of object to get.
	 * @param query A query to filter which objects are retrieved.
	 * @return A list of object Maps containing their fields
	 * @throws ServerException
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HashMap<String, String>> get(String table, String query) throws ServerException {
		PersistenceManager pm = getPersistenceManager();
		Class<? extends DataObject> tableClass = tableMap.get(table);
		List<DataObject> objects;
		ArrayList<HashMap<String, String>> retObjs = new ArrayList<HashMap<String, String>>();
		try {
			Query q;
			if (query.equals("*"))
				q = pm.newQuery(tableClass);
			else 
				q = pm.newQuery(tableClass, query);
			q.setOrdering((String) tableClass.getDeclaredMethod("getDefaultOrdering").invoke(null));
			objects = (List<DataObject>) q.execute(query);
			for (DataObject object: objects) {
				retObjs.add(object.formatForTable());
			}
		} catch (Exception e) { 
			LOG.log(Level.SEVERE, e.getMessage());
			throw new ServerException(e.getMessage(), e.getStackTrace());
		} finally {
			pm.close();
		}
		return retObjs;
	}

	/**
	 * A method to get the fields of one or more objects specified by a query.
	 * @param table The type of object to get.
	 * @param query A query to filter which objects are retrieved.
	 * @return A list of object Maps containing their fields
	 * @throws ServerException
	 */
	public ArrayList<HashMap<String, String>> get(String table) throws ServerException {
		return get(table, "*");
	}

	/**
	 * Helper method to get a persistence manager.
	 * @return A persistence manager.
	 */
	private PersistenceManager getPersistenceManager() {
		return PersistenceManagerSingleton.getInstance();
	}

	/**
	 * Helper method to retrieve the appropriate class given the table specification.
	 * @return The class corresponding to a table String.
	 */
	private static HashMap<String, Class<? extends DataObject>> getTables() {
		HashMap<String, Class<? extends DataObject>> myMap = new HashMap<String, Class<? extends DataObject>>();
		myMap.put("Park", Park.class);
		myMap.put("Event", Event.class);
		myMap.put("Advisory", Advisory.class);
		myMap.put("Washroom", Washroom.class);
		myMap.put("Facility", Facility.class);
		return myMap;
	}
}