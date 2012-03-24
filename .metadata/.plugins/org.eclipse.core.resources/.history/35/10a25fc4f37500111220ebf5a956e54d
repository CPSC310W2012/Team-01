package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
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
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;	


	private TextBox loadText = new TextBox();
	private HorizontalPanel rootPanel = new HorizontalPanel();
	private Label eventName = new Label();
	private Button joinButton = new Button();
	private DialogBox joinBox = new DialogBox();
	private TextBox joinName = new TextBox();
	private Button loadButton = new Button();
	private VerticalPanel parkPanel = new VerticalPanel();
	private ListBox attendeesBox = new ListBox();
	private VerticalPanel attendeePanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private Label eventCreator = new Label();
	private Label eventLoc = new Label();
	private Label eventTime = new Label();
	private Label eventNotes = new Label();
	private Label eventCategory = new Label();
	private Button shareButton = new Button();
	private MapWidget eventMap;
	private ArrayList<String> members = new ArrayList<String>();
	private Integer attendeeCount = 0;
	private Label attCountLabel = new Label();
	private ArrayList<HashMap<String, String>> allEvents;
    private SimplePanel viewPanel = new SimplePanel();
    private ArrayList<HashMap<String, String>> allParks;
    Element nameSpan = DOM.createSpan();
    private HashMap<String, String> event = new HashMap<String, String>();


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
		return rootPanel;
	}

	/**
	 * Constructs the UI elements of EventView
	 */
	public void buildUI(){
		
		loadData();

		// set up the Map
		// TODO: Make the map relevant. Load markers of the location, etc.
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		eventMap = new MapWidget(vancouver, 11);

		eventMap.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		eventMap.setScrollWheelZoomEnabled(true);
		eventMap.addControl(new LargeMapControl3D());
		eventMap.checkResizeAndCenter();
		
		// Sets the eventLoad button to load the events

		loadText.setText("Enter the number of the event to load");
		loadButton.setText("Click to load an event");
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadEvent(Integer.parseInt(loadText.getText()));
			}
		});
		
		//set up the shareButton
		//TODO: Make this work with the proper URL
		
		shareButton.setText("Share on Google Plus.");
		shareButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				com.google.gwt.user.client.Window.open("https://plus.google.com/share?url=vancitymeetupscheduler.appspot.com", "Share the Meetup Scheduler!", "");
			}
		});
		
		//set up the joinButton
		joinButton.setText("Join event!");
		joinButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				joinBox.show();
			}
		});


		
		setJoinBox();


//		// Sets up a temporary attendee list.
//		members = new ArrayList<String>();
//		members.add("Adrian");
//		members.add("Ben");
//		members.add("Caroline");
//		members.add("Connor");
//		members.add("Dave");
//		setUpAttendees();


		setUpInfoPanel();

		
		// Add items to panels
		parkPanel.add(eventMap);
		attendeePanel.add(attCountLabel);
		attendeePanel.add(attendeesBox);
		rootPanel.add(loadText);
		rootPanel.add(loadButton);
		rootPanel.add(infoPanel);
		rootPanel.add(joinButton);
		rootPanel.add(shareButton);
		rootPanel.add(parkPanel);
		
	}

	/**
	 * This loads the specifics of the event into the info panel.
	 * 
	 * @param eventID: The id of the event you want to load to the page.
	 * 
	 * TODO: - Add event positions to map
	 * 		 - Get the park information loaded into a parks page
	 * 		 - Set real attendees list.
	 */
	private void loadEvent(int eventID) {

		
		if (allEvents != null){
			try { 
				for (int i = 0; i < allEvents.size(); i++){
					if (Integer.parseInt(allEvents.get(i).get("id")) == eventID){
				
					event = allEvents.get(i);
					eventName.setText("The name of the event is " + event.get("name")); // TODO: get proper enum settings
					eventTime.setText("The event is from " + event.get("start_time") + " to " + event.get("end_time") + " on " + event.get("date"));
					eventLoc.setText(event.get("park_id") + " is the park ID.");
					eventCreator.setText(event.get("creator") + " is the event creator.");
					eventMap.checkResizeAndCenter();
					eventCategory.setText("This event is in the category: " + event.get("category"));
//					for (int j = 0; j < (event.get(ATTND_NAMES).size()); j++){
//						members.add(event.get(members.get(i)));	
//					}
					Window.alert("WHEEEEE");
				
					}
				}
				
			} catch (Exception e) {
				Window.alert("There is no event " + eventID + ".");
			}
		}
		else Window.alert("No events!");

	}


	/**
	 * Loads all existing Events into a list.
	 */
	private void loadData(){
		allEvents = MeetUpScheduler.getEvents();
		allParks = MeetUpScheduler.getParks();
		Window.alert("Data loaded from MeetUpScheduler!");
//		for (int i=0; i<allEvents.size(); i++){
//			HashMap<String, String> tempEvent = allEvents[i];
//			System.out.println("Loaded event " + allEvents);
//		}
	}

	
	/**
	 * This creates the panel with the relevant information of the event.
	 */
	private void setUpInfoPanel() {
		infoPanel.add(eventCreator);
		infoPanel.add(eventCategory);
		infoPanel.add(eventLoc);
		infoPanel.add(eventTime);
		infoPanel.add(attendeePanel);
		infoPanel.add(eventNotes);
		eventNotes.setText("This is the information for the event. Needs to be persistent.");
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
	 * Helper to set up the DialogBox that pops up when you click the Join Event button.
	 */
	protected void setJoinBox() {

		//Set up the joinBox
		joinBox.hide();
		joinBox.setText("Join this event!");
		VerticalPanel joinContents = new VerticalPanel();
		joinContents.setSpacing(4);
		joinBox.setWidget(joinContents);

		//Add to joinContents
		joinContents.add(joinName);
		joinName.setText("Please enter your name");

		// add the Submit Button
		Button submitButton = new Button();
		joinContents.add(submitButton);
		submitButton.setText("Join");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				members.add(joinName.getValue());
				attendeesBox.clear();
				setUpAttendees();
				joinName.setText(null);
				joinBox.hide();
			}
		});
		
		//Add a cancel button
		Button cancelButton = new Button();
		cancelButton.setText("Cancel");

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				joinBox.hide();
			}
		});
		joinContents.add(cancelButton);



	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Event " + name);
	}



	//TODO: 
	/**
	 *  Add styling with CSS
	 *  Fix up the interface to make it look nice
	 * make Attendees a JDO, and persistent
	 * Catch exceptions from creating jdo objects, etc.
	 * 
	 */
}
