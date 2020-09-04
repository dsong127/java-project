package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.dsong.AirlineRestClient.AirlineRestException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project5IT extends InvokeMainTestCase {

    @Test
    public void test0NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project5.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing line arguments"));
    }

    @Test
    public void test2NoAirline() {
        String airlineName = "testAirline";

        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName);
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("Airline testAirline doesn't exist"));
    }

    @Test
    public void test3createFlightNewAirline() {
        String airlineName = "testAirline";
        String flightNumber = "3";
        String src = "pdx";
        String dept = "10/10/2020 9:10 AM";
        String dest = "lax";
        String arr = "10/11/2020 11:11 AM";

        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName, flightNumber, src, dept, dest, arr);

        MainMethodResult result2 = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName);
        String out = result2.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("Airline testAirline doesn't exist"));

    }

    @Test
    public void test4search() {
        String airlineName = "testAirline";
        String flightNumber = "3";
        String src = "pdx";
        String dept = "10/10/2020 9:10 AM";
        String dest = "lax";
        String arr = "10/11/2020 11:11 AM";


        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName, flightNumber, src, dept, dest, arr);
        invokeMain( Project5.class, "-host localhost", "-port 8080", airlineName, "123", "lax", "10/10/2020 9:10 AM", "pdx", "10/11/2020 11:11 AM");

        MainMethodResult searchResult = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", "-search", airlineName, "lax", "pdx");
        String out = searchResult.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("No flights traveling from lax to pdx"));
    }

    @Test
    public void test5queryAirline() {
        String airlineName = "testAirline";
        String flightNumber = "3";
        String src = "pdx";
        String dept = "10/10/2020 9:10 AM";
        String dest = "lax";
        String arr = "10/11/2020 11:11 AM";


        MainMethodResult result = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName, flightNumber, src, dept, dest, arr);

        MainMethodResult result2 = invokeMain( Project5.class, "-host", "localhost", "-port", "8080", airlineName);
        String out2 = result2.getTextWrittenToStandardOut();
        assertThat(out2, containsString(""));
    }

    @Test
    public void test6readme() {
        MainMethodResult result = invokeMain( Project5.class, "-README");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));

    }

}