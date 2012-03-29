package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public class EventView extends Composite implements View{
	/**
	 * EventView for looking at the details of a specific event
	 * 
	 * @author Ben
	 */
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;	
	private final String ALL_PARKS = "All parks";
	private final String ALL_CATS = "All types";

	private VerticalPanel rootPanel = new VerticalPanel();
	private HorizontalPanel topPanel = new HorizontalPanel();
	private VerticalPanel bottomPanel = new VerticalPanel();
	private HorizontalPanel queryPanel = new HorizontalPanel();
	private VerticalPanel resultsPanel = new VerticalPanel();
	private HTML eventName = new HTML();
	private Button joinButton = new Button();
	private VerticalPanel parkPanel = new VerticalPanel();
	private ListBox attendeesBox = new ListBox();
	private VerticalPanel attendeePanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private Label eventCreator = new Label();
	private Label eventLoc = new Label();
	private Label eventTime = new Label();
	private Label eventNotes = new Label();
	private Label eventCategory = new Label();
	private MapWidget eventMap;
	private ArrayList<String> members = new ArrayList<String>();
	private Integer attendeeCount = 0;
	private Label attCountLabel = new Label();
	private ArrayList<HashMap<String, String>> allEvents;
	
	//Search-related elements
	private HTML searchLabel = new HTML();
	private ListBox parksBox = new ListBox();
	private ListBox categoryBox = new ListBox();
	private Button searchButton = new Button("Search!");
	private Label parksLabel = new Label("Choose a park: ");
	private Label categoryLabel = new Label("Choose an event type: ");
	private FlexTable resultsTable = new FlexTable();
	private HTML noResultsLabel = new HTML();

	private SimplePanel viewPanel = new SimplePanel();
	private ArrayList<HashMap<String, String>> allParks;
	Element nameSpan = DOM.createSpan();
	private HashMap<String, String> event = new HashMap<String, String>();
	private LoginInfo loginInfo;
	private int eventURLID;
	private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);

	/**
	 * The constructor for an EventView object
	 */
	public EventView() {
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
	}
	
	/**
	 * This shouldn't really be necessary anymore.
	 * @param eventID - The ID of the event you want to load.
	 */
	public EventView(int eventID){
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
		loadEvent(eventID);
	}


	/**
	 * Loads the map and initializes the page. 
	 * @return Returns the rootPanel for the page.
	 */
	@Override
	public Widget asWidget() {
		//Initialize Map, needs a key before it can be deployed
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUI();
			}
		});
		return rootPanel;
	}

	/**
	 * Constructs the UI elements of EventView
	 */
	public void buildUI(){
		String eventStringID = com.google.gwt.user.client.Window.Location.getParameter("id");
		boolean eventLoaded = false;
		if(eventStringID != null && !eventStringID.isEmpty() ){
			eventURLID = Integer.parseInt(eventStringID);
			eventLoaded = true;
		}
		loginInfo = MeetUpScheduler.SharedData.getLoginInfo();
		loadData();
		
		// Set up the map
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		eventMap = new MapWidget(vancouver, 11);
		eventMap.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		eventMap.setScrollWheelZoomEnabled(true);
		eventMap.addControl(new LargeMapControl3D());
		eventMap.checkResizeAndCenter();
		eventMap.addControl(new MapTypeControl());


		//set up the joinButton
		joinButton.setText("Join event!");
		joinButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				members.add(loginInfo.getNickname());
				//Add username to event
				String newNames = event.get("attending_names") + "," + loginInfo.getNickname();
				event.put("attending_names", newNames);
				//Add user email to event
				String newEmails = event.get("attending_emails") + "," + loginInfo.getEmailAddress();
				event.put("attending_emails", newEmails);
				//Increment number attending
				Integer numAttending = Integer.parseInt(event.get("num_attending"));
				numAttending++;
				event.put("num_attending", numAttending.toString());
				objectService.update("Event", "id=="+event.get("id"), event, new AsyncCallback<ArrayList<HashMap<String, String>>>() {
					public void onFailure(Throwable error) {
						System.out.println("Joining the event failed!");
					}
					public void onSuccess(ArrayList<HashMap<String, String>> newEvent) {
						attendeesBox.clear();
						setUpAttendees();
						joinButton.setEnabled(false);
						Window.alert("You are attending this event!");
					}
				});
			}
		});

		setUpInfoPanel();
		setUpSearchPanel();

		// Add items to panels
		loadEvent(eventURLID);
		parkPanel.add(eventMap);
		attendeePanel.add(attCountLabel);
		attendeePanel.add(attendeesBox);
		topPanel.add(infoPanel);
		topPanel.add(joinButton);
		topPanel.add(parkPanel);
		rootPanel.add(topPanel);
		if (!eventLoaded)
			topPanel.setVisible(false);
		rootPanel.add(bottomPanel);
		renderPlusButton();


	}
	
	/**
	 * This creates the button that shares to Google Plus. It uses the Plus APIs.
	 * Basically creates an object out of the Javascript, and runs on click.
	 * 
	 */
	private void renderPlusButton() {
		//<!-- Place this tag in your head or just before your close body tag -->
		//<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
		//<!-- Place this tag where you want the +1 button to render -->
		//<g:plusone></g:plusone>
		String testURL = "http://vancitymeetupscheduler.appspot.com?id=" + event.get("id") + "#EventPlace:Event";
		String s = "<g:plusone href=\"" + testURL +"\"></g:plusone>";
		HTML h = new HTML(s);
		attendeePanel.add(h);

		Document doc = Document.get();
		ScriptElement script = doc.createScriptElement();
		script.setSrc("https://apis.google.com/js/plusone.js");
		script.setType("text/javascript");
		script.setLang("javascript");
		doc.getBody().appendChild(script);
	}

	/**
	 * This loads the specifics of the event into the info panel.
	 * 
	 * @param eventID: The id of the event you want to load to the page.
	 */
	private void loadEvent(int eventID) {


		if (allEvents != null && allEvents.size() > 0){
			try { 
				for (int i = 0; i < allEvents.size(); i++){
					if (Integer.parseInt(allEvents.get(i).get("id")) == eventID){
						event = allEvents.get(i);
						eventCreator.setText("Creator: " + event.get("creator_name"));
						eventName.setHTML("<h2>" + event.get("name") + "</h2>"); 
						eventTime.setText("Time: " + event.get("date") + " from " + event.get("start_time") + " to " + event.get("end_time"));
						eventLoc.setText("Location: " + event.get("park_name"));
						if (event.get("notes") != null) {
							eventNotes.setText("Notes: \n" + event.get("notes"));
							eventNotes.setWordWrap(true);
							eventNotes.setWidth("250px");
						}

						eventMap.checkResizeAndCenter();
						eventCategory.setText("Category: " + event.get("category"));
						ArrayList<String> attendees = new ArrayList<String>(Arrays.asList(event.get("attending_names").split(",")));
						members.clear();
						for (String attendee : attendees){
							members.add(attendee);
						}
						attendeesBox.clear();
						setUpAttendees();
						zoomMap();

						ArrayList<String> attendingEmails = new ArrayList<String>(Arrays.asList(event.get("attending_emails").split(",")));
						if (attendingEmails.contains(loginInfo.getEmailAddress()))
							joinButton.setEnabled(false);
						else
							joinButton.setEnabled(true);
					}
				}

			} catch (Exception e) {
				Window.alert("There is no event " + eventID + ".");
			}
		}
		else Window.alert("No events!");
	}

	/**
	 * This zooms the map to the event the page is set to.
	 */
	private void zoomMap(){
		for(int i=0; i<allParks.size(); i++){
			if(allParks.get(i).get("name").equals(event.get("park_name"))){
				String latLong = allParks.get(i).get("google_map_dest");
				int index = latLong.indexOf(",");
				double lat = Double.parseDouble(latLong.substring(0, index));
				double lon = Double.parseDouble(latLong.substring(index+1));

				eventMap.setCenter(LatLng.newInstance(lat, lon), 17);
				final Marker eventMarker = new Marker(LatLng.newInstance(lat, lon));
				eventMap.addOverlay(eventMarker);

			}
		}

	}

	/**
	 * Loads all existing Events into a list.
	 */
	private void loadData(){
		allEvents = MeetUpScheduler.getEvents();
		allParks = MeetUpScheduler.getParks();
	}

	/**
	 * This creates the panel with the relevant information of the event.
	 */
	private void setUpInfoPanel() {
		infoPanel.add(eventName);
		infoPanel.add(eventCreator);
		infoPanel.add(eventCategory);
		infoPanel.add(eventLoc);
		infoPanel.add(eventTime);
		infoPanel.add(attendeePanel);
		infoPanel.add(eventNotes);
	}

	/**
	 * Helper to manage the list of attendees at the event.
	 */
	private void setUpAttendees() {
		for (int i = 0; i< members.size(); i++){
			attendeesBox.addItem(members.get(i));
		}
		attendeesBox.setVisibleItemCount(members.size());
		attendeeCount = members.size();
		attCountLabel.setText(attendeeCount + " people are attending.");

	}
	
	/**
	 * Sets up all the pieces of the search panel.
	 */
	private void setUpSearchPanel() {
		//Set up click handling for searchButton
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//Need to re-jigger this at some point to make it easier to add other search dimensions.
				setUpResultsTable();
				String parkFilter = parksBox.getValue(parksBox.getSelectedIndex());
				String catFilter = categoryBox.getValue(categoryBox.getSelectedIndex());
				ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
				if (catFilter.equals(ALL_CATS) && parkFilter.equals(ALL_PARKS)) {
					results = allEvents;
				}
				else if (catFilter.equals(ALL_CATS)) {
					for (HashMap<String, String> ev : allEvents) {
						if (ev.get("park_name").equals(parkFilter)) {
							results.add(ev);
						}
					}
				}
				else if (parkFilter.equals(ALL_PARKS)) {
					for (HashMap<String, String> ev : allEvents) {
						if (ev.get("category").equals(catFilter)) {
							results.add(ev);
						}
					}
				}
				else {
					for (HashMap<String, String> ev : allEvents) {
						if (ev.get("category").equals(catFilter) 
								&& ev.get("park_name").equals(parkFilter))
							results.add(ev);
					}
				}
				populateResults(results);			
			}
		});
		searchLabel.setHTML("<h2>Search for an event!</h2>");
		noResultsLabel.setHTML("<i>No results!</i>");
		getParks(parksBox);
		queryPanel.add(parksLabel);
		queryPanel.add(parksBox);
		populateCategories(categoryBox);
		queryPanel.add(categoryLabel);
		queryPanel.add(categoryBox);
		queryPanel.add(searchButton);
		setUpResultsTable();
		resultsPanel.add(noResultsLabel);
		bottomPanel.add(searchLabel);
		bottomPanel.add(queryPanel);
		bottomPanel.add(resultsPanel);
	}
	
	private void setUpResultsTable() {
		resultsPanel.remove(resultsTable);
		resultsPanel.remove(noResultsLabel);
		resultsTable = new FlexTable();
		resultsTable.setCellPadding(1);
		resultsTable.setCellSpacing(3);
		resultsTable.setText(1, 0, "Event Title");
		resultsTable.setText(1, 1, "Event Type");
		resultsTable.setText(1, 2, "Event Location");
		resultsTable.setText(1, 3, "Event Date");
		resultsTable.getCellFormatter().addStyleName(1, 0, "recentEventHeaders");
		resultsTable.getCellFormatter().addStyleName(1, 1, "recentEventHeaders");
		resultsTable.getCellFormatter().addStyleName(1, 2, "recentEventHeaders");
		resultsTable.getCellFormatter().addStyleName(1, 3, "recentEventHeaders");
		resultsPanel.add(resultsTable);
		resultsPanel.add(noResultsLabel);
	}
	
	private void populateResults(ArrayList<HashMap<String, String>> results) {
		if (!(results == null || results.isEmpty())){
			noResultsLabel.setVisible(false);
			for(HashMap<String, String> result : results){
				int row = resultsTable.getRowCount();
				resultsTable.setWidget(row, 0, new HTML("<a href=/?id=" + result.get("id") + "#EventPlace:Event" + ">" +
						result.get("name") + "</a>"));
				resultsTable.setText(row, 1, result.get("category"));
				resultsTable.setText(row, 2, result.get("park_name"));
				resultsTable.setText(row, 3, result.get("date"));
			}
		}
		else
			noResultsLabel.setVisible(true);
	}
	
	private void getParks(final ListBox parksList) {
		allParks = MeetUpScheduler.getParks();
		addParksToParksListBox(allParks, parksList);		
	}
	
	private void addParksToParksListBox(ArrayList<HashMap<String, String>> parks, ListBox parksList){
		ArrayList<String> parkNames = new ArrayList<String>();

		for(int i = 0; i<parks.size(); i++){
			parkNames.add(parks.get(i).get("name"));
		}

		Collections.sort(parkNames);

		parksList.addItem(ALL_PARKS);
		for(int i = 0; i<parks.size(); i++){
			parksList.addItem(parkNames.get(i));
		}
	}
	
	private void populateCategories(ListBox categoriesList) {
		categoriesList.addItem(ALL_CATS);
		for (String cat : MeetUpScheduler.getCategories())
			categoriesList.addItem(cat);
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Event " + name);
	}
}
