package com.cs310.ubc.meetupscheduler.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Persistent class to hold data about events.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event extends DataObject {

	public Event(Map<String, String> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(myFields);
		creation_date = new Date(); //Keep track of creation_date for recent events table.
	}
	
	//Can be used to access event fields.
	public enum EventField {
		ID {
		    public String toString() {
		        return "id";
		    }
		},
		 
		NAME {
		    public String toString() {
		        return "name";
		    }
		},
		PID {
		    public String toString() {
		        return "park_id";
		    }
		},
		P_NAME {
			public String toString() {
				return "park_name";
			}
		},
		ATTND {
		    public String toString() {
		        return "num_attending";
		    }
		},
		ATTND_NAMES {
			public String toString() {
				return "attending_names";
			}
		},
		ATTND_EMAILS {
			public String toString() {
				return "attending_emails";
			}
		},
		CREATOR_EMAIL {
		    public String toString() {
		        return "creator_email";
		    }
		},
		CREATOR_NAME {
			public String toString() {
				return "creator_name";
			}
		},
		CAT {
		    public String toString() {
		        return "category";
		    }
		},
		DATE {
			public String toString() {
				return "date";
			}
		},
		
		START {
			public String toString() {
				return "start_time";
			}
		},
		END {
			public String toString() {
				return "end_time";
			}
		},
		NOTES {
			public String toString() {
				return "notes";
			}
		}
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id; //Automatically generated pk
	@Persistent
	protected String park_id;
	@Persistent
	protected String park_name;
	@Persistent
	protected String name;
	@Persistent
	protected String num_attending;
	@Persistent
	protected String creator_email;
	@Persistent
	protected String creator_name;
	@Persistent
	protected String category;
	@Persistent
	protected String date;
	@Persistent
	protected String start_time;
	@Persistent
	protected String end_time;
	//TODO: Easy way to associate emails and names?
	@Persistent
	protected ArrayList<String> attending_emails;
	@Persistent
	protected ArrayList<String> attending_names;
	@Persistent
	protected Date creation_date;
	@Persistent
	protected String notes;
	
	public static String getDefaultOrdering() {
		return "creation_date descending";
	}
}
