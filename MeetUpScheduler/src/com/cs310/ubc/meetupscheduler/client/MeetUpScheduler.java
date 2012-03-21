package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;


import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MeetUpScheduler implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 * TODO: not implemented
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	  
	  private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label("Please sign in to your Google Account to access the Vancouver Meetup Scheduler application.");
	  private Anchor signInLink = new Anchor("Sign In");
	  private Anchor signOutLink = new Anchor("Sign Out");
	  
	  private Place defaultPlace = new GlobalPlace("Home");
	  private Place createEventPlace;
	  private Place eventPlace;
	  private Place adminPlace;
	  private SimplePanel appWidget = new SimplePanel();
	  //private Anchor createEvent = new Anchor("Create Event");
	  private Button createEventButton = new Button("Create Event");
	  private Button eventButton = new Button("Event");
	  private Button adminButton = new Button("Admin");
	  private Button homeButton = new Button("Home");
	  
	  public MeetUpScheduler() {
		  //instantiate data class TODO needed?
		 new SharedData(); 
	  }
	  

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    // Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  //Do something?
	      }
	
	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	loadMeetupScheduler();
	        } else {
	        	loadLogin();	
	        }
	      }
	    });
	}
	
	private void loadLogin() {
	    // Assemble login panel.
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get().add(loginPanel);
	}

	public void loadMeetupScheduler() {
		RootPanel.get().remove(loginPanel);
		homeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//already intialized
				if (SharedData.placeController.getWhere().equals(Place.NOWHERE)) {
					return;
				}
				else {
					SharedData.getPlaceController().goTo(defaultPlace);
				}
					
			}
		});
		createEventButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (createEventPlace == null) {
					createEventPlace = new CreateEventPlace("Create_Event");
				}
				else if (SharedData.placeController.getWhere().equals(Place.NOWHERE)) {
					return;
				}
				else {
					//TODO needs an update?
				}
				SharedData.getPlaceController().goTo(createEventPlace);
					
			}
		});
		//TODO: Move
		eventButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (eventPlace == null) {
					eventPlace = new EventPlace("Event");
				}
				else {
					//TODO needs an update?
				}
				SharedData.getPlaceController().goTo(eventPlace);
					
			}
		});
		adminButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (adminPlace == null) {
					adminPlace = new AdminPlace("Admin");
				}
				else if (SharedData.placeController.getWhere().equals(Place.NOWHERE)) {
					return;
				}
				else {
					//TODO needs an update?
				}
				SharedData.getPlaceController().goTo(adminPlace);
					
			}
		});
		RootPanel.get().add(homeButton);
		RootPanel.get().add(createEventButton);
		RootPanel.get().add(eventButton);
		RootPanel.get().add(adminButton);
			
		
        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new MSActivityMapper();
        ActivityManager activityManager = new ActivityManager(activityMapper, SharedData.getEventBus());
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        MSPlaceMapper historyMapper= GWT.create(MSPlaceMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
  
        historyHandler.register(SharedData.getPlaceController(), SharedData.eventBus, defaultPlace);

        RootPanel.get().add(appWidget);
        // Goes to the place represented on URL else default place
        historyHandler.handleCurrentHistory();
	    // Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    RootPanel.get().add(signOutLink);
	}

	
	public static final class SharedData {
		
		  private static final EventBus eventBus = new SimpleEventBus();

		 private static final PlaceController placeController = new PlaceController(eventBus);
		  
		 private SharedData() {			 
		 }
		  	
		public static PlaceController getPlaceController() {
				return placeController;
			}
		
		public static EventBus getEventBus() {
			return eventBus;
		}

	}

	
}
