package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private final int MAP_HEIGHT = 400;
	private final int MAP_WIDTH = 500;	


	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private HorizontalPanel rootPanel = new HorizontalPanel();
	private HTML eventName = new HTML();
	private Button joinButton = new Button();
	private ListBox attendeesBox = new ListBox();
	private VerticalPanel attendeePanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private HTML eventCreator = new HTML();
	private HTML eventLoc = new HTML();
	private HTML eventTime = new HTML();
	private HTML eventCategory = new HTML();
	private Button shareButton = new Button();
	private MapWidget eventMap;
	private ArrayList<String> members = new ArrayList<String>();
	private Integer attendeeCount = 0;
	private HTML attCountLabel = new HTML();
	private ArrayList<HashMap<String, String>> allEvents;
    private SimplePanel viewPanel = new SimplePanel();
    private ArrayList<HashMap<String, String>> allParks;
    Element nameSpan = DOM.createSpan();
    private HashMap<String, String> event = new HashMap<String, String>();
    private LoginInfo loginInfo;
    
    private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);


    public EventView() {
        viewPanel.getElement().appendChild(nameSpan);
		loadData();
        initWidget(viewPanel);
    }
    
    public EventView(int eventID){
    	viewPanel.getElement().appendChild(nameSpan);
		loadData();
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
		loginInfo = MeetUpScheduler.SharedData.getLoginInfo();

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
		
		// Add items to panels
		rightPanel.add(eventMap);
		attendeePanel.add(attCountLabel);
		attendeePanel.add(attendeesBox);
		leftPanel.add(infoPanel);
		leftPanel.add(attendeePanel);
		leftPanel.add(joinButton);
		leftPanel.add(shareButton);
		rootPanel.add(leftPanel);
		rootPanel.add(rightPanel);
	}

	/**
	 * This loads the specifics of the event into the info panel.
	 * 
	 * @param eventID: The id of the event you want to load to the page.
	 * 
	 * TODO: - Add event positions to map
	 * 		 - Get the park information loaded into a parks page
	 */
	private void loadEvent(int eventID) {


		if (allEvents != null && allEvents.size() > 0){
			try { 
				for (int i = 0; i < allEvents.size(); i++){
					if (Integer.parseInt(allEvents.get(i).get("id")) == eventID){
						event = allEvents.get(i);
						eventName.setHTML("<h1>" + event.get("name") + "</h1><br/>"); 
						eventCreator.setHTML("<b>Creator:</b> " + event.get("creator_name") + "<br/>");
						eventTime.setHTML("<b>Time</b>: " + event.get("date") + " from " + event.get("start_time") + " to " + event.get("end_time") + "<br/>");
						eventLoc.setHTML("<b>Location</b> " + event.get("park_name") + "<br/>");

						//eventMap.checkResizeAndCenter();
						eventCategory.setHTML("<b>Category:</b> " + event.get("category") + "<br/>");
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
				e.printStackTrace();
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
		attCountLabel.setHTML("<b>" + attendeeCount + "</b> people are attending.");

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
