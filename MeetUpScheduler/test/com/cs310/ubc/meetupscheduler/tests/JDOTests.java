package com.cs310.ubc.meetupscheduler.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cs310.ubc.meetupcheduler.server.Park;

public class JDOTests {
	Park myPark;
	
	@Before
	public void setUp() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Map<String, Object> parkRow;
		parkRow = new HashMap<String, Object>();
		parkRow.put("id", 1L); 
		parkRow.put("name", "Super Park"); 
		parkRow.put("street_number", "123");
		parkRow.put("street_name", "Party St");
		parkRow.put("ew_street", "Party St");
		parkRow.put("ns_street", "Rad Ave");
		parkRow.put("google_map_dest", "123.456, 789.012");
		parkRow.put("hectares", 17.5f);
		parkRow.put("neighbourhood_name", "Party Hood");
		parkRow.put("neighbourhood_url", "www.partyhood.com");
		parkRow.put("advisories", "Y");
		parkRow.put("facilities", "Y");
		parkRow.put("special_features", "Y");
		parkRow.put("washrooms", "N");
		myPark = new Park(parkRow);
	}
	
	@Test
	public void testCreatePark() {
		assert(myPark.getID() == 1);
		assert(myPark.getHectares() == 17.5f);
		assert(myPark.hasFacilities());
	}
}
