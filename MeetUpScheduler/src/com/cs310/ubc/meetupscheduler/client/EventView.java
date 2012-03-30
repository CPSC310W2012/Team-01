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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

//TODO: Maybe move search to a separate page?
/**
 * EventView for looking at the details of a specific event
 * and searching for events.
 */
public class EventView extends Composite implements View{
	/**
	 * EventView for looking at the details of a specific event
	 * 
	 * @author Ben
	 */
	


	
	
	//Constants
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;	
	private final String ALL_PARKS = "All parks";
	private final String ALL_CATS = "All types";
	private final String JOIN_TEXT = "Join event!";
	private final String UNJOIN_TEXT = "Unjoin event!";
	//panels	
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel eventPanel = new HorizontalPanel();	
	private VerticalPanel queryPanel = new VerticalPanel();
	private VerticalPanel resultsPanel = new VerticalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	private HorizontalPanel sharePanel = new HorizontalPanel();
	private Label eventName = new Label();
	//buttons
	private Button joinButton = new Button();
	private HorizontalPanel joinButtonPanel = new HorizontalPanel();
	private HorizontalPanel searchButtonPanel = new HorizontalPanel();
	private VerticalPanel parkPanel = new VerticalPanel();
	private ListBox attendeesBox = new ListBox();
	private VerticalPanel attendeePanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	//labels
	private Label eventCreator = new Label();
	private Label eventLoc = new Label();
	private Label eventTime = new Label();
	private Label eventNotes = new Label();
	private Label eventCategory = new Label();
	private Label searchLabel = new Label("Search for an Event");
	


	private MapWidget eventMap;
	private ArrayList<String> members = new ArrayList<String>();
	private Integer attendeeCount = 0;
	private Label attCountLabel = new Label();
	private ArrayList<HashMap<String, String>> allEvents;
	
	//Search-related elements	
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
		return eventPanel;
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

		//attendee panel setup
		attCountLabel.addStyleDependentName("attCount");
		attendeePanel.add(attCountLabel);
		attendeesBox.addStyleDependentName("attendees");
		attendeePanel.add(attendeesBox);
		

		//Set up panels
		setUpJoinButton();
		setUpInfoPanel();
		setUpSearchPanel();

		// Add items to panels
		loadEvent(eventURLID);
		parkPanel.add(eventMap);

		parkPanel.addStyleName("parkPanel");
				
		leftPanel.add(parkPanel);
		
		infoPanel.addStyleName("infoPanel");
		rightPanel.add(infoPanel);
		queryPanel.addStyleName("queryPanel");
		rightPanel.add(queryPanel);
		resultsPanel.addStyleName("resultsPanel");
		rightPanel.add(resultsPanel);
		
		eventPanel.addStyleName("eventPanel");
		eventPanel.add(leftPanel);
		eventPanel.add(rightPanel);
		
		//	rootPanel.add(shareButton);			
		
		if (!eventLoaded) {
			topPanel.setVisible(false);			
		}
		

		renderPlusButton();
	}
	
	/**
	 * Sets up the click handlers for the join/unjoin button
	 */
	private void setUpJoinButton() {
		//set up the joinButton
		joinButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent clickEvent) {
				if (joinButton.getText().equals(JOIN_TEXT)) {
					members.add(loginInfo.getNickname());
					//Add username to event
					String newNames = addToString(event.get("attending_names"), loginInfo.getNickname());
					event.put("attending_names", newNames);
					//Add user email to event
					String newEmails = addToString(event.get("attending_emails"), loginInfo.getEmailAddress());
					event.put("attending_emails", newEmails);
					//Increment number attending
					Integer numAttending = Integer.parseInt(event.get("num_attending"));
					numAttending++;
					event.put("num_attending", numAttending.toString());
					updateEvent(event);
					joinButton.setText(UNJOIN_TEXT);
				}
				else if (joinButton.getText().equals(UNJOIN_TEXT)) {
					//Remove name from display window.
					members.remove(loginInfo.getNickname());
					//Remove name from event
					String newAttendees = removeFromString(event.get("attending_names"), loginInfo.getNickname());
					event.put("attending_names", newAttendees);
					//Remove email from event
					String newEmails = removeFromString(event.get("attending_emails"), loginInfo.getEmailAddress());
					event.put("attending_emails", newEmails);
					//Decrement number attending
					Integer numAttending = Integer.parseInt(event.get("num_attending"));
					numAttending--;
					event.put("num_attending", numAttending.toString());
					updateEvent(event);
					joinButton.setText(JOIN_TEXT);
				}
			}
		});
	}
	
	/**
	 * Updates an event on the server when someone has joined/unjoined it.
	 * @param event The new values of the event to be updated.
	 */
	private void updateEvent(HashMap<String, String> event) {
		objectService.update("Event", "id=="+event.get("id"), event, new AsyncCallback<ArrayList<HashMap<String, String>>>() {
			public void onFailure(Throwable error) {
				System.out.println("Joining the event failed!");
			}
			public void onSuccess(ArrayList<HashMap<String, String>> newEvent) {
				attendeesBox.clear();
				setUpAttendees();
			}
		});
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
	
		sharePanel.add(h);

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
						eventName.setText(event.get("name")); 
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
							joinButton.setText(UNJOIN_TEXT);
						else
							joinButton.setText(JOIN_TEXT);
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
		eventName.addStyleDependentName("eventEventName");
		infoPanel.add(eventName);
		eventCreator.addStyleDependentName("eventCreator");
		infoPanel.add(eventCreator);
		eventCategory.addStyleDependentName("eventCategory");
		infoPanel.add(eventCategory);
		eventLoc.addStyleDependentName("eventLoc");
		infoPanel.add(eventLoc);
		eventTime.addStyleDependentName("eventTime");
		infoPanel.add(eventTime);
		attendeePanel.addStyleName("attendeePanel");
		infoPanel.add(attendeePanel);
		eventNotes.addStyleDependentName("eventNotes");
		infoPanel.add(eventNotes);
		joinButton.addStyleDependentName("join");
		joinButtonPanel.add(joinButton);
		joinButtonPanel.addStyleName("joinButtonPanel");
		sharePanel.add(joinButtonPanel);
		sharePanel.addStyleName("sharePanel");		
		infoPanel.add(sharePanel);
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
		searchLabel.addStyleDependentName("search");
		queryPanel.add(searchLabel);
		setUpSearchButton();		
		noResultsLabel.setHTML("<i>No results!</i>");
		getParks(parksBox);
		parksLabel.addStyleDependentName("parks");
		queryPanel.add(parksLabel);
		parksBox.addStyleDependentName("parks");
		queryPanel.add(parksBox);
		populateCategories(categoryBox);
		categoryLabel.addStyleDependentName("categories");
		queryPanel.add(categoryLabel);
		categoryBox.addStyleDependentName("categories");
		queryPanel.add(categoryBox);
		searchButton.addStyleDependentName("search");
		searchButtonPanel.add(searchButton);
		searchButtonPanel.addStyleName("searchButtonPanel");
		queryPanel.add(searchButtonPanel);		
		setUpResultsTable();
		resultsPanel.add(noResultsLabel);
		resultsPanel.addStyleName("resultsPanel");
	}
	
	/**
	 * Sets up search button click handling
	 */
	private void setUpSearchButton() {
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
	}
	/**
	 * Sets up table to hold search results. Used when page is loaded
	 * and to clear table when new query is performed.
	 */
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
	
	/**
	 * Method to populate the results table with search results
	 * @param results The results of a search query
	 */
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
	
	/**
	 * Gets all parks and populates parks list box
	 * @param parksList The listbox to populate
	 */
	private void getParks(final ListBox parksList) {
		allParks = MeetUpScheduler.getParks();
		addParksToParksListBox(allParks, parksList);		
	}
	
	/**
	 * Helper method to populate parks list box.
	 * @param parks The list of parks
	 * @param parksList The box to populate
	 */
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
	
	/**
	 * Helper method to populate category listbox
	 * @param categoriesList The listbox to populate
	 */
	private void populateCategories(ListBox categoriesList) {
		categoriesList.addItem(ALL_CATS);
		for (String cat : MeetUpScheduler.getCategories())
			categoriesList.addItem(cat);
	}
	
	/**
	 * Removes an element from a comma separated string and returns the
	 * new string.
	 * @param csList The string to change
	 * @param element The element to remove
	 * @return The CS-list minus the element
	 */
	private String removeFromString(String csList, String element) {
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(csList.split(",")));
		list.remove(element);
		StringBuilder sb = new StringBuilder();
		for (String item : list)
			sb.append(item + ",");
		String output = sb.toString();
		if (output.length() > 0)
			output = output.substring(0, output.length() -1);
		return output;
	}
	
	/**
	 * Adds an element to a comma separated string and returns the
	 * new string
	 * @param csList The string to change
	 * @param element The element to add
	 * @return The cs-list with the element included
	 */
	private String addToString(String csList, String element) {
		String newString = csList;
		if (newString == null)
			newString = "";
		if (!newString.isEmpty())
			newString += ",";
		newString += element;
		return newString;
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Event " + name);
	}

}
