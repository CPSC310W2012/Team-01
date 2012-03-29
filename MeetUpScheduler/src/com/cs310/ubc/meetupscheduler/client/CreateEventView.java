package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * This class contains the logic and appearance of the create event page.
 *
 */
public class CreateEventView extends Composite implements View {
	private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);
	private ArrayList<HashMap<String, String>> allParks;
	//event fields
	private String eventName;
	private String userName;
	private String userEmail;
	private String park_name;
	private String park_id;
	private String eventType;
	private String date;
	private String startTime;
	private String endTime;
	private String notes;

	//Panel to hold Create Event Components
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private HorizontalPanel createEventPanel = new HorizontalPanel();
	//Name boxes	
	private TextBox eventNameBox = new TextBox();
	private Label eventNameLabel = new Label("Event Name");
	private Label notesLabel = new Label("Notes");
	//Date Selector for Event
	private DatePicker eventDatePicker = new DatePicker();
	private Label eventDatePickerLabel = new Label();
	//Time selectors
	private ListBox startTimeList = new ListBox();
	private ListBox endTimeList = new ListBox();
	//Category selector
	private ListBox categoriesListBox = new ListBox();
	//Park Selector
	private ListBox parksListBox = new ListBox();
	private SimplePanel viewPanel = new SimplePanel();
	private Element nameSpan = DOM.createSpan();
	//Notes field
	private TextArea notesField = new TextArea();

	/**
	 * Constructor that does not specify a park.
	 */
	public CreateEventView() {
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
	}

	/**
	 * Constructor that takes parkID of event
	 * @param parkID The id of the park at which to create the event.
	 */
	public CreateEventView(String parkID) {
		viewPanel.getElement().appendChild(nameSpan);
		initWidget(viewPanel);
		park_id = parkID;
	}

	/**
	 * Renders create event page
	 */
	@Override
	public Widget asWidget() {					

		//Sets the label when a date is selected		
		eventDatePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date eventDate = event.getValue();
				String eventDateString = DateTimeFormat.getMediumDateFormat().format(eventDate);
				eventDatePickerLabel.setText(eventDateString);
			}
		}); 
		//Default Value for date
		eventDatePicker.setValue(new Date(), true);

		//populate time lists 			
		setHoursList(startTimeList);
		setHoursList(endTimeList);		

		//categories set-up
		categoriesListBox.addItem("Default Category");
		populateCategories(categoriesListBox);
		categoriesListBox.setVisibleItemCount(1);

		//get parks for parks list
		getParks(parksListBox);		
		parksListBox.setVisibleItemCount(1);		

		Button submitButton = new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				setEventFields();				

				if (verifyFields()) {
					createEvent();					
				}
				else {
					Window.alert("Please ensure all event fields are filled out and that the notes field is under 500 characters.");
				}
			}
		});

		//Setup notes field
		notesField.getElement().getStyle().setProperty("resize", "none");
		notesField.setWidth("250px");
		notesField.setHeight("100px");
		//Add items to panel
		//TODO: fix appearance through sub-panels
		leftPanel.add(eventNameLabel);
		leftPanel.add(eventNameBox);
		leftPanel.add(parksListBox);
		leftPanel.add(categoriesListBox);
		leftPanel.add(notesLabel);
		leftPanel.add(notesField);
		leftPanel.add(submitButton);

		rightPanel.add(eventDatePickerLabel);
		rightPanel.add(eventDatePicker);
		rightPanel.add(startTimeList);
		rightPanel.add(endTimeList);


		createEventPanel.add(leftPanel);
		createEventPanel.add(rightPanel);		

		return createEventPanel;
	}

	/**
	 * Gets a list of all parks from the server and populates parks listbox
	 * @param parksList The listbox to populate
	 */
	private void getParks(final ListBox parksList) {
		allParks = MeetUpScheduler.getParks();
		addParksToParksListBox(allParks, parksList);		

	}

	/**
	 * Populates the park listbox
	 * @param parks List of parks
	 * @param parksList The listbox to populate
	 */
	private void addParksToParksListBox(ArrayList<HashMap<String, String>> parks, ListBox parksList){
		ArrayList<String> parkNames = new ArrayList<String>();

		for(int i = 0; i<parks.size(); i++){
			parkNames.add(parks.get(i).get("name"));
		}

		Collections.sort(parkNames);

		for(int i = 0; i<parks.size(); i++){
			parksList.addItem(parkNames.get(i));
		}
	}

	/**
	 * Returns the id of a park given its name
	 * @param parkName The name of the park
	 * @return The id of the park
	 */
	private String getParkID(String parkName) {
		String id = "";		
		for (int i=0;i<allParks.size(); i++) {
			if (allParks.get(i).get("name").equals(parkName)) {
				id = allParks.get(i).get("id");
			}
		}
		return id;
	}

	/**
	 * Get values from user inputted fields.
	 */
	private void setEventFields() {
		eventName = eventNameBox.getValue();
		LoginInfo loginInfo = MeetUpScheduler.SharedData.getLoginInfo();
		userName = loginInfo.getNickname();
		userEmail = loginInfo.getEmailAddress();		
		park_name = parksListBox.getValue(parksListBox.getSelectedIndex());
		park_id = getParkID(park_name);
		eventType = categoriesListBox.getValue(categoriesListBox.getSelectedIndex());
		date = DateTimeFormat.getMediumDateFormat().format(eventDatePicker.getValue());
		startTime = startTimeList.getValue(startTimeList.getSelectedIndex());
		endTime = endTimeList.getValue(endTimeList.getSelectedIndex());
		notes = notesField.getText();
	}

	/**
	 * Checks if user inputted values are valid.
	 * @return true if valid, false if not
	 */
	private boolean verifyFields() {
		//TODO: specify conditions for field verification, add field specific errors
		if (eventName == null || userName == null || park_name == null || date == null || startTime == null || endTime == null || notes.length() > 500) return false;
		return true;
	}

	/**
	 * Creates event and saves it to server.
	 */
	//TODO: Use the ENUM from event for the field names. Note: breaks compilation at this point
	private void createEvent() {
		HashMap<String, String> event = new HashMap<String, String>();
		event.put("name", eventName);
		event.put("park_name", park_name);
		event.put("park_id", park_id);
		event.put("creator_name", userName);
		event.put("creator_email", userEmail);
		event.put("category", eventType);
		event.put("date", date);
		event.put("start_time", startTime);
		event.put("end_time", endTime);
		event.put("num_attending", "1");
		event.put("attending_names", userName);
		event.put("attending_emails", userEmail);
		event.put("notes", notes);

		//TODO: move to static data object? Make call to reload method to re get events and load views
		objectService.add("Event", event, new AsyncCallback<HashMap<String, String>>() {
			public void onFailure(Throwable error) {
				System.out.println("Flip a table!  (>o.o)> _|__|_)");
			}

			public void onSuccess(HashMap<String, String> newEvent) {
				//TODO: Find a method of either refreshing the events or adding the newly created event to the event list *before* going to the new URL.
				// NOTE: This only works in the deployed version.
				MeetUpScheduler.addEvent(newEvent);
				String newEventURL = "http://vancitymeetupscheduler.appspot.com?id=" + newEvent.get("id") + "#EventPlace:Event";
				Window.Location.replace(newEventURL);
				
			}
		});

	}

	/**
	 * Method to populate values in hours box.
	 * @param list The list to populate
	 */
	private void setHoursList(ListBox list) {
		for (int i=0; i<=23; i++ ) {
			list.addItem(i+":00");
		}
		list.setVisibleItemCount(1);
	}

	/**
	 * Method to populate categories list
	 * @param categoriesList The list to populate
	 */
	private void populateCategories(ListBox categoriesList) {
		for (String cat : MeetUpScheduler.getCategories())
			categoriesList.addItem(cat);
	}

	@Override
	public void setName(String name) {
		nameSpan.setInnerText("Create Event " + name);

	}
}
