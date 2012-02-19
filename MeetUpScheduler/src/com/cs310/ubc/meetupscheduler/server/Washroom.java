package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Washroom extends DataObject {
	public enum WashroomField {
		PID {
		    public String toString() {
		        return "pid";
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
	  private Long pid;
	  @Persistent
	  private String location;
	  @Persistent
	  private String notes;
	  @Persistent
	  private String summer_hours;
	  @Persistent
	  private String winter_hours;
	
	  public Washroom(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  super(myFields);
	  }

}
