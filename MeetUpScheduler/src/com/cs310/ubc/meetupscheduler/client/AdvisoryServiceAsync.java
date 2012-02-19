package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Advisory;
import com.cs310.ubc.meetupscheduler.server.Park;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdvisoryServiceAsync {
	public void addAdvisory(Map<String, Object> fields, AsyncCallback<Void> callback);
	public void removeAdvisories(String query, AsyncCallback<Void> callback);
	public void updateAdvisories(String query, String column, Object newValue, AsyncCallback<Void> callback);
	public void getAdvisories(String query, AsyncCallback<List<Advisory>> callback);
}
