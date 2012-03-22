package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * gwt place associated with global view
 * @author Caroline
 *
 */
public class GlobalPlace extends Place {
    
	private String name;
	
	/**
	 * constructor
	 * @param token the url token
	 */
	public GlobalPlace(String token)
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
	public static class Tokenizer implements PlaceTokenizer<GlobalPlace>
	{
	    @Override
	    public String getToken(GlobalPlace place)
	    {
	        return place.getName();
	    }
	
	    @Override
	    public GlobalPlace getPlace(String token)
	    {
	        return new GlobalPlace(token);
	    }
	}
}


