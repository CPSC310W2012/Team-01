package com.cs310.ubc.meetupscheduler.client;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Database {
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	private final WashroomServiceAsync washroomService = GWT.create(WashroomService.class);
	private final AdvisoryServiceAsync advisoryService = GWT.create(AdvisoryService.class);
	private final FacilityServiceAsync facilityService = GWT.create(FacilityService.class);
	
	public void createPark(Map<String, Object> myFields) {
		parkService.addPark(myFields, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				
			}
			public void onSuccess(Void ignore) {
				System.out.println("WOOOOOOO");
			}
		});
	}
}
