package edu.pdx.cs410J.dsong;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;
import jline.internal.Nullable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PrettyPrinter implements AirlineDumper<Airline> {

  private String filePath = null;
  private List<Flight> flights;
  private final Map<String, String> airportNames = AirportNames.getNamesMap();


  /**
   * Ccreates a <Code>PrettyPrinter</Code> object.
   * @param filePath
   *        File path to pretty write to
   * @param sortedList
   *        Sorted list of flights to pretty write
   */
  public PrettyPrinter(@Nullable String filePath, ArrayList<Flight> sortedList) {
    if (filePath != null) {
      this.filePath = filePath;
    }
    this.flights = sortedList;
  }

  /**
   * Gets flights, formats, then writes to file
   * @param airline
   *        Not really used
   * @throws FileNotFoundException
   *         If file is not found
   */
  @Override
  public void dump(Airline airline) throws FileNotFoundException {

    PrintWriter pw = new PrintWriter(this.filePath);

    for (Flight f : this.flights) {
      long diff = f.getArrival().getTime() - f.getDeparture().getTime();
      long duration = TimeUnit.MILLISECONDS.toMinutes(diff);

      pw.printf("Flight number: %s \t Total duration: %d minutes\n\n" +
                      "\t Departs: %s on %tB %<te, %<tY %tT %Tp\n" +
                      "\t Arrives: %s on %tB %<te, %<tY %tT %Tp\n\n",
              f.getNumber(),
              duration,
              airportNames.get(f.getSource().toUpperCase()),
              f.getDeparture(), f.getDeparture(), f.getDeparture(),
              airportNames.get(f.getDestination().toUpperCase()),
              f.getArrival(), f.getArrival(), f.getArrival());
    }
    pw.close();
  }

  /**
   * If "-" value is given for "-pretty" option, formats the flights and pretty writes to standard out
   */
  public void writeToStd() {
    for (Flight f : this.flights) {
      long diff = f.getArrival().getTime() - f.getDeparture().getTime();
      long duration = TimeUnit.MILLISECONDS.toMinutes(diff);

      System.out.printf("Flight number: %s \t Total duration: %d minutes\n\n" +
              "\t Departs: %s on %tB %<te, %<tY %tT %Tp\n" +
              "\t Arrives: %s on %tB %<te, %<tY %tT %Tp\n\n",
              f.getNumber(),
              duration,
              airportNames.get(f.getSource().toUpperCase()),
              f.getDeparture(), f.getDeparture(), f.getDeparture(),
              airportNames.get(f.getDestination().toUpperCase()),
              f.getArrival(), f.getArrival(), f.getArrival());
    }
  }

}
