package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


public class GlobalPlace extends Place {
    
	private String name;
	
	public GlobalPlace(String token)
    {
        super();
    }
	
	public String getName() {
	    return name;
	}
	
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


