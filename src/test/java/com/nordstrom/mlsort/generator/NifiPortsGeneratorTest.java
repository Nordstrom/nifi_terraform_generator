package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.PositionType;

/**
 * Test class for NifiPortsGenerator
 */
public class NifiPortsGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    PortType portType = new PortType();

    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    portType.setPosition(positionType);
    portType.setName("PRT_TYP_NAME");

    NifiPortsGenerator nifiPortsGenerator = new NifiPortsGenerator(portType);
    nifiPortsGenerator.setPortType("INPUT");
    String actual = nifiPortsGenerator.generateTFElement("PROCESSOR", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_port.tf").getFile());

    String expected = FileUtils.readFileToString(file);

    String[] expectedarray = expected.split("\n");
    boolean comparison = expectedarray.length == actualarray.length;
    for (int i = 0; i < expectedarray.length; i++) {
      if (!comparison) {
        break;
      }
      String expectedrow = expectedarray[i];
      String actualrow = actualarray[i];
      comparison = expectedrow.trim().equals(actualrow.trim());
    }

    assertTrue("Response id not matching the Expected string", comparison);
  }

}
