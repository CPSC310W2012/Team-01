package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * gwt place associated with event view
 * @author Caroline
 *
 */
public class EventPlace extends Place {

	private String name;
	
	/**
	 * constructor
	 * @param token url token
	 */
	public EventPlace(String token)
    {
        super();
		name = token;
    }
	
	/**
	 * get the url token
	 * @return the url token
	 */
	public String getName() {
	    return name;
	}
	
	/**
	 * tokenizer class for automated history
	 * @author Caroline
	 *
	 */
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
