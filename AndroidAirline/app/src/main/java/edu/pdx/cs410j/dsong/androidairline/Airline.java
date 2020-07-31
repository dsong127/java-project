package edu.pdx.cs410j.dsong.androidairline;

import edu.pdx.cs410J.AbstractAirline;

import java.util.ArrayList;
import java.util.LinkedHashSet;


public class Airline extends AbstractAirline<Flight> {
    private LinkedHashSet<Flight> flights;
    final private String name;

    /**
     * Creates a new <code>Airline</code> object
     *
     * @param name
     *        The name of this airline
     */
    public Airline(String name) {
        super();
        this.flights = new LinkedHashSet<>();
        this.name = name;
    }

    /**
     * Returns this <code>Airline</code>'s name
     *
     */
    @Override
    public String getName() {

        return this.name;
    }

    /**
     * Takes in a new <code>Flight</code> object, then iterates through the class' hashset to check if there is a duplicate
     * If not, it will add that flight to the set.
     *
     * @param newFlight
     *        The new <code>Flight</code> object user wants to add to this Airline's hashset
     * @throws IllegalStateException
     *          If there are duplicate flights.
     */
    @Override
    public void addFlight(Flight newFlight) throws IllegalStateException {
        for(Flight flight : this.flights) {
            if (flight.getNumber() == newFlight.getNumber()) {
                throw new IllegalStateException("Flight with that number already exists");
            }
        }
        this.flights.add(newFlight);
    }

    /**
     * Gets this airline's hashset of flights
     */
    @Override
    public LinkedHashSet<Flight> getFlights() {
        return this.flights;
    }

    public LinkedHashSet<String> getFormattedFlights() {
        LinkedHashSet<String> flightsList = new LinkedHashSet<>();
        for (Flight flight: this.flights) {
            String formattedFlight  = flight.getNumber() +" "+ flight.getSource() + " "+
                    flight.getOGDepartureString().replaceAll(",", "") + " " + flight.getDestination() +
                    " " + flight.getOGArrivalString().replaceAll(",", "");
            flightsList.add(formattedFlight);
        }

        return flightsList;
    }

    public ArrayList<Flight> getSortedFlightsAsList() {
        ArrayList<Flight> array = new ArrayList<>();
        array.addAll(0, this.getFlights());
        array.sort(new Flight.FlightComparator());

        return array;
    }

}
