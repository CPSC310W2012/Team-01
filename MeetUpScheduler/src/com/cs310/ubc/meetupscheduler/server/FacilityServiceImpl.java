package com.cs310.ubc.meetupscheduler.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cs310.ubc.meetupscheduler.client.FacilityService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FacilityServiceImpl extends RemoteServiceServlet implements FacilityService {
	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addFacility(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Facility(fields));
		} finally {
			pm.close();
		}
	}
	public void removeFacilities(String query) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Facility.class, query);
			List<Facility> facilities = (List<Facility>) q.execute();
			for (Facility facility: facilities) {
				pm.deletePersistent(facility);
			}
		} finally {
			pm.close();
		}
	}
	public void updateFacilities(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Park.class, query);
			List<Facility> facilities = (List<Facility>) q.execute();
			for (Facility facility: facilities) {
				facility.setField(column, newValue);
			}
		} finally {
			pm.close();
		}
	}
	
	public List<Facility> getFacilities(String query) {
		PersistenceManager pm = getPersistenceManager();
		List<Facility> facilities = new ArrayList<Facility>();
		try {
			Query q = pm.newQuery(Facility.class, query);
			facilities = (List<Facility>) q.execute(query);
		} finally {
			pm.close();
		}
		return facilities;
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}
}
