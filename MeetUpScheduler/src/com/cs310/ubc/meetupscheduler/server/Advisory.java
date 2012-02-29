package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Persistent class to hold advisory data.
 * @author connor
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Advisory extends DataObject {
	  
	public Advisory(Map<String, String> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(myFields);
	}
	  
	//Can be used to access advisory fields
	public enum AdvisoryField {
		PID {
		    public String toString() {
		        return "park_id";
		    }
		},
		
		DATE {
		    public String toString() {
		        return "date_last";
		    }
		},
		 
		TEXT {
		    public String toString() {
		        return "text";
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
	protected Long id; //Automatically generated PK
	@Persistent
	protected String park_id;
	@Persistent
	protected String date_last;
	@Persistent
	protected String text;
	@Persistent
	protected String url;
}
