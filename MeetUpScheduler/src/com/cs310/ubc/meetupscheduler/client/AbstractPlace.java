package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public abstract class AbstractPlace extends Place {

	private String name = "Page";

	public AbstractPlace(String token) {
		this.name = token;
	}
	
	public String getName() {
	    return name;
	}

	
	public static class Tokenizer implements PlaceTokenizer<AbstractPlace>
	{
	    @Override
	    public String getToken(AbstractPlace place)
	    {
	        return place.getName();
	    }
	
	    @Override
	    public AbstractPlace getPlace(String token)
	    {
	        return new GlobalPlace(token);
	    }
	}

}