package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class MSActivityMapper implements ActivityMapper {
    
	
	public MSActivityMapper() {
        super();
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof AdminPlace)
            return new AdminActivity((AdminPlace) place);
        else if (place instanceof CreateEventPlace)
        	return new CreateEventActivity((CreateEventPlace) place);
        else if (place instanceof EventPlace) {
        	return new EventActivity((EventPlace) place);
        }
        else if (place instanceof GlobalPlace) {
        	return new GlobalActivity((GlobalPlace) place);
        }
        return null;
    }
	

}
