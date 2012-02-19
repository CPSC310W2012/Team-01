package com.cs310.ubc.meetupscheduler.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cs310.ubc.meetupscheduler.client.WashroomService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class WashroomServiceImpl extends RemoteServiceServlet implements WashroomService {
	
	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public void addWashroom(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Washroom(fields));
		} finally {
			pm.close();
		}
		
	}

	public List<Washroom> getWashrooms(String query) {
		PersistenceManager pm = getPersistenceManager();
		List<Washroom> washrooms = new ArrayList<Washroom>();
		try {
			Query q = pm.newQuery(Washroom.class, query);
			washrooms = (List<Washroom>) q.execute(query);
		} finally {
			pm.close();
		}
		return washrooms;
	}

	public void removeWashrooms(String query) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Park.class, query);
			List<Washroom> washrooms = (List<Washroom>) q.execute();
			for (Washroom washroom: washrooms) {
				pm.deletePersistent(washroom);
			}
		} finally {
			pm.close();
		}
		
	}

	public void updateWashrooms(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Park.class, query);
			List<Washroom> washrooms = (List<Washroom>) q.execute();
			for (Washroom washroom: washrooms) {
				washroom.setField(column, newValue);
			}
		} finally {
			pm.close();
		}
		
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
