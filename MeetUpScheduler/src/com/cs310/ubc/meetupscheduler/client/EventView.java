package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EventView extends View{
	/**
	 * These are the UI components of the View
	 */
	private HorizontalPanel panel = new HorizontalPanel();
	private Label eventName = new Label();
	//private mapWidget eventMap = new mapWidget();
	private Button joinButton = new Button();
	private DialogBox joinBox = new DialogBox();
	private TextBox joinName = new TextBox();
	private ListBox attendees = new ListBox();
	private Label eventLoc = new Label();
	private Label time = new Label();
	private ListBox notes = new ListBox();



//	public Panel createPage(Event event) {
//		//TODO: Figure out how to get Event details from the JDO.
//
//		//set up the eventName label
//		//eventName.setText(event.NAME.toString()); // TODO: get proper enum settings
//
//
//		//set up the joinButton
//		joinButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				joinPopup.show();
//			}
//		});
//		joinPopup.add(joinName);
//		joinPopup.add(submitButton);
//
//		submitButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				//TODO: Add the name in joinName to the Event object
//				joinPopup.hide();
//				//TODO: Refresh the attendee list after adding new attendee.
//			}
//		});
//
//		//TODO: Add all attendiing members 
//	//	attendees.addItem(event.getField()
//		
//		panel.add(eventName);
//		//panel.add(eventMap);
//		panel.add(joinButton);
//		panel.add(attendees);
//		panel.add(attendees);
//		panel.add(eventLoc);
//		panel.add(time);
//		panel.add(notes);
//		return panel;
//	}


	@Override
	public HorizontalPanel createPage() {
		//Initialize Map, needs a key before it can be deployed
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildUI();
			}
		});
		return panel;
	}
	
	public void buildUI(){
		//set up the eventName label
		eventName.setText("This is the event name"); // TODO: get proper enum settings


		//set up the joinButton
		
		joinButton.setText("Join event!");
		joinButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				joinBox.show();
			}
		});
	
		
		//Add the joinBox
		setJoinBox();

		
		
		
		attendees.addItem("Adrian");
		attendees.addItem("Ben");
		attendees.addItem("Caroline");
		attendees.addItem("Connor");
		attendees.addItem("Dave");
		
		
		panel.add(eventName);
		//panel.add(eventMap);
		panel.add(joinButton);
		panel.add(attendees);
		panel.add(eventLoc);
		panel.add(time);
		panel.add(notes);
	}

	protected void setJoinBox() {
		
		//Hide joinBox and set the caption
		joinBox.hide();
		joinBox.ensureDebugId("cwDialogBox");
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
				System.out.println(joinName.getValue());
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
	/** joinButton.onClick - show the joinPopup, add joinName and submitButton
	 * submitButton.onClick - add the contents of joinName to the list of attendees 
	 * make Attendees a JDO, and persistent
	 * Catch exceptions from creating jdo objects, etc.
	 * 
	 */
}
