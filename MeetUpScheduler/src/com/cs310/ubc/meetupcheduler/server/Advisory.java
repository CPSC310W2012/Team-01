package com.cs310.ubc.meetupcheduler.server;

import java.util.Date;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Advisory {
	public enum AdvisoryField {
		PID {
		    public String toString() {
		        return "pid";
		    }
		},
		
		DATE {
		    public String toString() {
		        return "date_last";
		    }
		},
		 
		TEXT {
		    public String toString() {
		        return "advisory_text";
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
	  private Long pid;
	  @Persistent
	  private String date_last;
	  @Persistent
	  private String text;
	  @Persistent
	  private String url;
	  

	  
	  private Advisory() {
		  //TODO: 
	  }
	  
	  /*
	   * This needs to take a list of objects in the same
	   * order as the parameters/CSV columns.
	   */
	  public Advisory(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  //TODO
		  System.out.println("ADVISORY JDO NOT IMPLEMENTED");
	  }

}
