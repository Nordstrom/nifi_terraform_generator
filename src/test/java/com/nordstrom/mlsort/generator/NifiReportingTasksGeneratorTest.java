package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.ReportingTaskType;
import com.nordstrom.mlsort.jaxb.SchedulingStrategy;

/**
 * Test class for NifiReportingTasksGenerator
 */
public class NifiReportingTasksGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {
    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ReportingTaskType reportingTaskType = new ReportingTaskType();
    reportingTaskType.setName("TEST_REP_TSK");
    reportingTaskType.setClazz(ReportingTaskType.class.getName());
    reportingTaskType.setSchedulingPeriod("1 min");
    reportingTaskType.setSchedulingStrategy(SchedulingStrategy.PRIMARY_NODE_ONLY);

    PropertyType propertyType = new PropertyType();
    propertyType.setName("PROP_NAME");
    propertyType.setValue("PROP_VALUE");
    reportingTaskType.getProperty().add(propertyType);

    NifiReportingTasksGenerator nifiReportingTasksGenerator =
        new NifiReportingTasksGenerator(reportingTaskType);
    String actual =
        nifiReportingTasksGenerator.generateTFElement("REPORTING_TASKS_NAME", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_reporting_task.tf").getFile());

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
