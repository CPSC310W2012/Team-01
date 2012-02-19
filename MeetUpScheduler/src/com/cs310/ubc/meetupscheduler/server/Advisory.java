package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Advisory extends DataObject {
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
	  
	  public Advisory(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  super(myFields);
	  }

}
