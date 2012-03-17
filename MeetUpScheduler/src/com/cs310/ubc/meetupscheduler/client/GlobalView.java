package com.cs310.ubc.meetupscheduler.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.control.Control;
import com.google.gwt.maps.client.control.HierarchicalMapTypeControl;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;

/**
 * view for the parks and events summary page
 * 
 * @author David
 */

public class GlobalView extends View{

	private static final int MAP_HEIGHT = 600;
	private static final int MAP_WIDTH = 700;
	private static final int EVENT_TABLE_LENGTH = 15;

	//TODO: Do we need separate services for this?
	private final DataObjectServiceAsync parkService = GWT.create(DataObjectService.class);
	private final DataObjectServiceAsync eventService = GWT.create(DataObjectService.class);
	private HorizontalPanel rootPanel = new HorizontalPanel();
	private VerticalPanel parkTable = new VerticalPanel();
	private FlexTable recentEventsTable = new FlexTable();
	private TabPanel eventTabPanel = new TabPanel();
	private ListBox parkBox = new ListBox();
	private ArrayList<HashMap<String, String>> allEvents;
	private ArrayList<HashMap<String, String>> allParks;

	/**
	 * Loads the Maps API and returns the global view ui in a panel
	 */
	@Override
	public HorizontalPanel createPage() {
		//Initialize Map, needs a key before this can be deployed
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
		return rootPanel;
	}

	/**
	 * Builds the parks and events summary page ui and places it 
	 * in the rootPanel field
	 */
	public void buildUi() {

		//Map
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		final MapWidget map = new MapWidget(vancouver, 11);

		map.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		map.setScrollWheelZoomEnabled(true);
		Control typeControl = new MapTypeControl(true);
		map.addControl(typeControl);
		map.addControl(new LargeMapControl3D());

		//Parks
		//TODO add maps functionality
		parkTable.add(parkBox);
		parkTable.setWidth("500");
		parkTable.add(map);
		loadParks(map);

		//Recent Events
		recentEventsTable.setCellPadding(2);
		recentEventsTable.setCellSpacing(0);

		recentEventsTable.setText(1, 0, "Event Title");
		recentEventsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");

		recentEventsTable.setText(1, 1, "Event Type");
		recentEventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");

		recentEventsTable.setText(1, 2, "Park Name");
		recentEventsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");

		//Add Recent Event Table to tabPanel
		eventTabPanel.getTabBar().getElement().getStyle();
		eventTabPanel.add(recentEventsTable, "Recent Events");
		eventTabPanel.add(new HTML("My Events Here"), "My Events");
		eventTabPanel.add(new HTML("My Events Here"), "Park Notifications");
		eventTabPanel.selectTab(0);


		//put ui elements into rootPanel field

		rootPanel.add(parkTable);
		rootPanel.add(eventTabPanel);
	}

	/**
	 * This method makes the AsyncCallback to get an ArrayList of events stored
	 * in a HashMap. On success recent events table is loaded and event marker 
	 * overlays are placed on the map.
	 * 
	 * @param map The Google Maps widget that gets the event marker overlays
	 */
	//TODO: popup for errors, Async for load recent events?
	private void loadEvents(final MapWidget map, ArrayList<HashMap<String,String>> parks){
		eventService.get("Event", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("oh noes event data didnt werks");
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> events) {
				allEvents = events;
				addRecentEvents(events);
			}
		});
	}

	/**
	 * This method makes the AsyncCallback to get an ArrayList of parks stored
	 * in a HashMap. On success parks ListBox is loaded and park marker overlays 
	 * are placed on the map.
	 * 
	 * @param map The Google Maps widget that gets the park marker overlays
	 */
	//TODO: popup for errors and remove markers
	private void loadParks(final MapWidget map){
		parkService.get("Park", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("oh noes park data didnt werks");
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> parks) {
				allParks = parks;
				addParksToListBox(parks);
				addParkMarkers(parks, map);
				//TODO: solve async issue
				loadEvents(map, parks);

				//TODO: change this when async is refactored
				addEventMarkers(parks, map);
			}
		});
	}

	/**
	 * Adds events to the recent events table. Number of events added is
	 * specified by the EVENT_TABLE_LENGTH field.
	 * 
	 * @param events The events to be added to the recent events table
	 *///TODO: fix the async problem here with the allParks field, add if statement (empty events)
	private void addRecentEvents(ArrayList<HashMap<String, String>> events){
		
		int tableLength = (events.size() < EVENT_TABLE_LENGTH) ? events.size(): EVENT_TABLE_LENGTH;
			
			for(int i=0; i<tableLength; i++){
				int row = recentEventsTable.getRowCount();
				String park_id = events.get(i).get("park_id");

				recentEventsTable.setText(row, 0, events.get(i).get("name"));
				recentEventsTable.setText(row, 1, events.get(i).get("category"));
				recentEventsTable.setText(row, 2, events.get(i).get("park_id"));

				/*for(int n=0; n < allParks.size(); n++){
				if(allParks.get(n).get("park_id").equals(park_id)){
					eventTable.setText(row, 2, allParks.get(n).get("name"));
				}
			}*/
			}
	}

	/**
	 * Adds parks to the parks ListBox.
	 * 
	 * @param parks The parks to be added to the parks ListBox
	 */
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


	private void addEventMarkers(ArrayList<HashMap<String, String>> events, final MapWidget map){

		for(int i=0; i<50; i++){

			String latLong = events.get(i).get("google_map_dest");

			int index = latLong.indexOf(",");

			double lat = Double.parseDouble(latLong.substring(0, index));
			double lon = Double.parseDouble(latLong.substring(index+1));

			final HashMap<String, String> singleEvent = events.get(i);
			final Marker eventMarker = new Marker(LatLng.newInstance(lat, lon));


			eventMarker.addMarkerClickHandler(new MarkerClickHandler() {
				public void onClick(MarkerClickEvent event) {
					InfoWindow info = map.getInfoWindow();
					InfoWindowContent content = new InfoWindowContent(
							"<font color=\"#4C674C\"><big><b> Event Details: </b></big></font><br/>"
							+ "<a href=\"http://127.0.0.1:8888/MeetUpScheduler.html?gwt.codesvr=127.0.0.1:9997#page2\">" + singleEvent.get("name") + "</a><br/>"

					);
					info.open(eventMarker, content);

				}
			});

			map.addOverlay(eventMarker);

		}
	}

	/**
	 * Adds park overlay markers to the map widget
	 * 
	 * @param parks List of parks that will have markers placed
	 * @param map Map widget receiving marker overlays
	 */
	//TODO: "zoom to park" park marker
	private void addParkMarkers(ArrayList<HashMap<String, String>> parks, MapWidget map){
		/*for(int i = 0; i<parks.size(); i++){

			String latLong = parks.get(i).get("google_map_dest");

			int index = latLong.indexOf(",");

			double lat = Double.parseDouble(latLong.substring(0, index));
			double lon = Double.parseDouble(latLong.substring(index+1));

			map.addOverlay(new Marker(LatLng.newInstance(lat, lon)));
		}*/
	}
}

