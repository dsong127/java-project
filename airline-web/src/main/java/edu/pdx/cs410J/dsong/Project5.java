package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static void main(String... args) {
        String hostName = null;
        String portString = null;

        String airlineName = null;
        String flightNumber = null;
        String srcAirport = null;
        String destAirport = null;

        String dDate = null;
        String aDate = null;

        boolean printing = false;
        boolean searching = false;
        boolean queryAirline = false;
        boolean addingFlight = false;

        Flight newFlight = null;

        if (args.length == 0) {
            printErrorMessageAndExit("Missing line arguments");
        }

        /*
         *  Goes through each argument and parses the options first, then parses each flight argument
         */

        for (int i = 0; i < args.length; i++) {

            try {
                if (args[i].equals("-README")) {
                    printReadmeAndQuit();

                } else if (args[i].equals("-host")) {
                    if (++i >= args.length) {
                        printErrorMessageAndExit("No host name specified");
                    }
                    hostName = args[i];

                } else if (args[i].equals("-port")) {

                    if (++i >= args.length) {
                        printErrorMessageAndExit("No port number specified");
                    }
                    portString = args[i];

                } else if (args[i].equals("-search")) {
                    searching = true;
                    if (++i >= args.length) {
                        printErrorMessageAndExit("No airline name specified for search option");
                    }
                    airlineName = args[i];
                    if (++i >= args.length) {
                        printErrorMessageAndExit("No source airport specified for search option");
                    }
                    srcAirport = args[i];
                    if (++i >= args.length) {
                        printErrorMessageAndExit("No depart airport specified for search option");
                    }
                    destAirport = args[i];

                    // Lazy way of validate airport codes
                    try {
                        new Flight("1", srcAirport, "10/10/2020 10:10 am", destAirport, "10/11/2020 11:11 pm");
                    } catch (IllegalArgumentException e) {
                        printErrorMessageAndExit(e.getMessage());
                    }
                    break;

                } else if (args[i].equals("-print")) {
//                    if (++i >= args.length) {
//                        printErrorMessageAndExit("No arguments given");
//                    }
                    printing = true;
                } else if (airlineName == null) {
                    airlineName = args[i];
                    if (i == args.length-1) {
                        queryAirline = true;
                        break;
                    }
                } else if (flightNumber == null) {
                    flightNumber = args[i];
                } else if (srcAirport == null) {
                    srcAirport = args[i];
                } else if (dDate == null) {
                    dDate = args[i];
                    dDate = dDate.concat(" " + args[++i]);
                    dDate = dDate.concat(" " + args[++i]);
                } else if (destAirport == null){
                    destAirport = args[i];
                } else if (aDate == null) {
                    aDate = args[i];
                    aDate = aDate.concat(" " + args[++i]);
                    aDate = aDate.concat(" " + args[++i]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                printErrorMessageAndExit("Missing arguments");
            }

            if (airlineName != null && flightNumber != null && srcAirport != null &&
                    dDate != null && destAirport != null && aDate != null) {

                addingFlight = true;

                // Checking for extraneous arguments
                if (i != args.length-1) {
                    printErrorMessageAndExit("Too many arguments!");
                }
            }
        }

        int portNumber = -1;
        try {
            portNumber = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            printErrorMessageAndExit("Port must be an integer");
        }

        AirlineRestClient client = new AirlineRestClient(hostName, portNumber);


        if (queryAirline) {
            try {
                var res = client.getAirline(airlineName);
                if (res != null) {
                    new PrettyPrinter(null, res.getSortedFlightsAsList()).writeToStd();
                } else {
                    System.out.println("Airline " + airlineName + " doesn't exist");
                }

            } catch(IOException | ParserException e) {
                printErrorMessageAndExit(e.getMessage());
            }


        } else if (searching) {
            try {
                var res = client.searchFlights(airlineName, srcAirport, destAirport);
                if (res != null) {
                    new PrettyPrinter(null, res.getSortedFlightsAsList()).writeToStd();
                } else {
                    System.out.println("No flights traveling from lax to pdx");
                }

            } catch (IOException | ParserException e) {
                printErrorMessageAndExit(e.getMessage());
            }
        } else if (addingFlight) {
            try {
                // Only doing this because all arg validation logic is in Flight object :(
                newFlight = new Flight(flightNumber, srcAirport, dDate, destAirport, aDate);
                client.addFlight(airlineName, flightNumber, srcAirport, dDate, destAirport, aDate);
            } catch (IllegalArgumentException | IOException e) {
                printErrorMessageAndExit(e.getMessage());
            } catch (AirlineRestClient.AirlineRestException e) {
                printErrorMessageAndExit(e.getMessage() + " Possibly duplicate flight");
            }

            if (printing) {
                System.out.println("New flight created: " + newFlight);
            }
        }

    }

    private static void printErrorMessageAndExit(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private static void printReadmeAndQuit() {
        System.out.println("\nusage: java edu.pdx.cs410J.dsong.Project5 [options] <args>");
        System.out.println("  args are (in this order):");
        System.out.println("    airline \t\t The name of the airline");
        System.out.println("    flightNumber \t The flight number");
        System.out.println("    src \t\t Three-letter code of departure airport");
        System.out.println("    depart \t\t Departure date and time (24-hour time)");
        System.out.println("    dest \t\t Three-letter code of arrival airport");
        System.out.println("    arrive \t\t Arrival date and time (24-hour time)");
        System.out.println("  options are (options may appear in any order):");
        System.out.println("    -host hostname \t Host computer on which the server runs");
        System.out.println("    -port port \t Port on which the server is listening");
        System.out.println("    -print \t\t Prints a description of the new flight");
        System.out.println("    -search \t\t Search for flights");
        System.out.println("    -README \t\t Prints a README for this project and exits\n");

        System.out.println("      Date and time should be in the format: mm/dd/yyyy hh:mm a\n");

        System.out.println("Project 5 for CS510: Advanced Programming In Java");
        System.out.println("Written by: Daniel Song");

        System.out.println("Project 5 implements a server that provides RESTful web service to the client");

        System.exit(0);
    }
}