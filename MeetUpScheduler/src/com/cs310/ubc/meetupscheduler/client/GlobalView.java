package com.cs310.ubc.meetupscheduler.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;


/**
 * view for the parks and events summary page
 * 
 * @author David
 */

public class GlobalView extends Composite implements View{

	private static final int MAP_HEIGHT = 550;
	private static final int MAP_WIDTH = 700;
	private static final int EVENT_TABLE_LENGTH = 15;

	private static ArrayList<HashMap<String, String>> allEvents;
	private static ArrayList<HashMap<String, String>> allParks;
	private static ArrayList<HashMap<String, String>> allAdvisories;
	private final LoginInfo currentUser = SharedData.getLoginInfo();

	private HorizontalPanel rootPanel = new HorizontalPanel();
	private VerticalPanel parkTable = new VerticalPanel();
	private FlexTable advisoryTable = new FlexTable();
	private FlexTable eventsTable = new FlexTable();
	private FlexTable myEventsTable = new FlexTable();
	private TabPanel eventTabPanel = new TabPanel();
	private ListBox parkBox = new ListBox();
	Widget viewPanel = new SimplePanel();
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
				zoomToPark(map);
			}
		});

		//Parks
		addParksToListBox(allParks);
		parkTable.add(parkBox);
		parkTable.add(parkButton);
		parkTable.setWidth("500");
		parkTable.add(map);

		//Recent Events
		eventsTable.setCellPadding(1);
		eventsTable.setCellSpacing(3);
		
		eventsTable.setText(1, 0, "Event Title");
		eventsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");
		//eventsTable.setText(1, 1, "Share");
		//eventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		eventsTable.setText(1, 1, "Event Type");
		eventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		eventsTable.setText(1, 2, "Park Name");
		eventsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");

		//My Events
		myEventsTable.setCellPadding(1);
		myEventsTable.setCellSpacing(3);

		myEventsTable.setText(1, 0, "My Events");
		myEventsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");
		myEventsTable.setText(1, 1, "Share");
		myEventsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		myEventsTable.setText(1, 2, "Event Type");
		myEventsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");
		myEventsTable.setText(1, 3, "Park Name");
		myEventsTable.getCellFormatter().addStyleName(1, 3, "recentEventHeaders");


		//Add Tables to tabPanel
		eventTabPanel.getTabBar().getElement().getStyle();		
		eventTabPanel.add(eventsTable, "Recent Events");
		eventTabPanel.add(myEventsTable, "My Events");
		eventTabPanel.add(advisoryTable, "Park Advisories");
		eventTabPanel.selectTab(0);

		//Add Events in tables and on map
		addRecentEvents(allEvents);
		addMyEvents(allEvents);
		addParkAdvisories(allAdvisories);
		addEventMarkers(allEvents, allParks, map);

		//put ui elements into rootPanel field
		rootPanel.add(parkTable);
		rootPanel.add(eventTabPanel);
	}

	/**
	 * Adds events to the my events table. Only events belonging to
	 * the current logged in user will be added
	 * 
	 * @param events The events to be added to the "My Events" table
	 */
	private void addMyEvents(ArrayList<HashMap<String, String>> events){

		String userEmail = currentUser.getEmailAddress();

		if(events != null && events.size()>0){
			for(int i=0; i<events.size(); i++){

				if(userEmail.equals(events.get(i).get("creator_email")) || events.get(i).get("attending_emails").contains(userEmail)){
					int row = myEventsTable.getRowCount();
					HTML googlePlusButton = renderSmallPlusButton(events.get(i).get("id"));
					googlePlusButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					
					
					myEventsTable.setWidget(row, 0, new HTML("<a href=/?id=" + events.get(i).get("id") + "#EventPlace:Event" + ">" +
							events.get(i).get("name") + "</a>"));
					myEventsTable.setWidget(row, 1, googlePlusButton);
					myEventsTable.setText(row, 2, events.get(i).get("category"));
					myEventsTable.setText(row, 3, events.get(i).get("park_name"));
					
				}
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
				
				HTML googlePlusButton = renderSmallPlusButton(events.get(i).get("id"));
				googlePlusButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				
				
				eventsTable.setWidget(row, 0, new HTML("<a href=/?id=" + events.get(i).get("id") + "#EventPlace:Event" + ">" +
						events.get(i).get("name") + "</a>"));
				//eventsTable.setWidget(row, 1, googlePlusButton);
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
	/**
	 * Adds events markers that have info popups containing event titles and category
	 * 
	 * @param events All events to consider for markers
	 * @param parks All parks these events may take place in
	 * @param map	Map to display markers on
	 */
	private void addEventMarkers(ArrayList<HashMap<String, String>> events, ArrayList<HashMap<String, String>> parks, MapWidget map){

		if(map != null && events != null && events.size() > 0 && parks != null && parks.size() > 0){
			String userEmail = currentUser.getEmailAddress();

			for(int i=0; i<parks.size(); i++){
				ArrayList<HashMap<String, String>> parkEvents = new ArrayList<HashMap<String, String>>();
				boolean markerFlag = false;
				boolean userFlag = false;

				String latLong = parks.get(i).get("google_map_dest");
				int parseIndex = latLong.indexOf(",");
				double lat = Double.parseDouble(latLong.substring(0, parseIndex));
				double lon = Double.parseDouble(latLong.substring(parseIndex+1));

				for(int j=0; j<events.size(); j++){
					if(parks.get(i).get("name").equals(events.get(j).get("park_name"))){
						parkEvents.add(events.get(j));
						markerFlag = true;
						if(userEmail.equals(events.get(j).get("creator_email")) || events.get(j).get("attending_emails").contains(userEmail)){
							userFlag = true;	
						}
					}
				}
				/*This part is a bit fragile due to some gwt issues. 
				 * Please read the gwt issue link or talk to David S for more info if
				 * a change is needed.
				 * (http://code.google.com/p/gwt-google-apis/issues/detail?id=156)
				 */
				if(markerFlag){
					Marker marker = new Marker(LatLng.newInstance(lat, lon));
					addMarkerClickHandler(marker, parkEvents, map);
					if(userFlag){
						marker.getIcon().setIconSize(Size.newInstance(24, 41));
						marker.getIcon().setShadowSize(Size.newInstance(44, 41));
					}
					map.addOverlay(marker);
					marker.getIcon().setIconSize(Size.newInstance(20, 34));
					marker.getIcon().setShadowSize(Size.newInstance(37, 34));
				}
			}
		}  
	}

	/**
	 * Creates an event info window for the parks that have scheduled events
	 * 
	 * @param marker A park marker with a destination set
	 * @param parkEvents A list of events that take place at the marker location (park)
	 */
	private void addMarkerClickHandler(final Marker marker, final ArrayList<HashMap<String, String>> parkEvents, final MapWidget map){

		marker.addMarkerClickHandler(new MarkerClickHandler(){
			@Override
			public void onClick(MarkerClickEvent event) {
				InfoWindow info = map.getInfoWindow();
				InfoWindowContent content = new InfoWindowContent(createInfoWindowContent(parkEvents));
				info.open(marker, content);
			}
		});
	}

	/**
	 * Creates a the info window content for parks receiving event markers
	 * 
	 * @param parkEvents the park events receiving a marker
	 * @return info window content wrapped in a vertical panel
	 */
	public VerticalPanel createInfoWindowContent(ArrayList<HashMap<String, String>> parkEvents){

		FlexTable parkEventsTable = new FlexTable();
		VerticalPanel infoPanel = new VerticalPanel();

		for(int i=0; i<parkEvents.size(); i++){
			int row = parkEventsTable.getRowCount();

			HTML eventHTML = new HTML("<a href=/?id=" + parkEvents.get(i).get("id") + "#EventPlace:Event" + ">" +
					parkEvents.get(i).get("name") + "</a>" + " - " + parkEvents.get(i).get("category") + " - " + parkEvents.get(i).get("date"));
			//HTML plusButtonHTML = renderSmallPlusButton(parkEvents.get(i).get("id"));

			
			parkEventsTable.setWidget(row, 1, eventHTML);
			//parkEventsTable.setWidget(row, 2, plusButtonHTML);
			
		}

		infoPanel.add(new HTML("<font color=\"#4C674C\"><big><b> Events at " + parkEvents.get(0).get("park_name") + ": </b></big></font><br/>"));
		infoPanel.add(parkEventsTable);

		return infoPanel;
	}

	/**
	 * zoom to park selected in the parkBox
	 * 
	 * @param map map that will zoom to the park location
	 */
	private void zoomToPark(final MapWidget map) {
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
	
	/**
	 * Renders a small google+ button to share events.
	 * @param eventID The event for which to render the button.
	 * @return The rendered button
	 */
	private HTML renderSmallPlusButton(String eventID) {
		String targetURL = "http://vancitymeetupscheduler.appspot.com?id=" +eventID + "#EventPlace:Event";
		String s = "<g:plusone  size=\"small\" annotation=\"none\" href=\"" + targetURL +"\"></g:plusone>";
		HTML html = new HTML(s);
	
		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("https://apis.google.com/js/plusone.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
		
		return html;
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Global View, " + name);
	}
}