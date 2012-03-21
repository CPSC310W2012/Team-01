package com.cs310.ubc.meetupscheduler.client;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ AdminPlace.Tokenizer.class, EventPlace.Tokenizer.class, GlobalPlace.Tokenizer.class, CreateEventPlace.Tokenizer.class})
public interface MSPlaceMapper extends PlaceHistoryMapper {

}
