package com.cs310.ubc.meetupscheduler.server;

import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * A persistent class to hold park data.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park extends DataObject {

	public Park(Map<String, String> myFields) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException  {
		super(myFields);
	}

	//Can be used to access park field names.
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
		},
		SPECIALFEAT {
		    public String toString() {
		        return "special_features";
		    }
		}
	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	protected Long id; //Park id is given by XML data.
	@Persistent
	protected String name;
	@Persistent
	protected String street_number;
	@Persistent
	protected String street_name;
	@Persistent
	protected String ew_street;
	@Persistent
	protected String ns_street;
	@Persistent
	protected String google_map_dest;
	@Persistent
	protected String hectares;
	@Persistent
	protected String neighbourhood_name;
	@Persistent
	protected String neighbourhood_url;
	@Persistent
	protected String advisories;
	@Persistent
	protected String facilities;
	@Persistent
	protected String special_features;
	  
	/**
	 * Checks if a park has advisories
	 * @return true if has advisories, false otherwise.
	 */
	public boolean hasAdvisories() {
		return this.advisories == "Y" ? true: false;
	}
	  
	/**
	 * Checks if a park has facilities
	 * @return true if has facilities, false otherwise.
	 */
	public boolean hasFacilities() {
		return this.facilities == "Y" ? true: false;
	}
	
	public static String getDefaultOrdering() {
		return "name";
	}
}