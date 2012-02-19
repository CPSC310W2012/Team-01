package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Facility;
import com.google.gwt.user.client.rpc.RemoteService;

public interface FacilityService extends RemoteService {
	public void addFacility(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public void removeFacilities(String query);
	public void updateFacilities(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public List<Facility> getFacilities(String query);
}
