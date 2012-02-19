package com.cs310.ubc.meetupscheduler.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Database {
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	private final WashroomServiceAsync washroomService = GWT.create(WashroomService.class);
	private final AdvisoryServiceAsync advisoryService = GWT.create(AdvisoryService.class);
	private final FacilityServiceAsync facilityService = GWT.create(FacilityService.class);
	
	public void insert(String table, Object row) {
		//TODO: Implement
	}
	
	public List<Object> select(String table, String filter) {
		//TODO: Implement
		return null;
	}
	
	public void update(String table, String column, String filter, String value) {
		//TODO: Implement
	}
	
	public void delete(String table, String filter) {
		//TODO: Implement
	}
	
	
}
