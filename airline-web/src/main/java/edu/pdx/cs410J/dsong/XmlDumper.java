package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.AirlineDumper;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

public class XmlDumper implements AirlineDumper<Airline> {

  private String filePath;
  private static PrintStream err = System.err;
  private Writer writer;


  /**
   * <Code>XmlDumper</Code> Constructor
   * @param path File path where the xml file should be written to
   */
  public XmlDumper(String path){
    this.filePath = path;
  }

  /**
   * <Code>XmlDumper</Code> Constructor
   * @param writer PrintWriter object to write to
   */
  public XmlDumper(Writer writer) {
    this.writer = writer;
  }

  /**
   * Creates a DOM builder, then uses it to construct a DOM tree from sctratch
   * Outputs to the filepath specified in member variable
   * @param airline Airline it should use to build the DOM tree
   */
  @Override
  public void dump(Airline airline) throws IOException {

    var flightList = airline.getSortedFlightsAsList();

    // Create empty Document
    Document doc = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      factory.setValidating(true);

      DocumentBuilder builder = factory.newDocumentBuilder();

      DOMImplementation dom = builder.getDOMImplementation();

      DocumentType docType = dom.createDocumentType("airline",
              AirlineXmlHelper.PUBLIC_ID,
              AirlineXmlHelper.SYSTEM_ID);

      doc = dom.createDocument(null,"airline", docType);
    } catch (ParserConfigurationException | DOMException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Construct DOM tree
    try {
      Element root = doc.getDocumentElement();
      // Airline name
      Element name = doc.createElement("name");
      root.appendChild(name);
      name.appendChild(doc.createTextNode(airline.getName()));

      for(int i = 0; i < flightList.size(); i++) {
        // flightzzzz

        String flightNumber = String.valueOf(flightList.get(i).getNumber());
        String srcAirport = flightList.get(i).getSource();
        String destAirport = flightList.get(i).getDestination();

        Date arv = flightList.get(i).getArrival();
        Date dpt = flightList.get(i).getDeparture();

        Calendar calArrive = Calendar.getInstance();
        calArrive.setTime(arv);
        Calendar calDepart = Calendar.getInstance();
        calDepart.setTime(dpt);

        Element flight = doc.createElement("flight");
        root.appendChild(flight);

        Element number = doc.createElement("number");
        flight.appendChild(number);
        number.appendChild(doc.createTextNode(flightNumber));

        Element source = doc.createElement("src");
        flight.appendChild(source);
        source.appendChild(doc.createTextNode(srcAirport));

        Element depart = doc.createElement("depart");
        flight.appendChild(depart);
        Element dateDepart = doc.createElement("date");
        depart.appendChild(dateDepart);
        dateDepart.setAttribute("day", String.valueOf(calDepart.get(Calendar.DATE)));
        dateDepart.setAttribute("month", String.valueOf(calDepart.get(Calendar.MONTH)));
        dateDepart.setAttribute("year", String.valueOf(calDepart.get(Calendar.YEAR)));
        Element timeDepart = doc.createElement("time");
        depart.appendChild(timeDepart);
        timeDepart.setAttribute("hour", String.valueOf(calDepart.get(Calendar.HOUR_OF_DAY)));
        timeDepart.setAttribute("minute", String.valueOf(calDepart.get(Calendar.MINUTE)));

        Element dest = doc.createElement("dest");
        dest.appendChild(doc.createTextNode(destAirport));
        flight.appendChild(dest);

        Element arrive = doc.createElement("arrive");
        flight.appendChild(arrive);
        Element dateArrive = doc.createElement("date");
        arrive.appendChild(dateArrive);
        dateArrive.setAttribute("day", String.valueOf(calArrive.get(Calendar.DATE)));
        dateArrive.setAttribute("month", String.valueOf(calArrive.get(Calendar.MONTH)));
        dateArrive.setAttribute("year", String.valueOf(calArrive.get(Calendar.YEAR)));
        Element timeArrive = doc.createElement("time");
        arrive.appendChild(timeArrive);
        timeArrive.setAttribute("hour", String.valueOf(calArrive.get(Calendar.HOUR_OF_DAY)));
        timeArrive.setAttribute("minute", String.valueOf(calArrive.get(Calendar.MINUTE)));
      }
    } catch (DOMException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }

    // Write XML to the console
    try {
      Source src = new DOMSource(doc);
      Result res = null;

      if (filePath != null) {
        res = new StreamResult(this.filePath);
      } else if (this.writer != null) {
        res = new StreamResult(this.writer);
      }

      //      Result res = new StreamResult(System.out);

      TransformerFactory xFactory = TransformerFactory.newInstance();
      Transformer xform = xFactory.newTransformer();
      xform.setOutputProperty(OutputKeys.INDENT, "yes");
      xform.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, AirlineXmlHelper.SYSTEM_ID);
      xform.transform(src, res);
    } catch (TransformerException e) {
      e.printStackTrace(System.err);
      System.exit(1);
    }

    if (this.writer != null){
      this.writer.flush();
    }
  }
}
