package com.cs310.ubc.meetupcheduler.server;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs310.ubc.meetupcheduler.server.Park.ParkFields;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ParkDataParser {
	
	private Document doc;
	
	public void parseXML(InputStream xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
			
			if (doc == null) {
				//TODO: CAROLINE error handling
				return;
			}
			NodeList elementList = doc.getElementsByTagName("*");
			if (elementList == null) {
				//TODO: CAROLINE error handling
				return;
			}
			
			boolean areNoParks = true;
			for (int i = 0; i < elementList.getLength(); i++) {
				Element element = (Element) elementList.item(i);
				if (element.getNodeName().equals("Park")) {
					createPark(element);
					areNoParks = false;
				}
			}
			if (areNoParks) {
				//TODO: CAROLINE error handling
			}

	} catch (Exception e) {
		//TODO: CAROLINE error handling
		System.err.println(e);	
	}
	}
	
	//Creates a Park Object. Only Park ID is guaranteed to exist.
	private void createPark(Element element) {
		Map<String, Object> parkDataMap = new HashMap<String, Object>();
		
		String ID  = element.getAttribute("ID");
		if (ID == null || ID.isEmpty()) {
			//TODO: CAROLINE error handling
			return;
		}
		parkDataMap.put(ParkFields.ID.toString(), new Long(ID));
		
		NodeList parkElements = element.getElementsByTagName("*");
		
		if (parkElements != null ) {
			for (int i = 0; i < parkElements.getLength(); i++) {
				Element parkElement = (Element) parkElements.item(i);
				String elementName = parkElement.getNodeName();
				
				if (elementName == null) {
					//TODO: Caroline error handling
				}
				else if (elementName.equals("Name")) {
					parkDataMap.put(ParkFields.NAME.toString(), parkElement.getTextContent());
				}
				/** Do nothing?
				else if (elementName.equals("Official")) {
					parkDataMap.put("Official", new Integer(parkElement.getTextContent()));
				}**/
				else if (elementName.equals("StreetNumber")) {
					parkDataMap.put(ParkFields.STRTNUM.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("StreetName")) {
					parkDataMap.put(ParkFields.STRTNUM.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("EWStreet")) {
					parkDataMap.put(ParkFields.EWST.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("NSStreet")) {
					parkDataMap.put(ParkFields.NSST.toString(), parkElement.getTextContent());
				}
				//LatLng object from the maps API can be created directly from this string
				else if (elementName.equals("GoogleMapDest")) {
					parkDataMap.put(ParkFields.MAPSTR.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("Hectare")) {
					parkDataMap.put(ParkFields.HECT.toString(), new Float(parkElement.getTextContent()));
				}
				else if (elementName.equals("NeighbourhoodName")) {
					parkDataMap.put(ParkFields.NNAME.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("NeighbourhoodURL")) {
					parkDataMap.put(ParkFields.NURL.toString(), parkElement.getTextContent());
				}
				else if (elementName.equals("Advisory")) {
					parkDataMap.put(ParkFields.HASADVS.toString(), "Y");
					createAdvisory(parkElement, ID);
				}
				else if (elementName.equals("Facilities")) {
					parkDataMap.put(ParkFields.HASFAC.toString(), "Y");
					createFacility(parkElement, ID);
				}
			}
			try {
				new Park(parkDataMap);
				System.out.println("new park created ID:" + ID.toString());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	}

	private void createAdvisory(Element advisoryElement, String pID) {
		Map<String, Object> advisoryDataMap = new HashMap<String, Object>();
		
		NodeList children = advisoryElement.getElementsByTagName("*");
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Element advElement = (Element) children.item(i);
				String elementName = advElement.getNodeName();
				
				if (elementName == null) {
					//TODO: Caroline error handling
				}
				
			}
		}
		
	}

	private void createFacility(Element facilityElement, String pID) {
		Map<String, Object> facilityDataMap = new HashMap<String, Object>();
		
		NodeList children = facilityElement.getElementsByTagName("*");
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Element facElement = (Element) children.item(i);
				String elementName = facElement.getNodeName();
				
				if (elementName == null) {
					//TODO: Caroline error handling
				}
				
			}
			
		}
		
	}


}
