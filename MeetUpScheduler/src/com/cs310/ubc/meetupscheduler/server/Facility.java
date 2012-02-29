package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Persistent class to hold facility data.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Facility extends DataObject {
	
	public Facility(Map<String, String> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(myFields);
	}
	  
	//Can be used to access facility field names
	public enum FacilityField{
		ID {
			public String toString() {
				return "id";
			}
		},
		PID {
		    public String toString() {
		        return "park_id";
		    }
		},
		
		COUNT {
		    public String toString() {
		        return "count";
		    }
		},
		 
		TYPE {
		    public String toString() {
		        return "type";
		    }
		},
		URL {
		    public String toString() {
		        return "url";
		    }
		}
	}
	
	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id; //Automatically generated pk
	@Persistent
	protected String park_id;
	@Persistent
	protected String count;
	@Persistent
	protected String type;
	@Persistent
	protected String url;
}
