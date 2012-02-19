package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Park;
import com.cs310.ubc.meetupscheduler.server.Washroom;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface WashroomService extends RemoteService {
	public void addWashroom(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public List<Washroom> getWashrooms(String query);
	public void removeWashrooms(String query);
	public void updateWashrooms(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
}
