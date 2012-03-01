package com.cs310.ubc.meetupscheduler.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * Singleton-type class to manage the persistence manager.
 */
public class PersistenceManagerSingleton {
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private PersistenceManagerSingleton() { }
	
	/**
	 * Gets a PersistenceManager instance.
	 * @return A PersistenceManager instance.
	 */
	public static PersistenceManager getInstance() {
		return PMF.getPersistenceManager();
	}
}
