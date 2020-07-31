package edu.pdx.cs410J.dsong;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final String url;
    

    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }


  /**
   * Quries an airlines and pretty prints all its flights
   * @param airlineName name of the airline to query
   * @return quried Airline
   * @throws IOException If an HTTP exception is thrown
   * @throws ParserException If parser can't parse xml returned by the server
   */
  public Airline getAirline(String airlineName) throws IOException, ParserException {
    Response response = get(this.url, Map.of("airline", airlineName));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    Airline airline = null;
    if (!content.contains("No")) {
      airline = new XmlParser(content, airlineName, false).parse();
    }
    return airline;
  }

  /**
   * Searches an Airlines and returns flights traveling from src to dest
   * @param airlineName airline queried
   * @param src source airport
   * @param dest destination airport
   * @return A dummy airline containing all flights traveling from src to dest
   * @throws IOException If HTTP exception is thrown
   * @throws ParserException If XML can't parse string
   */
  public Airline searchFlights(String airlineName, String src, String dest) throws IOException, ParserException {
    Response response = get(this.url, Map.of("airline", airlineName, "src", src, "dest", dest));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    Airline airline = null;

    if (!content.contains("No")) {
      airline = new XmlParser(content, airlineName, false).parse();
    }

    return airline;
  }

  /**
   * Adds a new flight to an airline in the server, creating a new Airline if necessary
   * @param airlineName Airline to add the flight to
   * @param flightNumber Flight number
   * @param src Source airport
   * @param depart Depart date
   * @param dest destination airport
   * @param arrive arrival date
   * @throws IOException If HTTP exception is thrown
   */
  public void addFlight(String airlineName, String flightNumber, String src, String depart, String dest,
                        String arrive) throws IOException {

    Response response = postFlight(airlineName, flightNumber, src, depart, dest, arrive);
    throwExceptionIfNotOkayHttpStatus(response);
  }

  /**
   * Post method for adding a new flight
   * @param airlineName Airline to add the flight to
   * @param flightNumber Flight number
   * @param src Source airport
   * @param depart Depart date
   * @param dest destination airport
   * @param arrive arrival date
   * @throws IOException If HTTP exception is thrown
   */
  @VisibleForTesting
  Response postFlight(String airlineName, String flightNumber, String src, String depart, String dest,
                         String arrive) throws IOException {

    return post(this.url, Map.of("airline", airlineName, "flightNumber", flightNumber,
            "src", src, "depart", depart, "dest", dest,
            "arrive", arrive));
  }

  /**
   * Throws exception if HTTP status is not 2xx
   * @param response Web request
   * @return same response that was passed in
   */
  private Response throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getCode();
    if (code != HTTP_OK) {
      throw new AirlineRestException(code);
    }
    return response;
  }

  /**
   * Exeption class that is thrown when there is an http error
   */
  @VisibleForTesting
  class AirlineRestException extends RuntimeException {
    AirlineRestException(int httpStatusCode) {
      super("Got an HTTP Status Code of " + httpStatusCode);
    }
  }
}
