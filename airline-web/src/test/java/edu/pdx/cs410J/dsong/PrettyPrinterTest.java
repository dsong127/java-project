package edu.pdx.cs410J.dsong;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrettyPrinterTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void testDump() throws IOException {
    var airline = new Airline("airline");
    ArrayList<Flight> sortedList = new ArrayList<>();
    var flight1 = new Flight("1", "pdx", "10/10/2020 10:10 AM", "lax", "10/10/2020 10:20 AM");
    var flight2 = new Flight("2", "pdx", "10/11/2020 10:10 AM", "lax", "10/12/2020 10:20 AM");
    sortedList.add(flight1);
    sortedList.add(flight2);

    final File tempFile = tempFolder.newFile("tempFile");
    var pp = new PrettyPrinter(tempFile.getPath(), sortedList);

    pp.dump(airline);

    BufferedReader br = new BufferedReader(new FileReader(tempFile.getPath()));

    var st = br.readLine();
    assertThat(st, equalTo("Flight number: 1 \t Total duration: 10 minutes"));
  }

  @Test
  public void testToStd() {
    var airline = new Airline("airline");
    ArrayList<Flight> sortedList = new ArrayList<>();
    var flight1 = new Flight("1", "pdx", "10/10/2020 10:10 AM", "lax", "10/10/2020 10:20 AM");
    sortedList.add(flight1);
    var pp = new PrettyPrinter(null, sortedList);

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    pp.writeToStd();
    System.setOut(System.out);

    assertThat(outContent.toString(), equalTo("Flight number: 1 \t Total duration: 10 minutes\n\n" +
            "\t Departs: Portland, OR on October 10, 2020 10:10:00 AM\n" +
            "\t Arrives: Los Angeles, CA on October 10, 2020 10:20:00 AM\n\n"));
  }
}

