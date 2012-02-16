package com.cs310.ubc.meetupcheduler.server;


import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Facility {
	
	public enum FacilityField{
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
	  private Long pid;
	  @Persistent
	  private String count;
	  @Persistent
	  private String type;
	  @Persistent
	  private String url;
	  @Persistent
	  private String special_features;
	  

	  
	  private Facility() {
		  //TODO: 
	  }
	  
	  /*
	   * This needs to take a list of objects in the same
	   * order as the parameters/CSV columns.
	   */
	  public Facility(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  //TODO
		  System.out.println("FACILITY JDO NOT IMPLEMENTED");
	  }

}
