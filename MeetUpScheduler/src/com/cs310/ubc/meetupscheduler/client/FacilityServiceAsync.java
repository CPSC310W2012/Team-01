package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Facility;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FacilityServiceAsync {
	void getFacilities(String query, AsyncCallback<List<Facility>> callback);
	void updateFacilities(String query, String column, Object newValue, AsyncCallback<Void> callback);
	void removeFacilities(String query, AsyncCallback<Void> callback);
	void addFacility(Map<String, Object> fields, AsyncCallback<Void> callback);
}
