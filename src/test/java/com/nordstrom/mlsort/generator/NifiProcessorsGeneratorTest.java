package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.ExecutionNode;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.SchedulingStrategy;

/**
 * Test class for NifiProcessorsGenerator
 */
public class NifiProcessorsGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ProcessorType processorType = new ProcessorType();

    processorType.setName("TEST_PROCR");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    processorType.setPosition(positionType);
    processorType.setClazz(ProcessorType.class.getName());
    processorType.getAutoTerminatedRelationship().add("RLSP");
    PropertyType propertyType = new PropertyType();
    propertyType.setName("PROP_NAME");
    propertyType.setValue("PROP_VALUE");
    processorType.getProperty().add(propertyType);
    processorType.setMaxConcurrentTasks(BigInteger.valueOf(100));
    processorType.setSchedulingPeriod("1 min");
    processorType.setSchedulingStrategy(SchedulingStrategy.PRIMARY_NODE_ONLY);
    processorType.setExecutionNode(ExecutionNode.ALL);

    NifiProcessorsGenerator nifiProcessorsGenerator = new NifiProcessorsGenerator(processorType);
    String actual = nifiProcessorsGenerator.generateTFElement("PROCESSOR", parentGroupId);

    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_processor.tf").getFile());

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

  @Test
  public void testGenerateTFElementNoPropertyAndRelationship()
      throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ProcessorType processorType = new ProcessorType();

    processorType.setName("TEST_PROCR");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    processorType.setPosition(positionType);
    processorType.setClazz(ProcessorType.class.getName());
    processorType.setMaxConcurrentTasks(BigInteger.valueOf(100));
    processorType.setSchedulingPeriod("1 min");
    processorType.setSchedulingStrategy(SchedulingStrategy.PRIMARY_NODE_ONLY);
    processorType.setExecutionNode(ExecutionNode.ALL);

    NifiProcessorsGenerator nifiProcessorsGenerator = new NifiProcessorsGenerator(processorType);
    String actual = nifiProcessorsGenerator.generateTFElement("PROCESSOR", parentGroupId);
    String[] actualarray = actual.split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_processor2.tf").getFile());

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
