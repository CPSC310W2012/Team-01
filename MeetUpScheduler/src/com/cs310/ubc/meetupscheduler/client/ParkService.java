package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Park;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("park")
public interface ParkService extends RemoteService {
	public void addPark(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public void removeParks(String query);
	public void updateParks(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public List<Park> getParks(String query);
}
