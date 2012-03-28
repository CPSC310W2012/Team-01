/**
 * 
 */
package com.cs310.ubc.meetupscheduler.server;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.cs310.ubc.meetupscheduler.server.Park;
import com.cs310.ubc.meetupscheduler.server.ParkDataParser;
import com.cs310.ubc.meetupscheduler.server.Advisory.AdvisoryField;
import com.cs310.ubc.meetupscheduler.server.Facility.FacilityField;
import com.cs310.ubc.meetupscheduler.server.Park.ParkField;
import com.cs310.ubc.meetupscheduler.server.Washroom.WashroomField;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;



/**
 * Test data parsing results 
 * @author Caroline
 *
 */
public class DataParseTest {
	static MockHttpServletResponse response;
	
    private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private static List<Map> data;
    private static List<Map> parkData = new ArrayList<Map>();
    private static List<Map> facilityData = new ArrayList<Map>();
    private static List<Map> advisoryData = new ArrayList<Map>();
    private static List<Map> washroomData = new ArrayList<Map>();
    private static boolean isUnclassified = false;


	/**
	 * setup
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		helper.setUp();
		response = new MockHttpServletResponse();
		FileInputStream xml = new FileInputStream("testdata.xml"); 
		
		ParkDataParser parser = new ParkDataParser(response);
		parser.parseXML(xml);
		data = parser.getTestData();
		
		for (Map dataMap : data) {
			if (dataMap.get(ParkField.NAME.toString()) != null) {
				parkData.add(dataMap);
			}
			
			else if (dataMap.get(FacilityField.COUNT.toString()) != null) {
				facilityData.add(dataMap);
			}
			
			else if (dataMap.get(AdvisoryField.TEXT.toString()) != null) {
				advisoryData.add(dataMap);
			}
			
			else if (dataMap.get(WashroomField.LOC.toString()) != null ) {
				washroomData.add(dataMap);
			}
			else 
				isUnclassified = true;
			
		}
	}
	

	@Test
	public void correctAmountTest() {
		//print the results for extra info
		try {
			System.out.println(response.getContentAsString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("http response error");
		}
		
		assertTrue(data.size() == 20);
		assertFalse(isUnclassified);
		assertTrue(parkData.size() == 4);
		assertTrue(facilityData.size() == 13);
		assertTrue(advisoryData.size() == 1);
		assertTrue(washroomData.size() == 2);

	}
	
	@Test
	public void parkDataTest() {
		for (Map park : parkData) {
			if(park.get(Park.ParkField.ID.toString()).equals("2")) {
				assertTrue(park.get(ParkField.NAME.toString()).equals("Carnarvon Park"));
				assertTrue(park.get(ParkField.STRTNUM.toString()).equals("2995"));
				assertTrue(park.get(ParkField.STRTNAME.toString()).equals("W 19th Avenue"));
				assertTrue(park.get(ParkField.EWST.toString()).equals("W 19th Avenue"));
				assertTrue(park.get(ParkField.NSST.toString()).equals("Mackenzie Street"));
				assertTrue(park.get(ParkField.MAPSTR.toString()).equals("49.256555,-123.171406"));
				assertTrue(park.get(ParkField.HECT.toString()).equals("3.79"));
				assertTrue(park.get(ParkField.NNAME.toString()).equals("Arbutus Ridge"));
				assertTrue(park.get(ParkField.NURL.toString()).equals("http://vancouver.ca/community_profiles/arbutus_ridge/index.htm"));
				assertTrue(park.get(ParkField.HASADVS.toString()).equals("N"));
				assertTrue(park.get(ParkField.HASFAC.toString()).equals("Y"));
				assertTrue(park.get(ParkField.SPECIALFEAT.toString()).equals(""));
			
			}
			else if(park.get(Park.ParkField.ID.toString()).equals("3")) {
				assertTrue(park.get(ParkField.NAME.toString()).equals("Prince of Wales Park"));
				assertTrue(park.get(ParkField.STRTNUM.toString()).equals("4780"));
				assertTrue(park.get(ParkField.STRTNAME.toString()).equals("Haggart Street"));
				assertTrue(park.get(ParkField.EWST.toString()).equals("W 32nd Avenue"));
				assertTrue(park.get(ParkField.NSST.toString()).equals("Haggart Street"));
				assertTrue(park.get(ParkField.MAPSTR.toString()).equals("49.244397,-123.156429"));
				assertTrue(park.get(ParkField.HECT.toString()).equals("2.89"));
				assertTrue(park.get(ParkField.NNAME.toString()).equals("Arbutus Ridge"));
				assertTrue(park.get(ParkField.NURL.toString()).equals("http://vancouver.ca/community_profiles/arbutus_ridge/index.htm"));
				assertTrue(park.get(ParkField.HASADVS.toString()).equals("N"));
				assertTrue(park.get(ParkField.HASFAC.toString()).equals("Y"));
				assertTrue(park.get(ParkField.SPECIALFEAT.toString()).equals(""));
			}
			
			else if(park.get(Park.ParkField.ID.toString()).equals("16")) {
				assertTrue(park.get(ParkField.NAME.toString()).equals("David Lam Park"));
				assertTrue(park.get(ParkField.STRTNUM.toString()).equals("1300"));
				assertTrue(park.get(ParkField.STRTNAME.toString()).equals("Pacific Boulevard"));
				assertTrue(park.get(ParkField.EWST.toString()).equals("Drake Street"));
				assertTrue(park.get(ParkField.NSST.toString()).equals("Pacific Boulevard"));
				assertTrue(park.get(ParkField.MAPSTR.toString()).equals("49.272569,-123.124145"));
				assertTrue(park.get(ParkField.HECT.toString()).equals("4.34"));
				assertTrue(park.get(ParkField.NNAME.toString()).equals("Downtown"));
				assertTrue(park.get(ParkField.NURL.toString()).equals("http://vancouver.ca/community_profiles/downtown/index.htm"));
				assertTrue(park.get(ParkField.HASADVS.toString()).equals("Y"));
				assertTrue(park.get(ParkField.HASFAC.toString()).equals("Y"));
				assertTrue(park.get(ParkField.SPECIALFEAT.toString()).equals("Seawall"));
			}
			
			else if(park.get(Park.ParkField.ID.toString()).equals("18")) {
				assertTrue(park.get(ParkField.NAME.toString()).equals("Devonian Harbour Park"));
				assertTrue(park.get(ParkField.STRTNUM.toString()).equals("1929"));
				assertTrue(park.get(ParkField.STRTNAME.toString()).equals("W Georgia Street"));
				assertTrue(park.get(ParkField.EWST.toString()).equals("W Georgia Street"));
				assertTrue(park.get(ParkField.NSST.toString()).equals("Denman Street"));
				assertTrue(park.get(ParkField.MAPSTR.toString()).equals("49.294353,-123.134654"));
				assertTrue(park.get(ParkField.HECT.toString()).equals("4.42"));
				assertTrue(park.get(ParkField.NNAME.toString()).equals("Downtown"));
				assertTrue(park.get(ParkField.NURL.toString()).equals("http://vancouver.ca/community_profiles/downtown/index.htm"));
				assertTrue(park.get(ParkField.HASADVS.toString()).equals("N"));
				assertTrue(park.get(ParkField.HASFAC.toString()).equals("Y"));
				assertTrue(park.get(ParkField.SPECIALFEAT.toString()).equals("Seawall"));
			}
			
			else
				fail("unexpected park ID");
		}
	}
	@Test
	public void parkFacilityTest() {
		int fac2, fac3, fac16, fac18;
		fac2 = fac3 = fac16 = fac18 = 0;
		
		for(Map facility : facilityData) {
			if(facility.get(FacilityField.PID.toString()).equals("2")) {
				fac2++;
			}
			else if(facility.get(FacilityField.PID.toString()).equals("3")) {
				//test empty data fields
				assertTrue(facility.get(FacilityField.URL.toString()).equals(""));
				fac3++;
			}
			else if(facility.get(FacilityField.PID.toString()).equals("16")) {
				fac16++;
			}
			//Test content here because they are all the same and this one is easy to identify
			else if(facility.get(FacilityField.PID.toString()).equals("18")) {
				assertTrue(facility.get(FacilityField.TYPE.toString()).equals("Dogs Off-Leash Areas"));
				assertTrue(facility.get(FacilityField.COUNT.toString()).equals("1"));
				assertTrue(facility.get(FacilityField.URL.toString()).equals("http://vancouver.ca/parks/info/dogparks/index.htm"));
				fac18++;
				
			}
			else
				fail("facility with incorrect ID");
		}	
		assertTrue(fac2 == 6);
		assertTrue(fac3 == 2);
		assertTrue(fac16 == 4);
		assertTrue(fac18 == 1);
		
	}
	
	@Test
	public void parkAdvisoryTest() {
		//because there is only one
		Map advisory = advisoryData.get(0);
		
		assertTrue(advisory.get(AdvisoryField.PID.toString()).equals("16"));
		assertTrue(advisory.get(AdvisoryField.DATE.toString()).equals("2013-05-01 00:00:00"));
		
		//cannot check the whole string because some of the original chars are being converted to html tags by the parsing API
		assertTrue(((String) (advisory.get(AdvisoryField.TEXT.toString()))).contains("Work is currently underway in David Lam Park on a new underground transmission"));
		assertTrue(advisory.get(AdvisoryField.URL.toString()).equals(""));
	}
	
	@Test
	public void parkWashroomTest() {
		for (Map washroom : washroomData) {
			if (washroom.get(WashroomField.PID.toString()).equals("2")) {
				assertTrue(washroom.get(WashroomField.LOC.toString()).equals("West side, field house"));
				assertTrue(washroom.get(WashroomField.NOTES.toString()).equals(""));
				assertTrue(washroom.get(WashroomField.SUMHR.toString()).equals("Dawn to Dusk"));
				assertTrue(washroom.get(WashroomField.WINHR.toString()).equals("Dawn to Dusk"));
			}
			else if (washroom.get(WashroomField.PID.toString()).equals("16")) {
				assertTrue(washroom.get(WashroomField.LOC.toString()).equals("West side"));
				assertTrue(washroom.get(WashroomField.NOTES.toString()).equals("test"));
				assertTrue(washroom.get(WashroomField.SUMHR.toString()).equals("10:00 am - Dusk"));
				assertTrue(washroom.get(WashroomField.WINHR.toString()).equals("10:00 am - Dusk"));
			}
			else 
				fail("incorrect washroom ID");
		}
	}


}
