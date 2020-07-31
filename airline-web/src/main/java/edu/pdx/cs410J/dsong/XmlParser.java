package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XmlParser implements AirlineParser<Airline> {

    private String filePath;
    private String airlineNameInput = null;
    private String xmlString;

    /**
     * Constructor for <Code>XmlParser</Code>
     *
     * @param source XML source to parse from
     * @param airlineName Airline name specified by the user in the command line
     */
    public XmlParser(String source, String airlineName, boolean parseFromFile) {

        if (parseFromFile) {
            this.filePath = source;
        } else {
            this.xmlString = source;
        }
        this.airlineNameInput = airlineName;
    }


    /**
     * Uses DOM Parser to parse DOM elements from the XML file.
     * Uses the parsed information to create a new Airline that'll get passed to <Code>XmlDumper</Code>
     *
     * @return parsedAirline Airline information parsed from the XMl file
     * @throws ParserException If XML file is not parsable. i.e. Malformed XML file
     */

    @Override
    public Airline parse() throws ParserException {
        AirlineXmlHelper helper = new AirlineXmlHelper();
        Document doc = null;
        File file = null;
        Airline parsedAirline = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(helper);
            builder.setErrorHandler(helper);

            if (this.filePath != null) {
                file = new File(this.filePath);
                if (!file.exists()) {
                    return null;
                }
                doc = builder.parse(file);
            } else {
                InputSource inputSrc = new InputSource();
                inputSrc.setCharacterStream(new StringReader(this.xmlString));
                doc = builder.parse(inputSrc);
            }

        } catch (SAXException e) {
            throw new ParserException("Error parsing xml file: Malformed xml file");
        } catch (IOException e) {
            throw new ParserException("XMl parser IO error: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new ParserException("XMl parser config error: " + e.getMessage());
        }

        // This list is always gonna have length = 1
        NodeList airlineList = doc.getElementsByTagName("airline");
        NodeList flightList = doc.getElementsByTagName("flight");
        NodeList departList = doc.getElementsByTagName("depart");
        NodeList arrivalList = doc.getElementsByTagName("arrive");

        Node airline = airlineList.item(0);
        if(airline.getNodeType() == Node.ELEMENT_NODE) {
            Element airlineEle = (Element) airline;
            String airlineName = airlineEle.getElementsByTagName("name").item(0).getTextContent();

            if (!airlineName.equals(this.airlineNameInput)) {
                throw new ParserException("Error: Airline name given does not match the argument in the xml file");
            }

            parsedAirline = new Airline(airlineName);

            for (int i = 0; i < flightList.getLength(); i++) {
                Node flight = flightList.item(i);

                if (flight.getNodeType() == Node.ELEMENT_NODE) {
                    Element flightEle = (Element) flight;

                    // Get Number, Src
                    String fNumber = flightEle.getElementsByTagName("number").item(0).getTextContent();
                    String src = flightEle.getElementsByTagName("src").item(0).getTextContent();

                    // Traverse depart node
                    Element departEle = (Element) departList.item(i);

                    // Get depart date and time attributes
                    var departDate = departEle.getElementsByTagName("date").item(0);
                    var dateAttrMap = departDate.getAttributes();
                    var departTime = departEle.getElementsByTagName("time").item(0);
                    var timeAttrMap = departTime.getAttributes();

                    // Get destination airport
                    String dest = flightEle.getElementsByTagName("dest").item(0).getTextContent();

                    // Traverse arrive node
                    Element arrivalEle = (Element) arrivalList.item(i);

                    // Get arrival date and time attributes
                    var arrivalDate = arrivalEle.getElementsByTagName("date").item(0);
                    var dateAttrMap_arrival = arrivalDate.getAttributes();
                    var arrivalTime = arrivalEle.getElementsByTagName("time").item(0);
                    var timeAttrMap_arrival = arrivalTime.getAttributes();

                    String departTimeString = timeAttrMap.item(0).getNodeValue() + ":" + timeAttrMap.item(1).getNodeValue();
                    String arriveTimeString = timeAttrMap_arrival.item(0).getNodeValue() + ":" + timeAttrMap_arrival.item(1).getNodeValue();

                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

                    Date departTimeObj = null;
                    Date arriveTimeObj = null;

                    try {
                        departTimeObj = sdf.parse(departTimeString);
                        arriveTimeObj = sdf.parse(arriveTimeString);
                    } catch (ParseException e) {
                        throw new ParserException(e.getMessage());
                    }
                    String formattedDepartTime = new SimpleDateFormat("hh:mm a").format(departTimeObj);
                    String formattedArriveTime = new SimpleDateFormat("hh:mm a").format(arriveTimeObj);

                    // Format string time
                    int departMonth = Integer.parseInt(dateAttrMap.item(1).getNodeValue());
                    departMonth += 1;
                    int arriveMonth = Integer.parseInt(dateAttrMap_arrival.item(1).getNodeValue());
                    arriveMonth += 1;

                    String formattedDeparture = departMonth + "/" + dateAttrMap.item(0).getNodeValue() + "/"
                            + dateAttrMap.item(2).getNodeValue() + " " + formattedDepartTime;
                    String formattedArrival = arriveMonth + "/" + dateAttrMap_arrival.item(0).getNodeValue() + "/"
                            + dateAttrMap_arrival.item(2).getNodeValue() + " " + formattedArriveTime;

                    Flight newFlight = new Flight(fNumber, src, formattedDeparture, dest, formattedArrival);
                    parsedAirline.addFlight(newFlight);
                }
            }
        }



        return parsedAirline;
    }
}
