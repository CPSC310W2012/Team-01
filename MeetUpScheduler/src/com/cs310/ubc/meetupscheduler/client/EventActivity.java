package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class EventActivity extends AbstractActivity implements PlaceNavigator {

    private String name;

    public EventActivity(AbstractPlace place) {
        this.name = place.getName();
    }
	
	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		EventView ev = new EventView();
		ev.setName(name);
		panel.setWidget(ev.asWidget());
	}
	
    /**
     * Ask user before stopping this activity TODO: needed?
     */
    @Override
    public String mayStop() {
        return "Please hold on. This page is stopping.";
    }
}
