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
	private Integer id = null;
	
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
	 * constructor
	 * @param token url token
	 */
	public EventPlace(String token, Integer id)
    {
        super();
		name = token;
		this.id = id;
    }
	
	/**
	 * get the url token
	 * @return the url token
	 */
	public String getName() {
	    return name;
	}
	
	public Integer getID() {
		return id;
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
