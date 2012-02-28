package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * A persistent class to hold washroom data.
 * @author connor
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Washroom extends DataObject {
	
	public Washroom(Map<String, String> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(myFields);
	}

	//Can be used to access Washroom field names.
	public enum WashroomField {
		PID {
		    public String toString() {
		        return "park_id";
		    }
		},
		
		LOC {
		    public String toString() {
		        return "location";
		    }
		},
		 
		NOTES {
		    public String toString() {
		        return "notes";
		    }
		},
		SUMHR {
		    public String toString() {
		        return "summer_hours";
		    }
		},
		WINHR {
		    public String toString() {
		        return "winter_hours";
		    }
		}
	}
	
	@PrimaryKey 
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id; //Automatically generated pk
	@Persistent
	protected String park_id;
	@Persistent
	protected String location;
	@Persistent
	protected String notes;
	@Persistent
	protected String summer_hours;
	@Persistent
	protected String winter_hours;
}
