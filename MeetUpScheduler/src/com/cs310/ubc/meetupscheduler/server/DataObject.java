package com.cs310.ubc.meetupscheduler.server;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataObject {
	  private DataObject() {
		  //jdoNewInstrance needs a no-arg constructor.
	  }
	  
	  /*
	   * This takes a Map of objects where the keys are the column names defined in the object's enum.
	   */
	  public DataObject(Map<String, ? extends Object> myFields) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  Class<?> myClass = this.getClass();
		  for (String key : myFields.keySet()) {
			  Field f = myClass.getDeclaredField(key);
			  f.set(this, myFields.get(key));
		  }
	  }
	  
	  public void setField(String field, Object newValue) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  Class<?> myClass = this.getClass();
		  Field f = myClass.getDeclaredField(field);
		  f.set(this, newValue);
	  }
	  
	  public Map<String, Object> formatForTable() throws IllegalArgumentException, IllegalAccessException {
		  Map<String, Object> myValues = new HashMap<String, Object>();
		  Class<?> myClass = this.getClass();
		  Field[] f = myClass.getDeclaredFields();
		  for (Field field: f) {
			  myValues.put(field.getName(), field.get(this));
		  }
		  return myValues;
	  }
	  
	  public Object getField(String field) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		  Class<?> myClass = this.getClass();
		  Field f = myClass.getDeclaredField(field);
		  return f.get(this);
	  }
}
