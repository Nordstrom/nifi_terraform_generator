package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.BendPointsType;
import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.PositionType;

/**
 * Test class for NifiFunnelGenerator
 */
public class NifiConnectionsGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ConnectionType connectionType = new ConnectionType();
    BendPointsType bendPoints = new BendPointsType();
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    bendPoints.getBendPoint().add(positionType);
    connectionType.setBendPoints(bendPoints);
    connectionType.setLabelIndex(100);
    connectionType.setZIndex(10);

    connectionType.setSourceId("SRC_ID");
    connectionType.setSourceGroupId("SRC_GRP_ID");
    connectionType.setSourceType("SRC_TYP");

    connectionType.setDestinationId("DEST_ID");
    connectionType.setDestinationGroupId("DEST_GRP_ID");
    connectionType.setDestinationType("DEST_TYP");

    connectionType.getRelationship().add("A");
    connectionType.setMaxWorkQueueSize(BigInteger.valueOf(10));
    connectionType.setMaxWorkQueueDataSize("100");


    NifiConnectionsGenerator nifiConnectionsGenerator =
        new NifiConnectionsGenerator(connectionType);
    String actual = nifiConnectionsGenerator.generateTFElement("CONNECTION", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_connection.tf").getFile());

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
