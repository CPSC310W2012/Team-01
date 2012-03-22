package com.cs310.ubc.meetupscheduler.client.activity;

import com.cs310.ubc.meetupscheduler.client.CreateEventView;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;
import com.cs310.ubc.meetupscheduler.client.places.CreateEventPlace;
import com.cs310.ubc.meetupscheduler.client.placeutil.PlaceNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class CreateEventActivity extends AbstractActivity implements PlaceNavigator {

    private String name;

    public CreateEventActivity(CreateEventPlace place) {
        this.name = place.getName();
    }
	
	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		CreateEventView cv = new CreateEventView();
		cv.setName(name);
		panel.setWidget(cv.asWidget());
	}
	
    /**
     * Ask user before stopping this activity TODO: needed?
     */
    @Override
    public String mayStop() {
        return "Please hold on. This page is stopping.";
    }
}
