package com.cs310.ubc.meetupscheduler.client.places;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class CreateEventPlace extends Place {

	private String name;
	
	public CreateEventPlace(String token)
    {
        super();
		name = token;
    }
	
	public String getName() {
	    return name;
	}
	
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
