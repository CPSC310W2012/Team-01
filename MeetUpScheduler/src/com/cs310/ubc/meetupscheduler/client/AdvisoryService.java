package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.cs310.ubc.meetupscheduler.server.Advisory;
import com.cs310.ubc.meetupscheduler.server.Park;
import com.google.gwt.user.client.rpc.RemoteService;

public interface AdvisoryService extends RemoteService {
	public void addAdvisory(Map<String, Object> fields) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public void removeAdvisories(String query);
	public void updateAdvisories(String query, String column, Object newValue) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException;
	public List<Advisory> getAdvisories(String query);
}
