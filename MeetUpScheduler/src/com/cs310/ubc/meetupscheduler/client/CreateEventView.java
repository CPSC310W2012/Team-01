package com.cs310.ubc.meetupscheduler.client;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CreateEventView {

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
		parksListBox.addItem("Default Park");
		parksListBox.setVisibleItemCount(1);
		//TODO: parksList.add array of parks names as strings
		
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
}
