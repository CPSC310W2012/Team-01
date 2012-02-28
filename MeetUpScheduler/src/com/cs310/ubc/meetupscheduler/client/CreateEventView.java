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
	private String eventName;
	private String userName;
	private String park;
	private String eventType;
	private String date;
	private String startTime;
	private String endTime;
	//TODO: fields
	
	public CreateEventView() {
		//TODO: Default Constructor
	}
	
	public CreateEventView(String parkID) {
		park = parkID;
	}
	
	public VerticalPanel createPage() {
		//TODO: call data service for park data
		
		//Panel to hold Create Event Components
		VerticalPanel createEventPanel = new VerticalPanel();
		//Name boxes
		//TODO: Add change handlers to take values
		TextBox eventNameBox = new TextBox();
		TextBox userNameBox = new TextBox();
		
		//Date Selector for Event
		DatePicker eventDatePicker = new DatePicker();
		final Label eventDatePickerLabel = new Label();
		//Sets the label when a date is selected
		//TODO: set the field when a date is selected
		eventDatePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				Date eventDate = event.getValue();
				String eventDateString = DateTimeFormat.getMediumDateFormat().format(eventDate);
				eventDatePickerLabel.setText(eventDateString);
			}
		});
		//Default Value for date
		eventDatePicker.setValue(new Date(), true);
		
		//Selectors for event time
		//TODO: add change handlers, extract loop into generic time list helper
		ListBox startTimeList = new ListBox();
		for (int i=0; i<=23; i++ ) {
			startTimeList.addItem(i+":00");
		}
		startTimeList.setVisibleItemCount(1);
		
		ListBox endTimeList = new ListBox();
		for (int i=0; i<=23; i++ ) {
			endTimeList.addItem(i+":00");
		}
		endTimeList.setVisibleItemCount(1);
		
		ListBox facilitiesListBox = new ListBox();
		facilitiesListBox.addItem("Default Facility");
		facilitiesListBox.setVisibleItemCount(1);
		ListBox parksListBox = new ListBox();
		//get parks for parks list
		getParks(parksListBox);
		parksListBox.setVisibleItemCount(1);
		
		
		Button submitButton = new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				//TODO: Implement verifier call, submit call, and route to event view tab
				Window.alert("Submitted!");
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
		createEventPanel.add(facilitiesListBox);
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
}
