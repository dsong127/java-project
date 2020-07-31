package edu.pdx.cs410J.dsong;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link Flight} class.
 */
public class FlightTest {

  @Test()
  public void getArrivalStringNeedsToBeImplemented() {
    Flight flight = new Flight("10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getArrivalString(), equalTo("3/16/20, 10:00 AM"));
  }

  @Test()
  public void getArrivalSingleDigitDay() {
    Flight flight = new Flight("10", "PDX", "3/1/2020 10:00 AM", "LAX", "3/8/2020 10:00 AM");
    assertThat(flight.getArrivalString(), equalTo("3/8/20, 10:00 AM"));
  }

  @Test()
  public void getArrivalZeroSingleDigitDay() {
    Flight flight = new Flight("10", "PDX", "3/03/2020 10:00 AM", "LAX", "3/08/2020 10:00 AM");
    assertThat(flight.getArrivalString(), equalTo("3/8/20, 10:00 AM"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failsWhenNonNumericValueIsGiven() {
    Flight flight = new Flight("a10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
  }

  @Test
  public void getsExpectedNumber(){
    String number = "10";
    Flight flight = new Flight(number, "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getNumber(), equalTo(10));
  }

  @Test
  public void getsExpectedSource(){
    Flight flight = new Flight("10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getSource(), equalTo("PDX"));
  }

  @Test
  public void getsExpectedDestination(){
    Flight flight = new Flight("10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getDestination(), equalTo("LAX"));
  }

  @Test
  public void getsExpectedDeparture(){
    Flight flight = new Flight("10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getDepartureString(), equalTo("3/15/20, 10:00 AM"));
  }

  @Test
  public void getsExpectedArrival(){
    Flight flight = new Flight("10", "PDX", "3/15/2020 10:00 AM", "LAX", "3/16/2020 10:00 AM");
    assertThat(flight.getArrivalString(), equalTo("3/16/20, 10:00 AM"));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testBogusSrcCode(){
    Flight flight = new Flight("10","aeklrjka", "11/11/19 11:11 AM", "lax", "11/11/2020 11:14 AM");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBogusDestCode(){
    Flight flight = new Flight("10","pdx", "11/11/19 11:11 AM", "1", "11/11/2020 11:14 AM");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBadDateException() {
    Flight flight = new Flight("10","pdx", "11/1111/2020 11:11 AM", "lax", "11/11/2020 11:14 AM");
  }

  @Test()
  public void testGetDeparture() {
    Flight flight = new Flight("10","pdx", "8/1/2020 10:10 AM", "lax", "11/11/2020 11:14 AM");
//    assertThat(flight.getDeparture().toString(), equalTo("Sat Aug 01 10:10:00 PDT 2020"));
  }

  @Test()
  public void testGetArrival() {
    Flight flight = new Flight("10","pdx", "8/1/2020 10:10 AM", "lax", "11/11/2020 11:14 AM");
    assertThat(flight.getArrival().toString(), equalTo("Wed Nov 11 11:14:00 PST 2020"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void sourceDoesntExist() {
    Flight flight = new Flight("10","pox", "8/1/2020 10:10 AM", "lax", "11/11/2020 11:14 AM");
    assertThat(flight.getArrival().toString(), equalTo("Wed Nov 11 11:14:00 PST 2020"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void destDoesntExist() {
    Flight flight = new Flight("10","lax", "8/1/2020 10:10 AM", "pox", "11/11/2020 11:14 AM");
    assertThat(flight.getArrival().toString(), equalTo("Wed Nov 11 11:14:00 PST 2020"));
  }

  @Test()
  public void flightComparator() {
    Flight flight = new Flight("10","pdx", "8/1/2020 10:10 AM", "lax", "11/11/2020 11:14 AM");
    Flight flight2 = new Flight("101","pdx", "7/1/2020 10:10 AM", "lax", "11/11/2020 11:14 AM");
    ArrayList<Flight> array = new ArrayList<>();

    array.add(flight);
    array.add(flight2);

    array.sort(new Flight.FlightComparator());

    assertThat(array.get(0).getNumber(), equalTo(101));
  }

}
