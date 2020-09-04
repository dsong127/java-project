package edu.pdx.cs410J.dsong;

import edu.pdx.cs410J.ParserException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class XmlParserTest {

  @Test
  public void testAirline() throws ParserException, ParserConfigurationException, IOException, SAXException {
    AirlineXmlHelper helper = new AirlineXmlHelper();

    var expectedAirline = new Airline("Valid Airlines");
    String xml = "<?xml version='1.0' encoding='us-ascii'?>\n" +
            "\n" +
            "<!DOCTYPE airline\n" +
            "          SYSTEM \"http://www.cs.pdx.edu/~whitlock/dtds/airline.dtd\">\n" +
            "\n" +
            "<airline>\n" +
            "  <name>Valid Airlines</name>\n" +
            "  <flight>\n" +
            "    <number>1437</number>\n" +
            "    <src>BJX</src>\n" +
            "    <depart>\n" +
            "      <date day=\"25\" month=\"9\" year=\"2020\"/>\n" +
            "      <time hour=\"17\" minute=\"0\"/>\n" +
            "    </depart>\n" +
            "    <dest>CMN</dest> <!-- They're taking me to Marrakesh! -->\n" +
            "    <arrive>\n" +
            "      <date day=\"26\" month=\"9\" year=\"2020\"/>\n" +
            "      <time hour=\"3\" minute=\"56\"/>\n" +
            "    </arrive>\n" +
            "  </flight>\n" +
            "  <flight>\n" +
            "    <number>7865</number>\n" +
            "    <src>JNB</src>\n" +
            "    <depart>\n" +
            "      <date day=\"15\" month=\"5\" year=\"2020\"/>\n" +
            "      <time hour=\"7\" minute=\"24\"/>\n" +
            "    </depart>\n" +
            "    <dest>XIY</dest>\n" +
            "    <arrive>\n" +
            "      <date day=\"16\" month=\"5\" year=\"2020\"/>\n" +
            "      <time hour=\"9\" minute=\"7\"/>\n" +
            "    </arrive>\n" +
            "  </flight>\n" +
            "</airline>";

    var parser = new XmlParser(xml, expectedAirline.getName(), false);
    var airline = parser.parse();


    assertEquals(airline.getName(), expectedAirline.getName());
  }

 @Test(expected = ParserException.class)
 public void cantParseInvalidXmlFile() throws ParserConfigurationException, IOException, SAXException, ParserException {
   AirlineXmlHelper helper = new AirlineXmlHelper();

   var name = "Valid Airlines";

   var parser = new XmlParser(this.getClass().getResource("invalid-airline.xml").getPath(), name, true);
   parser.parse();

 }
}