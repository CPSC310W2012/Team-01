package com.cs310.ubc.meetupscheduler.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cs310.ubc.meetupscheduler.client.ParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addPark(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Park(fields));
		} finally {
			pm.close();
		}
	}

	public void removeParks(String query) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Park.class, query);
			List<Park> parks = (List<Park>) q.execute();
			for (Park park: parks) {
				pm.deletePersistent(park);
			}
		} finally {
			pm.close();
		}
	}
	
	public void updateParks(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Park.class, query);
			List<Park> parks = (List<Park>) q.execute();
			for (Park park: parks) {
				park.setField(column, newValue);
			}
		} finally {
			pm.close();
		}
	}

	public List<Park> getParks(String query) {
		PersistenceManager pm = getPersistenceManager();
		List<Park> parks = new ArrayList<Park>();
		try {
			Query q = pm.newQuery(Park.class, query);
			parks = (List<Park>) q.execute(query);
		} finally {
			pm.close();
		}
		return parks;
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
