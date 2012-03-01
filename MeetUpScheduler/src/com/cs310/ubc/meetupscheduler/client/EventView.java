package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs310.ubc.meetupscheduler.server.Event;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;



public class EventView extends View{
	/**
	 * These are the UI components of the View
	 */
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;
	private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);	
	
	private String currEventID;
	private String currEventName;
	private String currEventparkID;
	private String currEventnumAttend;
	private String currEventCreator;
	private String currEventCategory;
	private String currEventDate;
	private String currEventStartTime;
	private String currEventEndTime;
	
	private HorizontalPanel rootPanel = new HorizontalPanel();
	private Label eventName = new Label();
	private Button joinButton = new Button();
	private DialogBox joinBox = new DialogBox();
	private TextBox joinName = new TextBox();
	private VerticalPanel parkPanel = new VerticalPanel();
	private ListBox attendees = new ListBox();
	private VerticalPanel attendeePanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private Label eventLoc = new Label();
	private Label eventTime = new Label();
	private Label eventNotes = new Label();
	private ArrayList<String> members;
	private Integer attendeeCount = 0;
	Label attCountLabel = new Label();
	private final DataObjectServiceAsync eventService = GWT.create(DataObjectService.class);
	private ArrayList<HashMap<String, String>> allEvents;
	




//	public Widget createPage (Event event) {
//		currentEvent = event;
//		//Initialize Map, needs a key before it can be deployed
//		Maps.loadMapsApi("", "2", false, new Runnable() {
//			public void run() {
//				buildUI();
//			}
//		});
//		return panel;
//	}
	
	public HorizontalPanel createPage() {
		//Initialize Map, needs a key before it can be deployed
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUI();
			}
		});
		return rootPanel;
	}
	
	public void buildUI(){
		// Create and load the sample event
		buildSampleEvent();
		loadEvent(1);
		
		// set up the Map
		LatLng vancouver = LatLng.newInstance(49.258480, -123.094574);
		final MapWidget eventMap = new MapWidget(vancouver, 11);

		eventMap.setPixelSize(MAP_WIDTH, MAP_HEIGHT);
		eventMap.setScrollWheelZoomEnabled(true);
		eventMap.addControl(new LargeMapControl3D());
		
		//set up the eventName label
		eventName.setText("This is the event name"); // TODO: get proper enum settings
		eventTime.setText("3:00pm");
		eventLoc.setText("Jericho Park");
		eventNotes.setText("This is the information for the event. People should bring their own soccer ball to the game. Shinpads mandatory. \n Make sure to bring water!");


		//set up the joinButton
		
		joinButton.setText("Join event!");
		joinButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				joinBox.show();
			}
		});
	
		
		//Add the joinBox
		setJoinBox();
		
		

		members = new ArrayList<String>();
		members.add("Adrian");
		members.add("Ben");
		members.add("Caroline");
		members.add("Connor");
		members.add("Dave");
		setUpAttendees();
		
		//Put the UI together
		
		setUpInfoPanel();
		eventMap.setSize("500px", "400px");
		eventMap.checkResizeAndCenter();
		parkPanel.add(eventMap);
		attendeePanel.add(attCountLabel);
		attendeePanel.add(attendees);
		
		rootPanel.add(infoPanel);

		rootPanel.add(joinButton);
		rootPanel.add(parkPanel);
	}

	private void loadEvent(int eventID) {
		// TODO Auto-generated method stub
		
	}
	
	private void loadEvents(){
		eventService.get("Event", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("You're doing it wrong!");
			}

			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> events) {
				allEvents = events;
			}
		});
	}

	private void buildSampleEvent() {
		HashMap<String, String> sampleEventMap = new HashMap<String, String>();
		sampleEventMap.put("id", "1");
		sampleEventMap.put("name","Champion's League");
		sampleEventMap.put("park_id", "1");
		sampleEventMap.put("num_attending","0");
		sampleEventMap.put("creator","Ben");
		sampleEventMap.put("category","Soccer");
		sampleEventMap.put("date","Tomorrow");
		sampleEventMap.put("start_time","1:00");
		sampleEventMap.put("end_time", "4:00");

		objectService.add("Event", sampleEventMap, new AsyncCallback<HashMap<String, String>>() {
			public void onFailure(Throwable error) {
				System.out.println("Flip a table!  (>o.o)> _|__|_)");
			}
			
			public void onSuccess(HashMap<String, String> newEvent) {
				//TODO: Add call to helper to scheduler to get event view based on event id
				System.out.println(newEvent);
				Window.alert("Event Created!");
			}
		});
		
	}

	private void setUpInfoPanel() {
		infoPanel.add(eventLoc);
		infoPanel.add(eventTime);
		infoPanel.add(attendeePanel);
		infoPanel.add(eventNotes);
	}

	private void setUpAttendees() {
		 for (int i = 0; i< members.size(); i++){
			 attendees.addItem(members.get(i));
		 }
		attendees.setVisibleItemCount(members.size());
		attendeeCount = members.size();
		attCountLabel.setText(attendeeCount + " people are attending.");
		
	}

	protected void setJoinBox() {
		
		//Hide joinBox and set the caption
		joinBox.hide();
		joinBox.setText("Join this event!");
		
		//Create the contents of joinBox
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
			//TODO: Set this to attendee list	
				members.add(joinName.getValue());
				attendees.clear();
				setUpAttendees();
				joinName.setText(null);
				joinBox.hide();
				//TODO: Refresh the attendee list after adding new attendee.
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
	


	//TODO: 
	/**
	 *  
	 * make Attendees a JDO, and persistent
	 * Catch exceptions from creating jdo objects, etc.
	 * 
	 */
}
