package com.cs310.ubc.meetupscheduler.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park extends DataObject {

	public enum ParkField {
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
		STRTNUM {
		    public String toString() {
		        return "street_number";
		    }
		},
		STRTNAME {
		    public String toString() {
		        return "street_name";
		    }
		},
		EWST {
		    public String toString() {
		        return "ew_street";
		    }
		},
		NSST {
		    public String toString() {
		        return "ns_street";
		    }
		},
		MAPSTR {
		    public String toString() {
		        return "google_map_dest";
		    }
		},
		HECT {
		    public String toString() {
		        return "hectares";
		    }
		},
		NNAME {
		    public String toString() {
		        return "neighbourhood_name";
		    }
		},
		NURL {
		    public String toString() {
		        return "neighbourhood_url";
		    }
		},
		HASADVS {
		    public String toString() {
		        return "advisories";
		    }
		},
		HASFAC {
		    public String toString() {
		        return "facilities";
		    }
		}

		}
	
	  @PrimaryKey 
	  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	  private Long id;
	  @Persistent
	  private String name;
	  @Persistent
	  private String street_number;
	  @Persistent
	  private String street_name;
	  @Persistent
	  private String ew_street;
	  @Persistent
	  private String ns_street;
	  @Persistent
	  private String google_map_dest;
	  @Persistent
	  private Float hectares;
	  @Persistent
	  private String neighbourhood_name;
	  @Persistent
	  private String neighbourhood_url;
	  @Persistent
	  private String advisories;
	  @Persistent
	  private String facilities;
	  
	  public Park(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  super(myFields);
	  }
	  
	  public boolean hasAdvisories() {
		  return this.advisories == "Y" ? true: false;
	  }
	  
	  public boolean hasFacilities() {
		  return this.facilities == "Y" ? true: false;
	  }
}