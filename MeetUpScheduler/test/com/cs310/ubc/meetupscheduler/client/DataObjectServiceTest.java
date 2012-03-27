package com.cs310.ubc.meetupscheduler.client;

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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class DataObjectServiceTest extends GWTTestCase {
	private DataObjectServiceAsync dataObjectService;
	private HashMap<String, String> parkRow;

	@Before
	public void gwtSetUp() {
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
		Timer timer = new Timer() {
			public void run() {
				AsyncCallback<HashMap<String, String>> callback = new AsyncCallback<HashMap<String, String>>() {
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
						finishTest();
					}

					public void onSuccess(HashMap<String, String> results) {
						assertEquals(results, parkRow);
						finishTest();
					}
				};
				dataObjectService.add("Park", parkRow, callback);
			}
		};
		delayTestFinish(5000);
		timer.schedule(100);
	}

	@Test
	public void testQueryAllParks() {
		Timer timer = new Timer() {
			public void run() {
				AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
						finishTest();
					}

					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						assertEquals(result, parkRow);
						finishTest();
					}
				};
				dataObjectService.get("Park", callback);
			}
		};
		delayTestFinish(5000);
		timer.schedule(100);
	}

	@Test
	public void testQueryOnePark() {
		Timer timer = new Timer() {
			public void run() {
				AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
						finishTest();
					}

					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						assertEquals(result, parkRow);
						finishTest();
					}
				};
				dataObjectService.get("Park", "name=='Super Park'", callback);
			}
		};
		delayTestFinish(5000);
		timer.schedule(100);
	}

	@Test
	public void testUpdatePark() {
		Timer timer = new Timer() {
			public void run() {
				AsyncCallback<ArrayList<HashMap<String, String>>> callback = new AsyncCallback<ArrayList<HashMap<String, String>>>() {
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
						finishTest();
					}

					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						assertEquals(result.get(0).get("name"), "Party Park");
						finishTest();
					}
				};
				HashMap<String, String> newValues = new HashMap<String,String>();
				newValues.put("name", "Party Park");
				dataObjectService.update("Park", "id==\"1\"", newValues, callback);
			}
		};
		delayTestFinish(5000);
		timer.schedule(100);
	}

	@Test
	public void testRemovePark() {
			Timer timer = new Timer() {
				public void run() {
					AsyncCallback<Void> callback = new AsyncCallback<Void>() {
						public void onFailure(Throwable caught) {
							System.out.println(caught.getMessage());
							finishTest();
						}

						public void onSuccess(Void ignore) {
							finishTest();
						}
					};
					dataObjectService.remove("Park", "id==\"1\"", callback);
				}
			};
			delayTestFinish(5000);
			timer.schedule(100);
	}

	@Override
	public String getModuleName() {
		return "com.cs310.ubc.meetupscheduler.MeetUpScheduler";
	}
}