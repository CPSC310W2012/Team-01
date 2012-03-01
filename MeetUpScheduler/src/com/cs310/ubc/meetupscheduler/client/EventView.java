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
	private HashMap<String, String> sampleEventMap;
	private Event sampleEvent;
	




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
		// set up the Map
		buildSampleEvent();
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

	private void buildSampleEvent() {
		sampleEventMap.put("ID", "1");
		sampleEventMap.put("Name","Champion's League");
		sampleEventMap.put("PID", "1");
		sampleEventMap.put("ATTND","0");
		sampleEventMap.put("CREATOR","Ben");
		sampleEventMap.put("CAT","Soccer");
		sampleEventMap.put("DATE","Tomorrow");
		sampleEventMap.put("START","1:00");
		sampleEventMap.put("END", "4:00");
		try {
			sampleEvent = new Event(sampleEventMap);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
//	/**
//	 * This method makes the AsyncCallback to get an ArrayList of events stored
//	 * in a HashMap. On success recent events table is loaded and event marker 
//	 * overlays are placed on the map.
//	 * 
//	 * @param map The Google Maps widget that gets the event marker overlays
//	 */
//	//TODO: popup for errors, Async for load recent events?
//	private void loadEvents(final MapWidget map){
//		eventService.get("Event", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>(){
//			@Override
//			public void onFailure(Throwable caught) {
//				System.out.println("oh noes event data didnt werks");
//			}
//
//			@Override
//			public void onSuccess(ArrayList<HashMap<String, String>> events) {
//				allEvents = events;
//				
//			}
//		});
//	}

	//TODO: 
	/**
	 *  
	 * make Attendees a JDO, and persistent
	 * Catch exceptions from creating jdo objects, etc.
	 * 
	 */
}
