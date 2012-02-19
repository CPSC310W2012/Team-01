package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Park;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParkServiceAsync {
	public void addPark(Map<String, Object> fields, AsyncCallback<Void> callback);
	public void getParks(String query, AsyncCallback<List<Park>> callback);
	public void removeParks(String query, AsyncCallback<Void> callback);
	public void updateParks(String query, String column, Object newValue, AsyncCallback<Void> callback);
}
