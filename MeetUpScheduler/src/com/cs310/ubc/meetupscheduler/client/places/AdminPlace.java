package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class AdminPlace extends Place {
	
	private String name;
	
	public AdminPlace(String token)
    {
        super();
    }
	
	public String getName() {
	    return name;
	}
	
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
