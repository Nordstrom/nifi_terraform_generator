package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.BendPointsType;
import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.ExecutionNode;
import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.LabelType;
import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.ProcessGroupType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.SchedulingStrategy;

/**
 * Test class for NifiProcessorsGenerator
 */
public class NifiProcessGroupsGeneratorTest {

  @Test
  public void testGenerateTFElement() throws IOException, URISyntaxException {

    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    ProcessGroupType processGroupType = new ProcessGroupType();
    processGroupType.setId("bbbb-cccc-dddd-eeee");
    processGroupType.setName("TEST_PROCS_GRP");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    processGroupType.setPosition(positionType);

    List<ConnectionType> connections = getConnections();
    List<PortType> outputPorts = getOutputPorts();
    List<PortType> inputPorts = getInputPorts();
    List<ProcessorType> processors = getProcessors();
    List<ControllerServiceType> controllerServiceList = getControllerServices();
    List<FunnelType> funnels = getFunnels();
    List<LabelType> labels = getLabels();
    List<ProcessGroupType> processGroupList = getProcessGroups();

    Map<String, String> map = new HashMap<>();
    boolean isParent = true;
    List<String> childHolderList = new ArrayList<>();
    NifiProcessGroupsGenerator nifiProcessGroupsGenerator =
        new NifiProcessGroupsGenerator(processGroupType, parentGroupId, map, isParent,
            childHolderList).withConnections(connections).withOutputPorts(outputPorts)
                .withInputPorts(inputPorts).withProcessors(processors)
                .withControllerServices(controllerServiceList).withFunnels(funnels)
                .withLabels(labels).withProcessGroups(processGroupList).build();
    String actual = nifiProcessGroupsGenerator.generateTFElement("PROCESS_GROUP", parentGroupId);

    String[] actualarray = actual.split("\n");
    List<String> actualList = new ArrayList<>();
    for (String string : actualarray) {
      if (StringUtils.isNotBlank(string)) {
        actualList.add(string);
      }
    }

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_process_group.tf").getFile());

    String expected = FileUtils.readFileToString(file);

    String[] expectedarray = expected.split("\n");
    List<String> expectedList = new ArrayList<>();
    for (String string : expectedarray) {
      if (StringUtils.isNotBlank(string)) {
        expectedList.add(string);
      }
    }

    boolean comparison = actualList.size() == expectedList.size();
    for (int i = 0; i < expectedList.size(); i++) {
      if (!comparison) {
        break;
      }
      String expectedrow = expectedList.get(i);
      String actualrow = actualList.get(i);
      if (!(actualrow.contains("nifi_funnel") || expectedrow.contains("nifi_funnel"))) {
        comparison = expectedrow.trim().equals(actualrow.trim());
      }
    }

    assertTrue("Response id not matching the Expected string", comparison);
  }

  public static List<ProcessGroupType> getProcessGroups() {
    List<ProcessGroupType> list = new ArrayList<>();
    return list;
  }

  public static List<LabelType> getLabels() {
    LabelType labelType = new LabelType();
    List<LabelType> list = new ArrayList<>();
    list.add(labelType);
    return list;
  }

  public static List<FunnelType> getFunnels() {
    FunnelType funnelType = new FunnelType();
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    funnelType.setPosition(positionType);
    List<FunnelType> list = new ArrayList<>();
    list.add(funnelType);
    return list;
  }

  public static List<ControllerServiceType> getControllerServices() {
    ControllerServiceType controllerServiceType = new ControllerServiceType();

    controllerServiceType.setName("CSR_NAME");
    controllerServiceType.setClazz(ControllerServiceType.class.getName());

    PropertyType propertyType = new PropertyType();
    propertyType.setName("PROP_NAME");
    propertyType.setValue("PROP_VALUE");
    controllerServiceType.getProperty().add(propertyType);
    List<ControllerServiceType> list = new ArrayList<>();
    list.add(controllerServiceType);
    return list;
  }

  public static List<ProcessorType> getProcessors() {
    ProcessorType processorType = new ProcessorType();
    processorType.setName("PCR_NAME");
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
    List<ProcessorType> list = new ArrayList<>();
    list.add(processorType);
    return list;
  }

  public static List<PortType> getInputPorts() {
    PortType portType = new PortType();

    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    portType.setPosition(positionType);
    portType.setName("PRT_TYP_NAME");
    List<PortType> list = new ArrayList<>();
    list.add(portType);
    return list;
  }

  public static List<PortType> getOutputPorts() {
    return getInputPorts();
  }

  public static List<ConnectionType> getConnections() {
    ConnectionType connectionType = new ConnectionType();
    connectionType.setName("CON_NAME");
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
    List<ConnectionType> list = new ArrayList<>();
    list.add(connectionType);
    return list;
  }

}
