package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * gwt place for create event view
 * @author Caroline
 *
 */
public class CreateEventPlace extends Place {

	private String name;
	
	/**
	 * constructor
	 * @param token the url token
	 */
	public CreateEventPlace(String token)
    {
        super();
		name = token;
    }
	
	/**
	 * returns the url token
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
	public static class Tokenizer implements PlaceTokenizer<CreateEventPlace>
	{
	    @Override
	    public String getToken(CreateEventPlace place)
	    {
	        return place.getName();
	    }
	
	    @Override
	    public CreateEventPlace getPlace(String token)
	    {
	        return new CreateEventPlace(token);
	    }
	}
}
