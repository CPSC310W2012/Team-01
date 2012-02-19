package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Washroom;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WashroomServiceAsync {
	void addWashroom(Map<String, Object> fields, AsyncCallback<Void> callback);
	void getWashrooms(String query, AsyncCallback<List<Washroom>> callback);
	void removeWashrooms(String query, AsyncCallback<Void> callback);
	void updateWashrooms(String query, String column, Object newValue, AsyncCallback<Void> callback);
}
