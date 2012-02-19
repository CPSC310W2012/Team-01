package com.cs310.ubc.meetupscheduler.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cs310.ubc.meetupscheduler.client.AdvisoryService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdvisoryServiceImpl extends RemoteServiceServlet implements AdvisoryService {

	private static final Logger LOG = Logger.getLogger(AdvisoryServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addAdvisory(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(new Advisory(fields));
		} finally {
			pm.close();
		}
	}
	
	public void removeAdvisories(String query) {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Advisory.class, query);
			List<Advisory> advisories = (List<Advisory>) q.execute();
			for (Advisory advisory: advisories) {
				pm.deletePersistent(advisory);
			}
		} finally {
			pm.close();
		}
	}

	public void updateAdvisories(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(Advisory.class, query);
			List<Advisory> advisories = (List<Advisory>) q.execute();
			for (Advisory advisory: advisories) {
				advisory.setField(column, newValue);
			}
		} finally {
			pm.close();
		}
	}

	public List<Advisory> getAdvisories(String query) {
		PersistenceManager pm = getPersistenceManager();
		List<Advisory> advisories = new ArrayList<Advisory>();
		try {
			Query q = pm.newQuery(Advisory.class, query);
			advisories = (List<Advisory>) q.execute(query);
		} finally {
			pm.close();
		}
		return advisories;
	}
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}
