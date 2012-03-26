package com.cs310.ubc.meetupscheduler.client.activity;

import com.cs310.ubc.meetupscheduler.client.EventView;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;
import com.cs310.ubc.meetupscheduler.client.places.CreateEventPlace;
import com.cs310.ubc.meetupscheduler.client.places.EventPlace;
import com.cs310.ubc.meetupscheduler.client.placeutil.PlaceNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * gwt activity associated with event view
 * @author Caroline
 *
 */
public class EventActivity extends AbstractActivity implements PlaceNavigator {

    private String name;
    private Integer id = null;
    private EventView ev;

    /**
     * constructor for event with no id
     * @param place the place for event view with no event id
     */
    public EventActivity(EventPlace place) {
        this.name = place.getName();
    }
    /**
     * constructor for event with id
     * @param place the place for event view 
     * @param id the id of the event to show
     */
    public EventActivity(EventPlace place, int id) {
        this.name = place.getName();
        this.id = id;
    }
    
	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if (id == null) {
			ev = new EventView();
		}
		else {
			ev = new EventView(id);
		}
		ev.setName(name);
		panel.setWidget(ev.asWidget());
	}
	
}
