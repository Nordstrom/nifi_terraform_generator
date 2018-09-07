package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.PositionType;

/**
 * Test class for NifiFunnelGenerator
 */
public class NifiFunnelGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    FunnelType funnelType = new FunnelType();

    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    funnelType.setPosition(positionType);


    NifiFunnelGenerator nifiFunnelGenerator = new NifiFunnelGenerator(funnelType);
    String actual = nifiFunnelGenerator.generateTFElement("FUNNEL", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_funnel.tf").getFile());

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
