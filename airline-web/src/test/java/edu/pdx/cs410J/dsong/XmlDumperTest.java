package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.ParserException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class XmlDumperTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void testTakingAdump() throws IOException, ParserException {
    final File tempFile = tempFolder.newFile("testEmptyFile");

    var airline = new Airline("Dumped Airline");
    var dumper = new XmlDumper(tempFile.getPath());

    var flight1 = new Flight("123", "pdx", "1/10/2020 9:12 AM", "lax", "10/11/2020 11:11 PM");
    var flight2 = new Flight("133", "lax", "10/11/2020 9:10 AM", "lax", "10/14/2020 1:00 PM");

    airline.addFlight(flight1);
    airline.addFlight(flight2);

    dumper.dump(airline);

    assertEquals(tempFile.exists(), true);

    var parser = new XmlParser(tempFile.getPath(), airline.getName(), true);
    var parsedAirline = parser.parse();

    assertEquals(parsedAirline.getName(), "Dumped Airline");
    assertEquals(parsedAirline.getSortedFlightsAsList().toString(), airline.getSortedFlightsAsList().toString());
  }

}