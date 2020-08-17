package edu.pdx.cs410J.dsong;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import edu.pdx.cs410J.dsong.Airline;


public class AirlineServlet extends HttpServlet {

  private final Map<String, String> dictionary = new HashMap<>();
  private Map<String, Airline> airlineMap = new HashMap<>();

  /**
   * HTTP GET handler for returning all flights, and for a specific Airline's flights
   * traveling from source airport to destination airport.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType("text/xml; charset=UTF-8");

    String airlineName = getParameter( "airline", request);
    String src = getParameter( "src", request);
    String dest = getParameter( "dest", request);

    if (airlineName != null) {
      if (src != null && dest != null) {
        // Search flights traveling from specified source to specified destination.
        // Airline object to return - This does not get saved into the map
        Airline dummyAirline = new Airline(airlineName);
        Airline airline = airlineMap.get(airlineName);
        
        if (airline != null) {
          LinkedHashSet<Flight> allFlights = airline.getFlights();

          // Iterate through each flight from specified Airline.
          // Find matching flights and add to dummyAirline
          for (Flight flight : allFlights) {
            if (flight.getSource().equals(src) && flight.getDestination().equals(dest)) {
              dummyAirline.addFlight(flight);
            }
          }

          if(!dummyAirline.getFlights().isEmpty()) {
            writeAirline(response, dummyAirline);
          } else {
            writeMessage(response, "No flights traveling from " + src + " to " + dest);
          }
        } else {
          writeMessage(response, "Search failed: No airline named " + airlineName + " exists");
        }

      } else {
        // Query all flights
        if (airlineMap.containsKey(airlineName)) {
          writeAirline(response, airlineMap.get(airlineName));
        } else {
          writeMessage(response, "No matching airline: " + airlineName);
        }
      }
    } else {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Missing required parameter: Airline");
    }
  }

  /**
   * Handles an HTTP POST request by storing the flight entry for the
   * request parameters.  It writes the Airline as XML
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
    response.setContentType( "text/plain" );

    Airline newAirline = null;
    Flight newFlight = null;

    String airlineName = getParameter("airline", request );
    if (airlineName == null) {
        missingRequiredParameter(response, "airline");
        return;
    }

    String flightNumber = getParameter("flightNumber", request );
    if (flightNumber == null) {
      missingRequiredParameter(response, "flightNumber");
      return;
    }

    String src = getParameter("src", request );
    if (src == null) {
      missingRequiredParameter(response, "src");
      return;
    }

    String depart = getParameter("depart", request );
    if (depart == null) {
      missingRequiredParameter(response, "depart");
      return;
    }

    String dest = getParameter("dest", request );
    if (dest == null) {
      missingRequiredParameter(response, "dest");
      return;
    }

    String arrive = getParameter("arrive", request );
    if (arrive == null) {
      missingRequiredParameter(response, "arrive");
      return;
    }

    try {
      newFlight = new Flight(flightNumber, src, depart, dest, arrive);
    } catch (IllegalArgumentException e) {
//      response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad flight arguments");
    }

    // Writer class for dumping formatted string results to client
    PrintWriter pw = response.getWriter();

    // Update existing airline with new flight. Otherwise, create a new airline.
    if(airlineMap.containsKey(airlineName)){
      Airline updatedAirline = airlineMap.get(airlineName);
      updatedAirline.addFlight(newFlight);
      airlineMap.put(airlineName, updatedAirline);
      pw.println("Added " + newFlight.toString() + " to existing airline " + updatedAirline.getName());

    } else {
      newAirline = new Airline(airlineName);
      newAirline.addFlight(newFlight);
      airlineMap.put(airlineName, newAirline);
      pw.println("Created a new airline: " + newAirline.getName() + " containing " + newFlight.toString());
    }

    pw.flush();
    response.setStatus( HttpServletResponse.SC_OK);

  }

  /**
   * Throws an IO Exception if a parameter is missing
   *
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = "The required parameter " + parameterName + " is missing";
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   *  Method for returning plain text. Mostly for outputting GET output with 0 results
   *
   */
  private void writeMessage(HttpServletResponse response, String message) throws IOException {
    response.setContentType("text/plain");
    PrintWriter pw = response.getWriter();
    pw.println(message);
    pw.flush();
  }

  /**
   * Uses XmlDumper to return a requested Airline
   *
   */

  private void writeAirline(HttpServletResponse response, Airline airline) throws IOException {
    PrintWriter pw = response.getWriter();
    XmlDumper dumper = new XmlDumper(pw);
    dumper.dump(airline);
    response.setStatus( HttpServletResponse.SC_OK );
  }

  /**
   * Gets an airline given a key (Airline name) - For testing
   *
   */
  @VisibleForTesting
  Airline getAirline(String airlineName) {
    return this.airlineMap.get(airlineName);
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }
}
