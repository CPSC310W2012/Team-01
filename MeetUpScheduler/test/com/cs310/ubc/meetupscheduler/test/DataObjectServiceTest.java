package com.cs310.ubc.meetupscheduler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cs310.ubc.meetupscheduler.client.DataObjectService;
import com.cs310.ubc.meetupscheduler.client.DataObjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class DataObjectServiceTest extends GWTTestCase {
	private DataObjectServiceAsync dataObjectService;
	private HashMap<String, String> parkRow;
	
	@Before
	public void gwtSetUp() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		dataObjectService = GWT.create(DataObjectService.class);
		parkRow = new HashMap<String, String>();
		parkRow.put("id", "1"); 
		parkRow.put("name", "Super Park"); 
		parkRow.put("street_number", "123");
		parkRow.put("street_name", "Party St");
		parkRow.put("ew_street", "Party St");
		parkRow.put("ns_street", "Rad Ave");
		parkRow.put("google_map_dest", "123.456, 789.012");
		parkRow.put("hectares", "17.5");
		parkRow.put("neighbourhood_name", "Party Hood");
		parkRow.put("neighbourhood_url", "www.partyhood.com");
		parkRow.put("advisories", "Y");
		parkRow.put("facilities", "Y");
		parkRow.put("special_features", "Y");
		parkRow.put("washrooms", "N");
	}
	
	@Test
	public void testAddPark() {
		AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
			public void onFailure(Throwable caught) {
				//Oh no!
			}
			
			public void onSuccess(HashMap<String, String> results) {
				System.out.println(results);
			}
		};
		dataObjectService.add("Park", parkRow, callback);		
	}
	
	@Test
	public void testQueryAllParks() {
		AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
			public void onFailure(Throwable caught) {
				//Oh no!
			}
			
			public void onSuccess(ArrayList<HashMap<String, String>> results) {
				System.out.println(results);
			}
		};
		dataObjectService.get("Park", callback);
	}
	
	@Test
	public void testQueryOnePark() {
		AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
			public void onFailure(Throwable caught) {
				//Oh no!
			}
			
			public void onSuccess(ArrayList<HashMap<String, String>> results) {
				System.out.println(results);
			}
		};
		dataObjectService.get("Park", "name=='Super Park'", callback);
	}
	
	@Test
	public void testUpdatePark() {
		AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
			public void onFailure(Throwable caught) {
				//Oh no!
			}
			
			public void onSuccess(ArrayList<HashMap<String, String>> results) {
				System.out.println(results);
			}
		};
		dataObjectService.update("Park", "id==\"1\"", "name", "Party Park", callback);
	}

	@Test
	public void testRemovePark() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				//Oh no!
			}
			
			public void onSuccess(Void ignore) {
				System.out.println("All gone!");
			}
		};
		dataObjectService.remove("Park", "id==\"1\"", callback);
	}
	@Override
	public String getModuleName() {
		return "com.cs310.ubc.meetupscheduler.MeetUpScheduler";
	}
}