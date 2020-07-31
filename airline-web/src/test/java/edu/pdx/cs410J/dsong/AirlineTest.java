package edu.pdx.cs410J.dsong;

import org.junit.Test;

import java.util.LinkedHashSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AirlineTest {


    @Test
    public void getName() {
        String name = "United";
        var unitedAirline = new Airline(name);
        assertThat(unitedAirline.getName(), equalTo(name));
    }

    @Test
    public void airlineInitiallyHasEmptySet() {
        String name = "test";
        var airline = new Airline(name);
        assertThat(airline.getFlights().isEmpty(), is(true));
    }

    @Test()
    public void airlineNameCanTakeMultipleWords() {
        var airline = new Airline("Some lame airline");
        assertThat(airline.getName(), equalTo("Some lame airline"));
    }


    @Test
    public void addAndGetFlight() {
        LinkedHashSet<Flight> testSet = new LinkedHashSet<>();
        var flight = new Flight("5", "pdx", "1/3/2020 1:03 am", "lax", "1/10/2020 1:03 pm");
        var airline = new Airline("testAirline");

        testSet.add(flight);
        airline.addFlight(flight);

        assertThat(airline.getFlights(), equalTo(testSet));
    }

    @Test
    public void addAndGetMultipleFlights() {
        var testSet = new LinkedHashSet<Flight>();
        var airline = new Airline("testAirline");

        var flight1 = new Flight("5", "pdx", "1/3/2020 1:03 am", "lax", "1/11/2020 1:03 am");
        var flight2 = new Flight("6", "sfo", "1/11/2020 5:03 pm", "pdx", "1/21/2020 3:03 pm");
        var flight3 = new Flight("7", "sea", "1/12/2020 3:03 pm", "sfo", "1/20/2020 1:03 pm");

        testSet.add(flight1);
        testSet.add(flight2);
        testSet.add(flight3);

        airline.addFlight(flight1);
        airline.addFlight(flight2);
        airline.addFlight(flight3);

        assertThat(airline.getFlights(), equalTo(testSet));
    }

    @Test
    public void sortMethodWorks() {
        var flight1 = new Flight("5", "pdx", "1/3/2020 1:03 am", "lax", "1/11/2020 1:03 am");
        var flight4 = new Flight("7", "pdx", "1/3/2020 1:10 am", "lax", "1/11/2020 1:03 am");
        var flight3 = new Flight("8", "pdx", "1/4/2020 1:03 am", "lax", "1/11/2020 1:03 am");
        var flight2 = new Flight("6", "sfo", "1/11/2020 5:03 pm", "pdx", "1/21/2020 3:03 pm");

        var airline = new Airline("test");

        airline.addFlight(flight1);
        airline.addFlight(flight2);
        airline.addFlight(flight3);
        airline.addFlight(flight4);

//        System.out.println("Before sort");
//        for (Flight f : airline.getFlights()) {
//            System.out.println(f);
//        }

        var sorted = airline.getSortedFlightsAsList();

//        System.out.println("After sort");
//        for (Flight f : airline.getSortedFlightsAsList()) {
//            System.out.println(f);
//        }

        assert(sorted.get(0).equals(flight1));
        assert(sorted.get(1).equals(flight4));
        assert(sorted.get(2).equals(flight3));
        assert(sorted.get(3).equals(flight2));
    }

}