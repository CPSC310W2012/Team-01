package com.cs310.ubc.meetupscheduler.client.activity;

import com.cs310.ubc.meetupscheduler.client.AdminView;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler;
import com.cs310.ubc.meetupscheduler.client.MeetUpScheduler.SharedData;
import com.cs310.ubc.meetupscheduler.client.places.AdminPlace;
import com.cs310.ubc.meetupscheduler.client.placeutil.PlaceNavigator;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
/**
 * The GWT activity that wraps the admin view
 * @author Caroline
 *
 */
public class AdminActivity extends AbstractActivity implements PlaceNavigator {

    private String name = "Upload";
    private AdminView av;
    
    /**
     * constructor
     * The gwt place/tokenizer for admin view
     * @param place
     */
    public AdminActivity(AdminPlace place) {
        this.name = place.getName();
    }
	

	@Override
	public void goTo(Place place) {
		MeetUpScheduler.SharedData.getPlaceController().goTo(place);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if (av == null) {
		 av = new AdminView();
		}
		av.setName(name);
		panel.setWidget(av.asWidget());
	}
		
}
