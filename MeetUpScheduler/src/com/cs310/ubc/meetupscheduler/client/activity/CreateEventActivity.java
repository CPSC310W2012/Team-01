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

/**
 * the gwt activity associated with create event view
 * @author Caroline
 *
 */
public class CreateEventActivity extends AbstractActivity implements PlaceNavigator {

    private String name;
    private CreateEventView cv;

    /**
     * constructor
     * @param place the place associated with create event place
     */
    public CreateEventActivity(CreateEventPlace place) {
        this.name = place.getName();
    }

	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if (cv == null) {
			cv = new CreateEventView();
		}
		cv.setName(name);
		panel.setWidget(cv.asWidget());
	}
	
}
