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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public class EventView extends Composite implements View{
	/**
	 * EventView for looking at the details of a specific event
	 * 
	 * @author Ben
	 */
	//map constants
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;	


	private TextBox loadText = new TextBox();
	//Element Panels
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
	private Button loadButton = new Button();
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
	private Button shareButton = new Button();
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
	private Label categoryLabel = new Label("Choose an event category: ");

	private SimplePanel viewPanel = new SimplePanel();
	private ArrayList<HashMap<String, String>> allParks;
	Element nameSpan = DOM.createSpan();
	private HashMap<String, String> event = new HashMap<String, String>();
	private LoginInfo loginInfo;
	private int eventURLID;
	private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);


	public EventView() {
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
	}
	public EventView(int eventID){
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
		loadEvent(eventID);
	}

	// TODO: Make this into a working constructor that takes an eventID string
	//	public Widget createPage (String eventID) {
	//		currentEvent = event;
	//		//Initialize Map, needs a key before it can be deployed
	//		Maps.loadMapsApi("", "2", false, new Runnable() {
	//			public void run() {
	//				buildUI();
	//			}
	//		});
	//		return panel;
	//	}

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
		if(eventStringID != null && !eventStringID.isEmpty() ){
			eventURLID = Integer.parseInt(eventStringID);
		}
		loginInfo = MeetUpScheduler.SharedData.getLoginInfo();
		loadData();

		// set up the Map
		// TODO: Make the map relevant. Load markers of the location, etc.
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		eventMap = new MapWidget(vancouver, 11);

		eventMap.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		eventMap.setScrollWheelZoomEnabled(true);
		eventMap.addControl(new LargeMapControl3D());
		eventMap.checkResizeAndCenter();
		eventMap.addControl(new MapTypeControl());
		// Sets the eventLoad button to load the events

		loadText.setText("Enter the number of the event to load");
		loadButton.setText("Click to load an event");
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadEvent(Integer.parseInt(loadText.getText()));
			}
		});



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

		//attendee panel setup
		attCountLabel.addStyleDependentName("attCount");
		attendeePanel.add(attCountLabel);
		attendeesBox.addStyleDependentName("attendees");
		attendeePanel.add(attendeesBox);
		
		setUpInfoPanel();
		setUpSearchPanel();

		// Add items to panels
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
		
		loadEvent(eventURLID);
		renderPlusButton();


	}
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
		System.out.println(com.google.gwt.user.client.Window.Location.getParameter("id"));
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
	
	private void setUpSearchPanel() {
		searchLabel.addStyleDependentName("search");
		queryPanel.add(searchLabel);
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

		parksList.addItem("All parks");
		for(int i = 0; i<parks.size(); i++){
			parksList.addItem(parkNames.get(i));
		}
	}
	
	private void populateCategories(ListBox categoriesList) {
		categoriesList.addItem("All categories");
		for (String cat : MeetUpScheduler.getCategories())
			categoriesList.addItem(cat);
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Event " + name);
	}

	
}
