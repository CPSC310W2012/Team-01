package com.cs310.ubc.meetupscheduler.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;
import com.cs310.ubc.meetupscheduler.client.places.EventPlace;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.user.client.Element;

/**
 * view for the parks and events summary page
 * 
 * @author David
 */

public class GlobalView extends Composite implements View{

	private static final int MAP_HEIGHT = 550;
	private static final int MAP_WIDTH = 700;
	private static final int EVENT_TABLE_LENGTH = 15;

	private ArrayList<HashMap<String, String>> allEvents;
	private ArrayList<HashMap<String, String>> allParks;
	private ArrayList<HashMap<String, String>> allAdvisories;

	private HorizontalPanel rootPanel = new HorizontalPanel();
	private VerticalPanel parkTable = new VerticalPanel();
	private FlexTable advisoryTable = new FlexTable();
	private FlexTable eventsTable = new FlexTable();
	private FlexTable myEventsTable = new FlexTable();
	private TabPanel eventTabPanel = new TabPanel();
	private ListBox parkBox = new ListBox();
	SimplePanel viewPanel = new SimplePanel();
	Element nameSpan = DOM.createSpan();

	public GlobalView() {
		allEvents = MeetUpScheduler.getEvents();
		allParks = MeetUpScheduler.getParks();
		allAdvisories = MeetUpScheduler.getAdvisories();

		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
	}

	/**
	 * Loads the Maps API and returns the global view ui in a panel
	 */
	@Override
	public Widget asWidget() {
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
		//TODO: pull this out for all classes to use
		//Map
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		final MapWidget map = new MapWidget(vancouver, 11);
		map.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new LargeMapControl3D());
		map.addControl(new MapTypeControl());

		//Zoom to park button
		Button parkButton = new Button("Zoom to Park", new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(parkBox.getItemCount()>0 && parkBox != null && allParks != null){
					int boxIndex = parkBox.getSelectedIndex();
					String selectedPark = parkBox.getValue(boxIndex);

					for(int i=0; i<allParks.size(); i++){
						if(allParks.get(i).get("name").equals(selectedPark)){
							String latLong = allParks.get(i).get("google_map_dest");

							int index = latLong.indexOf(",");
							double lat = Double.parseDouble(latLong.substring(0, index));
							double lon = Double.parseDouble(latLong.substring(index+1));

							map.setCenter(LatLng.newInstance(lat, lon), 17);
						}
					}
				}
			}
		});

		//Parks
		addParksToListBox(allParks);
		parkTable.add(parkBox);
		parkTable.add(parkButton);
		parkTable.setWidth("500");
		parkTable.add(map);

		//Recent Events
		eventsTable.setCellPadding(2);
		eventsTable.setCellSpacing(0);
		eventsTable.setText(1, 0, "Event Title");
		eventsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");
		eventsTable.setText(1, 1, "Event Type");
		eventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		eventsTable.setText(1, 2, "Park Name");
		eventsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");

		//My Events
		myEventsTable.setCellPadding(2);
		myEventsTable.setCellSpacing(0);

		myEventsTable.setText(1, 0, "My Events");
		myEventsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");
		myEventsTable.setText(1, 1, "Event Type");
		myEventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		myEventsTable.setText(1, 2, "Park Name");
		myEventsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");

		//Advisories
		advisoryTable.setCellPadding(2);
		advisoryTable.setCellSpacing(0);

		//Add Tables to tabPanel
		eventTabPanel.getTabBar().getElement().getStyle();
		eventTabPanel.add(eventsTable, "Recent Events");
		eventTabPanel.add(myEventsTable, "My Events");
		eventTabPanel.add(advisoryTable, "Park Advisories");
		eventTabPanel.selectTab(0);

		//Add Events in tables and on map
		addRecentEvents(allEvents);
		addParkAdvisories(allAdvisories);
		addEventMarkers(allEvents, allParks, map);

		//put ui elements into rootPanel field
		rootPanel.add(parkTable);
		rootPanel.add(eventTabPanel);
	}
	
	private void addMyEvents(ArrayList<HashMap<String, String>> events){
		if(events != null && events.size() >0){
			// If there are less events than our max table length, use the number of events for the length
			int tableLength = (events.size() < EVENT_TABLE_LENGTH) ? events.size(): EVENT_TABLE_LENGTH;

			for(int i=0; i<tableLength; i++){
				int row = eventsTable.getRowCount();

				eventsTable.setText(row, 0, events.get(i).get("name"));
				eventsTable.setText(row, 1, events.get(i).get("category"));
				eventsTable.setText(row, 2, events.get(i).get("park_name"));
			}
		}
	}

	/**
	 * Adds events to the recent events table. Number of events added is
	 * specified by the EVENT_TABLE_LENGTH field.
	 * 
	 * @param events The events to be added to the recent events table
	 */
	private void addRecentEvents(ArrayList<HashMap<String, String>> events){
		if(events != null && events.size() >0){
			// If there are less events than our max table length, use the number of events for the length
			int tableLength = (events.size() < EVENT_TABLE_LENGTH) ? events.size(): EVENT_TABLE_LENGTH;

			for(int i=0; i<tableLength; i++){
				int row = eventsTable.getRowCount();

				eventsTable.setText(row, 0, events.get(i).get("name"));
				eventsTable.setText(row, 1, events.get(i).get("category"));
				eventsTable.setText(row, 2, events.get(i).get("park_name"));
			}
		}
	}

	/**
	 * Adds the park advisories to the park advisories table
	 * 
	 * @param parks list of parks to display advisories
	 */
	private void addParkAdvisories(ArrayList<HashMap<String, String>> advisories){
		if(advisories != null && advisories.size() > 0){

			for(int i=0; i<advisories.size(); i++){
				int row = advisoryTable.getRowCount();
				advisoryTable.setHTML(row, 1, advisories.get(i).get("text"));
			}
		}
	}

	/**
	 * Adds parks to the parks ListBox.
	 * 
	 * @param parks The parks to be added to the parks ListBox
	 */
	private void addParksToListBox(ArrayList<HashMap<String, String>> parks){
		if(parks != null && parks.size() > 0){
			ArrayList<String> parkNames = new ArrayList<String>();

			for(int i = 0; i<parks.size(); i++){
				parkNames.add(parks.get(i).get("name"));
			}

			Collections.sort(parkNames);

			for(int i = 0; i<parks.size(); i++){
				parkBox.addItem(parkNames.get(i));
			}
		}
	}

	private void addEventMarkers(ArrayList<HashMap<String, String>> events, ArrayList<HashMap<String, String>> parks, final MapWidget map){

		if(map != null && events != null && events.size() > 0 && parks != null && parks.size() > 0){

			for(int i=0; i<parks.size(); i++){
				final String parkName = parks.get(i).get("name");
				final VerticalPanel vertPan = new VerticalPanel();
				final HorizontalPanel horPan = new HorizontalPanel();
				
				for(int j=0; j<events.size(); j++){
					if(parks.get(i).get("name").equals(events.get(j).get("park_name"))){
						
						String latLong = parks.get(i).get("google_map_dest");
						int index = latLong.indexOf(",");
						double lat = Double.parseDouble(latLong.substring(0, index));
						double lon = Double.parseDouble(latLong.substring(index+1));

						//this is to pass event info into the info window creation
						final Marker eventMarker = new Marker(LatLng.newInstance(lat, lon));
						final Integer id = new Integer(events.get(j).get("id"));

						Button eventButton = new Button(events.get(j).get("name"));
						eventButton.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
									EventPlace eventPlace = new EventPlace("Event", id);
									SharedData.getPlaceController().goTo(eventPlace);
							}
						});
						
						horPan.add(eventButton);
						
						eventMarker.addMarkerClickHandler(new MarkerClickHandler() {
							public void onClick(MarkerClickEvent event) {

								InfoWindow info = map.getInfoWindow();
								vertPan.add(new HTML(parkName));
								vertPan.add(horPan);
								
								InfoWindowContent content = new InfoWindowContent(
										vertPan
										
								);
								info.open(eventMarker, content);
							}
						});
						map.addOverlay(eventMarker);
					}
				}
			}
		}
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Global View, " + name);

	}
}