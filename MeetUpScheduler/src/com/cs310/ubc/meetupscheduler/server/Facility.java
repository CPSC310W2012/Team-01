package com.cs310.ubc.meetupscheduler.server;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Facility extends DataObject {
	
	public enum FacilityField{
		ID {
			public String toString() {
				return "id";
			}
		},
		PID {
		    public String toString() {
		        return "pid";
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
		},
		SPECIALFEAT {
		    public String toString() {
		        return "special_features";
		    }
		}

		}
	
	  @PrimaryKey 
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  @Persistent
	  private Long park_id;
	  @Persistent
	  private String count;
	  @Persistent
	  private String type;
	  @Persistent
	  private String url;
	  @Persistent
	  private String special_features;
	  
	  /*
	   * This needs to take a list of objects in the same
	   * order as the parameters/CSV columns.
	   */
	  public Facility(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  super(myFields);
	  }
}
