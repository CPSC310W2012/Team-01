package com.cs310.ubc.meetupcheduler.server;

import java.lang.reflect.Field;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Park {
	
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
	  @Persistent
	  private String special_features;
	  @Persistent
	  private String washrooms;
	  
	  private Park() {
		  //jdoNewInstrance needs a no-arg constructor.
	  }
	  
	  /*
	   * This needs to take a list of objects in the same
	   * order as the parameters/CSV columns.
	   */
	  public Park(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  Class<?> parkClass = this.getClass();
		  for (String key : myFields.keySet()) {
			  Field f = parkClass.getDeclaredField(key);
			  f.set(this, myFields.get(key));
		  }
	  }
	  
	  public Long getID() {
		  return this.id;
	  }
	  
	  public String getName() {
		  return this.name;
	  }
	  
	  public String getStreetNumber() {
		  return this.street_number;
	  }
	  
	  public String getStreetName() {
		  return this.street_name;
	  }
	  
	  public String getEWStreet() {
		  return this.ew_street;
	  }
	  
	  public String getNSStreet() {
		  return this.ns_street;
	  }
	  
	  public String getGoogleMapDestination() {
		  return this.google_map_dest;
	  }
	  
	  public Float getHectares() {
		  return this.hectares;
	  }
	  
	  public String getNeighbourhoodName() {
		  return this.neighbourhood_name;
	  }
	  
	  public String getNeighbourhoodURL() {
		  return this.neighbourhood_url;
	  }
	  
	  public boolean hasAdvisories() {
		  return this.advisories == "Y" ? true: false;
	  }
	  
	  public boolean hasFacilities() {
		  return this.facilities == "Y" ? true: false;
	  }
	  
	  public boolean hasSpecialFeatures() {
		  return this.special_features == "Y" ? true: false;
	  }
	  
	  public boolean hasWashrooms() {
		  return this.washrooms == "Y" ? true: false;
	  }
}