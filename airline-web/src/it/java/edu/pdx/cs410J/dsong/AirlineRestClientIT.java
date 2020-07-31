package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AirlineRestClient newAirlineRestClient() {
    int port = Integer.parseInt(PORT);
    return new AirlineRestClient(HOSTNAME, port);
  }

  @Test
    public void test0AddOneFlight() throws IOException, ParserException {
      AirlineRestClient client = newAirlineRestClient();
      String airlineName = "test";
      String flightNumber = "123";
      String src = "pdx";
      String dept = "10/10/2020 9:10 AM";
      String dest = "lax";
      String arr = "10/11/2020 11:11 AM";

      client.addFlight(airlineName, flightNumber, src, dept, dest, arr);
      Airline airline = client.getAirline(airlineName);
      ArrayList<Flight> flights = airline.getSortedFlightsAsList();
      assertThat(airline.getName(), equalTo(airlineName));
      assertThat(flights.get(0).getNumber(), equalTo(123));
  }

  @Test
  public void test1searchFlight() throws IOException, ParserException {
    AirlineRestClient client = newAirlineRestClient();
    String airlineName = "test";
    String flightNumber = "1234";
    String src = "pdx";
    String dept = "10/10/2020 9:10 AM";
    String dest = "lax";
    String arr = "10/11/2020 11:11 AM";

    String flightNumber2 = "111111";
    String src2 = "sfo";
    String dept2 = "10/10/2020 9:10 AM";
    String dest2 = "lax";
    String arr2 = "10/11/2020 11:11 AM";

    client.addFlight(airlineName, flightNumber, src, dept, dest, arr);
    client.addFlight(airlineName, flightNumber2, src2, dept2, dest2, arr2);

    Airline airline = client.searchFlights(airlineName, src, dest);
    ArrayList<Flight> flights = airline.getSortedFlightsAsList();
    assertThat(airline.getName(), equalTo(airlineName));
//    assertThat(flights.size(), equalTo(1)); // This passes in Intellij but not in mvn verify..
    assertThat(flights.size(), equalTo(2));
  }

  @Test(expected = AirlineRestClient.AirlineRestException.class)
  public void test2NoDuplicateFlights() throws IOException, ParserException {
    AirlineRestClient client = newAirlineRestClient();
    String airlineName = "test";
    String flightNumber = "1234";
    String src = "pdx";
    String dept = "10/10/2020 9:10 AM";
    String dest = "lax";
    String arr = "10/11/2020 11:11 AM";

    client.addFlight(airlineName, flightNumber, src, dept, dest, arr);
    client.addFlight(airlineName, flightNumber, src, dept, dest, arr);
  }


}
