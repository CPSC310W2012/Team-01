package com.cs310.ubc.meetupscheduler.client.activity;

import com.cs310.ubc.meetupscheduler.client.GlobalView;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;
import com.cs310.ubc.meetupscheduler.client.places.GlobalPlace;
import com.cs310.ubc.meetupscheduler.client.placeutil.PlaceNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * gwt activity associated with global activity
 * @author Caroline
 *
 */
public class GlobalActivity extends AbstractActivity implements PlaceNavigator {

    private String name;
    private GlobalView gv;

    public GlobalActivity(GlobalPlace place) {
        this.name = place.getName();
    }
	
	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if (gv == null) {
			gv = new GlobalView();
		}
		gv.setName(name);
		panel.setWidget(gv.asWidget());
	}
	

}
