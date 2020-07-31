package edu.pdx.cs410J.dsong;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AirlineServletTest {

  @Test
  public void testNewFlightInServletStore() throws ServletException, IOException {

    String airlineName = "test";
    int flightNumber = 123;
    String src = "pdx";
    String dept = "10/10/2020 9:10 AM";
    String dest = "lax";
    String arr = "10/11/2020 11:11 AM";

    AirlineServlet servlet = createFlight(airlineName, flightNumber, src, dept, dest, arr);

    Airline airline = servlet.getAirline(airlineName);
    assertThat(airline, not(nullValue()));

    Flight flight = airline.getFlights().iterator().next();
    assertThat(flight.getNumber(), equalTo(flightNumber));
  }

  @Test
  public void testGetAirline() throws ServletException, IOException {

    String airlineName = "test";
    int flightNumber = 123;
    String src = "pdx";
    String dept = "10/10/2020 9:10 AM";
    String dest = "lax";
    String arr = "10/11/2020 11:11 AM";
    StringWriter sw = new StringWriter();
    PrintWriter pw  = new PrintWriter(sw);

    AirlineServlet servlet = createFlight(airlineName, flightNumber, src, dept, dest, arr);

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(request.getParameter("airline")).thenReturn(airlineName);
    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    assertThat(sw.toString(), containsString("test"));
  }

  @Test
  public void testSearchAirline() throws ServletException, IOException {

    String airlineName = "test";
    int flightNumber = 123;
    String src = "pdx";
    String dept = "10/10/2020 9:10 AM";
    String dest = "lax";
    String arr = "10/11/2020 11:11 AM";

    StringWriter sw = new StringWriter();
    PrintWriter pw  = new PrintWriter(sw);

    AirlineServlet servlet = createFlight(airlineName, flightNumber, src, dept, dest, arr);

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    when(request.getParameter("airline")).thenReturn(airlineName);
    when(response.getWriter()).thenReturn(pw);
    when(request.getParameter("src")).thenReturn(src);
    when(request.getParameter("dest")).thenReturn(dest);

    servlet.doGet(request, response);

    assertThat(sw.toString(), containsString("test"));
  }

  private AirlineServlet createFlight(String airlineName, int flightNumber, String src, String dept,
                                      String dest, String arr) throws IOException, ServletException {
    AirlineServlet servlet = new AirlineServlet();
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("airline")).thenReturn(airlineName);
    when(request.getParameter("flightNumber")).thenReturn(String.valueOf(flightNumber));
    when(request.getParameter("src")).thenReturn(src);
    when(request.getParameter("depart")).thenReturn(dept);
    when(request.getParameter("dest")).thenReturn(dest);
    when(request.getParameter("arrive")).thenReturn(arr);

    HttpServletResponse response = mock(HttpServletResponse.class);

    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    return servlet;
  }

}
