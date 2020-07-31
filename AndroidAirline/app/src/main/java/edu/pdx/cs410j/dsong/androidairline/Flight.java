package edu.pdx.cs410j.dsong.androidairline;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Flight extends AbstractFlight {
  private int number;
  private Date arrival = null;
  private Date departure = null;
  private final String source;
  private final String destination;
  private String ogArrival;
  private String ogDeparture;
  private final Map<String, String> airportNames = AirportNames.getNamesMap();

  /**
   * Creates a new Flight object
   *
   * @param number
   *        Unique numeric value of type String that specifies this flight's number
   * @param source
   *        Three letter code of departure airport
   * @param departure
   *        Departure date and time
   *        Time: 12-hour time
   * @param destination
   *        Three letter code of arrival airport
   * @param arrival
   *        Arrival date and time
   *        Date: MM/DD/YYYY or M/DD/YYYY or MM/D/YYYY
   *        Time: 12-hour time
   * @throws IllegalArgumentException
   *         IllegalArgumentException thrown when:
   *         - When number is not a numeric value
   *         - Source and/or destination are not 3 letters
   *         - Format of departure/arrival time is incorrect
   */
  public Flight(String number, String source, String departure, String destination, String arrival)
          throws IllegalArgumentException {
    super();

    String[] splitDepart = departure.split("\\s+", 2);
    String[] splitArrival = arrival.split("\\s+", 2);

    int flightNumber = 0;
    DateFormat sf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
    final String time_regex = "(((0?[1-9])|(1[0-2])):([0-5])([0-9])\\s(A|P|a|p)(M|m))";
    Pattern pattern = Pattern.compile(time_regex);

    if (splitDepart[0].matches("\\d{1,2}/\\d{1,2}/\\d{4}") &&
            pattern.matcher(splitDepart[1]).matches()) {
      this.ogDeparture = departure;
      try {
        this.departure = sf.parse(departure);
      } catch (ParseException e) {
        System.err.println("Bad departure date format:" + departure);
        System.exit(1);
      }
    } else {
      throw new IllegalArgumentException("Bad departure date/time format");
    }

    if (splitArrival[0].matches("\\d{1,2}/\\d{1,2}/\\d{4}") &&
            pattern.matcher(splitArrival[1]).matches()) {
      this.ogArrival = arrival;
      try {
        this.arrival = sf.parse(arrival);
      } catch (ParseException e) {
        System.err.println("Bad arrival date format:" + arrival);
        System.exit(1);
      }
    } else {
      throw new IllegalArgumentException("Bad arrival date/time format");
    }

    if (this.departure.after(this.arrival)) {
      System.out.println(this.departure);
      System.out.println(this.arrival);
      throw new IllegalArgumentException("Departure date can't be after arrival date");
    }

    try {
      flightNumber = Integer.parseInt(number);
      this.number = flightNumber;
    } catch(IllegalArgumentException e) {
        throw new IllegalArgumentException("Flight number must be a numeric value");
    }

    if (source.length() == 3 && source.matches(".*[a-zA-Z].*")) {
      this.source = source;
    } else {
      throw new IllegalArgumentException("Source needs to be a 3 letter character code");
    }

    if (destination.length() == 3 && destination.matches(".*[a-zA-Z].*")) {
      this.destination = destination;
    } else {
      throw new IllegalArgumentException("Destination needs to be a 3 letter character code");
    }

    if (!airportNames.containsKey(source.toUpperCase())){
      throw new IllegalArgumentException("Source doesn't exist: " + source);
    }

    if (!airportNames.containsKey(destination.toUpperCase())){
      throw new IllegalArgumentException("Destination doesn't exist: " + destination);
    }
  }

  /**
   *
   * Returns this flight's number
   */
  @Override
  public int getNumber() {

    return this.number;
  }

  /**
   *
   * Returns this flight's source airport 3 letter code
   */
  @Override
  public String getSource() {

    return this.source;
  }

  /**
   *
   * Returns this flight's destination airport 3 letter code
   */
  @Override
  public String getDestination() {

    return this.destination;
  }

  /**
   *
   * Returns this flight's departure date/time as String
   */
  @Override
  public String getDepartureString() {

    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    return df.format(this.departure);
  }

  /**
   *
   * Returns this flight's arrival date/time as String
   */
  @Override
  public String getArrivalString() {

    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    return df.format(this.arrival);
  }

  public String getOGDepartureString() {
    return this.ogDeparture;
  }

  public String getOGArrivalString() {
    return this.ogArrival;
  }

  /**
   *
   * Returns this flight's departure date / time
   */

  @Override
  public Date getDeparture() {


    return this.departure;
  }

  /**
   *
   * Returns this flight's arrival date / time
   */

  @Override
  public Date getArrival() {

    return this.arrival;
  }

  /**
   *  Wrapper for comparator
   */
  static class FlightComparator implements Comparator<Flight> {
    /**
     * Sorts by flight's source first. If source airports are equal,
     * then further sorts by departure date
     *
     * @return -1, 1, 0 if flight departure airports are before, after, or equal
     *          If equal, same logic applies and returns one of the three numbers for date compares.
     */
    @Override
    public int compare(Flight flight1, Flight flight2) {
      int sourceCompare = flight1.getSource().compareTo(flight2.getSource());
      int dateCompare;

      if (flight1.departure.before(flight2.departure)) {
        dateCompare = -1;
      } else if (flight1.departure.after(flight2.departure)) {
        dateCompare = 1;
      } else {
        dateCompare = 0;
      }

      if (sourceCompare == 0) {
        return ((dateCompare == 0) ? sourceCompare : dateCompare);
      } else {
        return sourceCompare;
      }
    }
  }
}
