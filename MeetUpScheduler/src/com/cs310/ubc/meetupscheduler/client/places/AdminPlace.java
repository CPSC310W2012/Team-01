package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * The gwt place for admin view
 * @author Caroline
 *
 */
public class AdminPlace extends Place {

	private String name;
	
	/**
	 * Constructor
	 * @param token url token
	 */
	public AdminPlace(String token)
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
	public static class Tokenizer implements PlaceTokenizer<AdminPlace>
	{
	    @Override
	    public String getToken(AdminPlace place)
	    {
	        return place.getName();
	    }
	
	    @Override
	    public AdminPlace getPlace(String token)
	    {
	        return new AdminPlace(token);
	    }
	}

}
