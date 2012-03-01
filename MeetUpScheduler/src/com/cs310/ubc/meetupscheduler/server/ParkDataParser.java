package com.cs310.ubc.meetupscheduler.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.cs310.ubc.meetupscheduler.server.Advisory.AdvisoryField;
import com.cs310.ubc.meetupscheduler.server.Facility.FacilityField;
import com.cs310.ubc.meetupscheduler.server.Park.ParkField;
import com.cs310.ubc.meetupscheduler.server.Washroom.WashroomField;

import com.cs310.ubc.meetupscheduler.client.ServerException;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A class to parse uploaded XML data into persistent JDOs.
<<<<<<< HEAD
 * 
=======
>>>>>>> f1f4c29b2aaab0cf1f67029a36e7d573e0b1e793
 */
public class ParkDataParser {

	private Document doc;
	private ArrayList<DataObject> createdObjects;
	private ServletOutputStream out;

	/**
	 * Sets the output stream to display results
	 * 
	 * @param res
	 *            the response object
	 * @throws ServerException
	 */
	public ParkDataParser(HttpServletResponse res) throws ServerException {
		try {
			out = res.getOutputStream();
		} catch (IOException e) {
			throw new ServerException(e.getMessage(), e.getStackTrace());
		}
	}

	/**
	 * Parses an XML file containing parks data and saves objects contained in
	 * the file in the application database.
	 * 
	 * @param xmlFile
	 *            The XML file to parse.
	 * @throws ServerException
	 */
	public void parseXML(InputStream xmlFile) throws ServerException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile);

			if (doc == null) {
				throw new ServerException("Invalid XML file");
			}
			NodeList elementList = doc.getElementsByTagName("Park");
			if (elementList == null) {
				throw new ServerException("Invalid XML file");
			}

			// Remove all advisory, washroom, and facility data.
			ArrayList<Class<? extends DataObject>> delClasses = getClassesToDelete();
			PersistenceManager pm = getPersistenceManager();
			try {
				for (Class<? extends DataObject> delClass : delClasses) {
					Query q = pm.newQuery(delClass);
					@SuppressWarnings("unchecked")
					List<DataObject> objects = (List<DataObject>) q.execute();
					pm.deletePersistentAll(objects);
					out.println(delClass.toString()
							+ " deleted from the data store.");
				}
			} finally {
				pm.close();
			}
			// List to hold created objects
			createdObjects = new ArrayList<DataObject>();

			// Create objects from XML elements.
			boolean areNoParks = true;
			for (int i = 0; i < elementList.getLength(); i++) {
				Element element = (Element) elementList.item(i);
				createPark(element);
				areNoParks = false;
			}
			if (areNoParks) {
				throw new ServerException("XML file contains no park data");
			}

			// Batch persist objects
			pm = getPersistenceManager();
			try {
				pm.makePersistentAll(createdObjects);
			} finally {
				pm.close();
			}

		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e.getStackTrace());
		}
	}

	/**
	 * Creates a park from an XML element and appends it to the created objects
	 * list. Calls functions to create objects found inside the park.
	 * 
	 * @param element
	 *            The XML element containing the park information.
	 * @throws ServerException
	 * @throws IOException
	 */
	private void createPark(Element element) throws ServerException {
		Map<String, String> parkDataMap = new HashMap<String, String>();
		try {
			String ID = element.getAttribute("ID");
			if (ID == null || ID.isEmpty()) {
				out.println("Park element with Invalid XML: null ID encountered. Park not recorded");
				return;
			}
			parkDataMap.put(ParkField.ID.toString(), ID);

			NodeList parkElements = element.getElementsByTagName("*");
			String specialFeatures = "";

			if (parkElements != null) {
				for (int i = 0; i < parkElements.getLength(); i++) {
					Element parkElement = (Element) parkElements.item(i);
					String elementName = parkElement.getNodeName();

					if (elementName == null) {
						throw new ServerException("Invalid XML: null element name");
						
					} else if (elementName.equals("Name")) {
						parkDataMap.put(ParkField.NAME.toString(), parkElement.getTextContent());
					}
					/**
					 * Do nothing? else if (elementName.equals("Official")) {
					 * parkDataMap.put("Official", new
					 * Integer(parkElement.getTextContent())); }
					 **/
					else if (elementName.equals("StreetNumber")) {
						parkDataMap.put(ParkField.STRTNUM.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("StreetName")) {
						parkDataMap.put(ParkField.STRTNAME.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("EWStreet")) {
						parkDataMap.put(ParkField.EWST.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("NSStreet")) {
						parkDataMap.put(ParkField.NSST.toString(), parkElement.getTextContent());
					}
					// LatLng object from the maps API can be created directly
					// from this string
					else if (elementName.equals("GoogleMapDest")) {
						parkDataMap.put(ParkField.MAPSTR.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("Hectare")) {
						parkDataMap.put(ParkField.HECT.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("NeighbourhoodName")) {
						parkDataMap.put(ParkField.NNAME.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("NeighbourhoodURL")) {
						parkDataMap.put(ParkField.NURL.toString(), parkElement.getTextContent());
					
					} else if (elementName.equals("Advisories") && parkElement.hasChildNodes()) {
						parkDataMap.put(ParkField.HASADVS.toString(), "Y");
					
					} else if (elementName.equals("Advisories") && !parkElement.hasChildNodes()) {
						parkDataMap.put(ParkField.HASADVS.toString(), "N");
					
					} else if (elementName.equals("Advisory")) { 
						createAdvisory(parkElement, ID);
					} 
					else if (elementName.equals("Facilities") && parkElement.hasChildNodes()) {
						parkDataMap.put(ParkField.HASFAC.toString(), "Y");
					
					} else if (elementName.equals("Facilities") && !parkElement.hasChildNodes()) {
						parkDataMap.put(ParkField.HASFAC.toString(), "N");
					
					} else if (elementName.equals("SpecialFeature")) {
						if (specialFeatures.isEmpty())
							specialFeatures = parkElement.getTextContent();
						else
							specialFeatures += (", " + parkElement
									.getTextContent());
					
					} else if (elementName.equals("Facility")) {
						createFacility(parkElement, ID);
					
					} else if (elementName.equals("Washroom") && parkElement.hasChildNodes()) {
						createWashroom(parkElement, ID);
					}
				}
				parkDataMap.put(ParkField.SPECIALFEAT.toString(), specialFeatures);
			}

			createdObjects.add(new Park(parkDataMap));
			out.println("Park ID: " + ID + " created/updated");
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e.getStackTrace());
		}

	}

	/**
	 * Creates a washroom for a park from an XML element and appends it to the
	 * created objects list.
	 * 
	 * @param washroomElement
	 *            The XML element containing the washroom information.
	 * @param pID
	 *            The ID of the park containing the washroom
	 * @throws ServerException
	 */
	private void createWashroom(Element washroomElement, String pID)
			throws ServerException {
		Map<String, String> washroomDataMap = new HashMap<String, String>();
		washroomDataMap.put(WashroomField.PID.toString(), pID);

		NodeList children = washroomElement.getElementsByTagName("*");
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Element wshElement = (Element) children.item(i);
				String elementName = wshElement.getNodeName();

				if (elementName == null) {
					throw new ServerException(
							"Invalid XML: null washroom field for park " + pID);
				} else if (elementName.equals("Location")) {
					washroomDataMap.put(WashroomField.LOC.toString(),wshElement.getTextContent());
					
				} else if (elementName.equals("Notes")) {
					washroomDataMap.put(WashroomField.NOTES.toString(), wshElement.getTextContent());
					
				} else if (elementName.equals("SummerHours")) {
					washroomDataMap.put(WashroomField.SUMHR.toString(), wshElement.getTextContent());
					
				} else if (elementName.equals("WinterHours")) {
					washroomDataMap.put(WashroomField.WINHR.toString(),wshElement.getTextContent());
				}
			}
			try {
				createdObjects.add(new Washroom(washroomDataMap));
				out.println("Washroom with PID: " + pID + " created");
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), e.getStackTrace());
			}
		}
	}

	/**
	 * Creates an advisory for a park from an XML element and appends it to the
	 * created objects list.
	 * 
	 * @param advisoryElement
	 *            The XML element containing information about the advisory
	 * @param iD
	 *            The ID of the park containing the advisory.
	 * @throws ServerException
	 */
	private void createAdvisory(Element advisoryElement, String iD) throws ServerException {
		Map<String, String> advisoryDataMap = new HashMap<String, String>();
		advisoryDataMap.put(AdvisoryField.PID.toString(), iD);

		NodeList children = advisoryElement.getElementsByTagName("*");
		try {
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Element advElement = (Element) children.item(i);
				String elementName = advElement.getNodeName();

				if (elementName == null) {
					out.println("Invalid XML: null advisory field for park " + iD + "advisory not created");
					return;
					
				} else if (elementName.equals("DateLast")) {
					advisoryDataMap.put(AdvisoryField.DATE.toString(), advElement.getTextContent());
					
				} else if (elementName.equals("AdvisoryText")) {
					advisoryDataMap.put(AdvisoryField.TEXT.toString(), advElement.getTextContent());
					
				} else if (elementName.equals("URL")) {
					advisoryDataMap.put(AdvisoryField.URL.toString(), advElement.getTextContent());
					
				}
			}
				createdObjects.add(new Advisory(advisoryDataMap));
				out.println("Advisory with PID: " + iD + " created");
		}
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), e.getStackTrace());
			}
		
	}

	/**
	 * Creates a facility for a park from an XML element and appends it to the
	 * created objects list.
	 * 
	 * @param facilityElement
	 *            The XML element containing facility information
	 * @param iD
	 *            The ID of the park containing the facility
	 * @throws ServerException
	 */
	private void createFacility(Element facilityElement, String iD)
			throws ServerException {
		Map<String, String> facilityDataMap = new HashMap<String, String>();
		facilityDataMap.put(FacilityField.PID.toString(), iD);

		NodeList children = facilityElement.getElementsByTagName("*");
		
		try {
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Element facElement = (Element) children.item(i);
				String elementName = facElement.getNodeName();

				if (elementName == null) {
					out.println("Invalid XML: null facility field for park " + iD + "facility not created");
					return;
				} else if (elementName.equals("FacilityCount")) {
					facilityDataMap.put(FacilityField.COUNT.toString(), facElement.getTextContent());
					
				} else if (elementName.equals("FacilityType")) {
					facilityDataMap.put(FacilityField.TYPE.toString(), facElement.getTextContent());
					
				} else if (elementName.equals("FacilityURL")) {
					facilityDataMap.put(FacilityField.URL.toString(), facElement.getTextContent());
				}
			}
				createdObjects.add(new Facility(facilityDataMap));
				out.println("Facility with PID: " + iD + " created");
		}
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), e.getStackTrace());
			}

	}

	/**
	 * Helper method to get a persistence manager.
	 * 
	 * @return A persistence manager.
	 */
	private PersistenceManager getPersistenceManager() {
		return PersistenceManagerSingleton.getInstance();
	}

	/**
	 * Helper method that returns a list of classes that should be wiped on file
	 * upload.
	 * 
	 * @return A list of classes that should be wiped.
	 */
	private ArrayList<Class<? extends DataObject>> getClassesToDelete() {
		ArrayList<Class<? extends DataObject>> delClasses = new ArrayList<Class<? extends DataObject>>();
		delClasses.add(Facility.class);
		delClasses.add(Washroom.class);
		delClasses.add(Advisory.class);

		return delClasses;
	}
}
