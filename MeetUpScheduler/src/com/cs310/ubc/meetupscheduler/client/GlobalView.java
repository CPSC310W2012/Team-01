package com.cs310.ubc.meetupscheduler.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.control.LargeMapControl3D;



public class GlobalView extends View {

	private final DataObjectServiceAsync parkService = GWT.create(DataObjectService.class);
	private final DataObjectServiceAsync eventService = GWT.create(DataObjectService.class);
	private HorizontalPanel panel = new HorizontalPanel();
	private VerticalPanel parkTable = new VerticalPanel();
	private FlexTable eventTable = new FlexTable();
	private ListBox parkBox = new ListBox();
	private int MAPHEIGHT = 400;
	private int MAPWIDTH = 500;
	private ArrayList<HashMap<String, String>> allEvents;
	private ArrayList<HashMap<String, String>> allParks;

	@Override
	public HorizontalPanel createPage() {
		//Initialize Map, needs a key before it can be deployed
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
		return panel;
	}


	private void buildUi(){

		//Map
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		final MapWidget map = new MapWidget(vancouver, 11);

		map.setPixelSize(MAPWIDTH, MAPHEIGHT);
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new LargeMapControl3D());


		//Parks
		//TODO add maps functionality
		parkTable.add(parkBox);
		parkTable.setWidth("500");
		parkTable.add(map);
		loadParks(map);


		//Recent Events
		eventTable.setCellPadding(2);
		eventTable.setCellSpacing(0);

		eventTable.setText(0, 0, "Recently Added Events");
		eventTable.getCellFormatter().addStyleName(0, 0, "recentEventTitles");	

		eventTable.setText(1, 0, "Event Title");
		eventTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");

		eventTable.setText(1, 1, "Event Type");
		eventTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");

		eventTable.setText(1, 2, "Park Name");
		eventTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");
		loadEvents(map);

		//put panels together for return
		panel.add(parkTable);
		panel.add(eventTable);
	}

	private void loadEvents(final MapWidget map){
		eventService.get("Event", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("oh noes event data didnt werks");
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> events) {
				allEvents = events;
				addRecentEvents(events);
				addEventMarkers(events, map);
			}
		});
	}

	private void loadParks(final MapWidget map){
		parkService.get("Park", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("oh noes park data didnt werks");
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> parks) {
				allParks = parks;
				addParksToListBox(parks);
				addParkMarkers(parks, map);
			}
		});
	}

	//Add 15 most recent events to Recently Added Events table
	//TODO:Real Events
	private void addRecentEvents(ArrayList<HashMap<String, String>> events){
		if(allParks != null){
			for(int i=0; i<=14; i++){
				int row = eventTable.getRowCount();
				String park_id = events.get(i).get("park_id");

				eventTable.setText(row, 0, events.get(i).get("name"));
				eventTable.setText(row, 1, events.get(i).get("category"));

				for(int n=0; n < allParks.size(); n++){
					if(allParks.get(n).get("park_id").equals(park_id)){
						eventTable.setText(row, 2, allParks.get(n).get("name"));
					}
				}

			}
		}
	}

	private void addParksToListBox(ArrayList<HashMap<String, String>> parks){
		ArrayList<String> parkNames = new ArrayList<String>();

		for(int i = 0; i<parks.size(); i++){
			parkNames.add(parks.get(i).get("name"));
		}

		Collections.sort(parkNames);

		for(int i = 0; i<parks.size(); i++){
			parkBox.addItem(parkNames.get(i));
		}
	}

	private void addEventMarkers(ArrayList<HashMap<String, String>> events, MapWidget map){
		//TODO: implement this
		/*if(allParks != null){
			ArrayList<String> parkId = new ArrayList<String>();

			for(int i = 0; i<events.size(); i++){
				parkId.add(events.get(i).get("park_id"));
			}
		}*/
	}

	private void addParkMarkers(ArrayList<HashMap<String, String>> parks, MapWidget map){
		for(int i = 0; i<parks.size(); i++){

			String latLong = parks.get(i).get("google_map_dest");

			int index = latLong.indexOf(",");

			double lat = Double.parseDouble(latLong.substring(0, index));
			double lon = Double.parseDouble(latLong.substring(index+1));

			map.addOverlay(new Marker(LatLng.newInstance(lat, lon)));
		}
	}
}

