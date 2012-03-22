package com.cs310.ubc.meetupscheduler.client.placeutil;

import com.google.gwt.place.shared.Place;

/**
 * gwt instantiates an object using this interface that allows navigation between widgets
 * @author Caroline
 *
 */
public interface PlaceNavigator {
	
	  void goTo(Place place);

}
