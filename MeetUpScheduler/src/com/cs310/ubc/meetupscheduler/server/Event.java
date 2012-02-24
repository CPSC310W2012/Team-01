package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Event extends DataObject {

	public Event(Map<String, String> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		super(myFields);
	}
	
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
		ATTND {
		    public String toString() {
		        return "num_attending";
		    }
		},
		CREATOR {
		    public String toString() {
		        return "creator";
		    }
		},
		CAT {
		    public String toString() {
		        return "category";
		    }
		}

		}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id; //Automatically generated pk
	@Persistent
	protected String park_id;
	@Persistent
	protected String name;
	@Persistent
	protected String num_attending;
	@Persistent
	protected String creator;
	@Persistent
	protected String category;
}
