package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.PropertyType;

/**
 * Test class for NifiReportingTasksGenerator
 */
public class NifiControllerServicesGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ControllerServiceType controllerServiceType = new ControllerServiceType();

    controllerServiceType.setName("TEST_CTRL_SER");
    controllerServiceType.setClazz(ControllerServiceType.class.getName());

    PropertyType propertyType = new PropertyType();
    propertyType.setName("PROP_NAME");
    propertyType.setValue("PROP_VALUE");
    controllerServiceType.getProperty().add(propertyType);


    NifiControllerServicesGenerator nifiControllerServicesGenerator =
        new NifiControllerServicesGenerator(controllerServiceType);
    String actual = nifiControllerServicesGenerator.generateTFElement("CTRL_SER", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_controller_service.tf").getFile());

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
