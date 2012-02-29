package com.cs310.ubc.meetupscheduler.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CreateEventView extends View {

	private final DataObjectServiceAsync objectService = GWT.create(DataObjectService.class);	
	//event fields
	private String eventName;
	private String userName;
	private String park;
	private String eventType;
	private String date;
	private String startTime;
	private String endTime;
	
	//Panel to hold Create Event Components
	private VerticalPanel createEventPanel = new VerticalPanel();
	//Name boxes
	//TODO: Add labels for these boxes
	private TextBox eventNameBox = new TextBox();
	private TextBox userNameBox = new TextBox();
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
	
	
	public CreateEventView() {
		//TODO: Default Constructor
	}
	
	public CreateEventView(String parkID) {
		park = parkID;
	}
	
	public VerticalPanel createPage() {				
		
		//TODO: Add change handlers to take values			
		
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
		//TODO: add change handlers		
		setHoursList(startTimeList);
		setHoursList(endTimeList);		
		
		//categories set-up
		categoriesListBox.addItem("Default Category");
		categoriesListBox.setVisibleItemCount(1);
		
		//get parks for parks list
		getParks(parksListBox);
		parksListBox.addItem("1");
		parksListBox.setVisibleItemCount(1);		
		
		Button submitButton = new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
							
				//TODO: Implement verifier call, submit call, and route to event view tab
				setEventFields();				
				
				if (verifyFields()) {
					createEvent();
					Window.alert("Event Created!");
				}
				Window.alert("Something went wrong!");
			}
		});
		
		//Add items to panel
		//TODO: fix appearance through sub-panels
		createEventPanel.add(eventNameBox);
		createEventPanel.add(userNameBox);
		createEventPanel.add(eventDatePickerLabel);
		createEventPanel.add(eventDatePicker);
		createEventPanel.add(startTimeList);
		createEventPanel.add(endTimeList);
		createEventPanel.add(parksListBox);
		createEventPanel.add(categoriesListBox);
		createEventPanel.add(submitButton);
		return createEventPanel;
	}
	
	private void getParks(final ListBox parksList) {
		
		objectService.get("Park", "*", new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			@Override
			public void onFailure(Throwable error) {
				//TODO: replace with actual table flip
				System.out.println("Table Flip!");
			}
			
			public void onSuccess(ArrayList<HashMap<String, String>> parks) {
				addParksToParksListBox(parks, parksList);
			}
		});
	}
	
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
	
	private void setEventFields() {
		
		eventName = eventNameBox.getValue();
		userName = userNameBox.getValue();
		//TODO: Fix this so that it actually gets the park_id and not the name
		park = parksListBox.getValue(parksListBox.getSelectedIndex());
		eventType = categoriesListBox.getValue(categoriesListBox.getSelectedIndex());
		date = DateTimeFormat.getMediumDateFormat().format(eventDatePicker.getValue());
		startTime = startTimeList.getValue(startTimeList.getSelectedIndex());
		endTime = endTimeList.getValue(endTimeList.getSelectedIndex());		
	}
	
	private boolean verifyFields() {
		//TODO: specify conditions for field verification, add field specific errors
		if (eventName == null || userName == null || park == null || date == null || startTime == null || endTime == null) return false;
		return true;
	}
	
	private void createEvent() {
		HashMap<String, String> event = new HashMap<String, String>();
		event.put("name", eventName);
		event.put("park_id", park);
		event.put("creator", userName);
		event.put("category", eventType);
		event.put("date", date);
		event.put("start_time", startTime);
		event.put("end_time", endTime);
		//TODO: Make data object service call to create event		
		System.out.println(event);
	}
	
	private void setHoursList(ListBox list) {
		for (int i=0; i<=23; i++ ) {
			list.addItem(i+":00");
		}
		list.setVisibleItemCount(1);
	}
}
