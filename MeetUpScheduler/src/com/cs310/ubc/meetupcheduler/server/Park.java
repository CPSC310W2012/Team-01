package com.cs310.ubc.meetupcheduler.server;

import java.util.List;

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
	  public Park(List<? extends Object> myFields) {
		  this.id = (Long) myFields.get(0);
		  this.name = (String) myFields.get(1);
		  this.street_number = (String) myFields.get(2);
		  this.street_name = (String) myFields.get(3);
		  this.ew_street = (String) myFields.get(4);
		  this.ns_street= (String) myFields.get(5);
		  this.google_map_dest = (String) myFields.get(6);
		  this.hectares = (Float) myFields.get(7);
		  this.neighbourhood_name = (String) myFields.get(8);
		  this.neighbourhood_url = (String) myFields.get(9);
		  this.advisories = (String) myFields.get(10);
		  this.facilities = (String) myFields.get(11);
		  this.special_features = (String) myFields.get(12);
		  this.washrooms = (String) myFields.get(13);
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