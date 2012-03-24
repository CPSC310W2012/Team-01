package com.cs310.ubc.meetupscheduler.client.placeutil;

import com.cs310.ubc.meetupscheduler.client.places.AdminPlace;
import com.cs310.ubc.meetupscheduler.client.places.CreateEventPlace;
import com.cs310.ubc.meetupscheduler.client.places.EventPlace;
import com.cs310.ubc.meetupscheduler.client.places.GlobalPlace;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * gwt instantiates an object using this interface that keeps track of the history via the tokenizer classes
 * @author Caroline
 *
 */
@WithTokenizers({ AdminPlace.Tokenizer.class, EventPlace.Tokenizer.class, GlobalPlace.Tokenizer.class, CreateEventPlace.Tokenizer.class})
public interface MSPlaceMapper extends PlaceHistoryMapper {

}
