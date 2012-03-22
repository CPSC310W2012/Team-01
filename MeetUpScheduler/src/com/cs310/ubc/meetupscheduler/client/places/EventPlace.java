package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class EventPlace extends Place {

	private String name;
	
	public EventPlace(String token)
    {
        super();
		name = token;
    }
	
	public String getName() {
	    return name;
	}
	
	public static class Tokenizer implements PlaceTokenizer<EventPlace>
	{
	    @Override
	    public String getToken(EventPlace place)
	    {
	        return place.getName();
	    }
	
	    @Override
	    public EventPlace getPlace(String token)
	    {
	        return new EventPlace(token);
	    }
	}

}
