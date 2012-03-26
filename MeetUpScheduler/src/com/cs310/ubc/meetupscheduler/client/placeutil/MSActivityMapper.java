package com.cs310.ubc.meetupscheduler.client.placeutil;

import com.cs310.ubc.meetupscheduler.client.activity.AdminActivity;
import com.cs310.ubc.meetupscheduler.client.activity.CreateEventActivity;
import com.cs310.ubc.meetupscheduler.client.activity.EventActivity;
import com.cs310.ubc.meetupscheduler.client.activity.GlobalActivity;
import com.cs310.ubc.meetupscheduler.client.places.AdminPlace;
import com.cs310.ubc.meetupscheduler.client.places.CreateEventPlace;
import com.cs310.ubc.meetupscheduler.client.places.EventPlace;
import com.cs310.ubc.meetupscheduler.client.places.GlobalPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * maps the places to the activities
 * @author Caroline
 *
 */
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
        	EventPlace eventPlace = (EventPlace)place;
        	Integer id = eventPlace.getID();
        	if (id == null) {
        		return new EventActivity(eventPlace);
        	}
        	else
        		return new EventActivity(eventPlace, id);
        }
        else if (place instanceof GlobalPlace) {
        	return new GlobalActivity((GlobalPlace) place);
        }
        return null;
    }
	

}
