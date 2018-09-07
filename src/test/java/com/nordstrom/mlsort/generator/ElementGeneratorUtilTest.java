package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.LabelType;
import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.ProcessGroupType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.RemoteProcessGroupType;
import com.nordstrom.mlsort.jaxb.ReportingTaskType;
import com.nordstrom.mlsort.jaxb.ReportingTasksType;
import com.nordstrom.mlsort.jaxb.RootGroupPortType;
import com.nordstrom.mlsort.jaxb.ScheduledState;
import com.nordstrom.mlsort.jaxb.SchedulingStrategy;

/**
 * Test ElementGeneratorUtil
 */
public class ElementGeneratorUtilTest {

  @Test
  public void testGenerateTFForProcessGroups() throws IOException {
    String parentGroupId = "aaaa-bbbb-cccc-dddd";

    ProcessGroupType processGroupType = new ProcessGroupType();
    processGroupType.setId("bbbb-cccc-dddd-eeee");
    processGroupType.setName("TEST_PROCS_GRP");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    processGroupType.setPosition(positionType);

    List<ConnectionType> connections = NifiProcessGroupsGeneratorTest.getConnections();
    List<PortType> outputPorts = NifiProcessGroupsGeneratorTest.getOutputPorts();
    List<PortType> inputPorts = NifiProcessGroupsGeneratorTest.getInputPorts();
    List<ProcessorType> processors = NifiProcessGroupsGeneratorTest.getProcessors();
    List<ControllerServiceType> controllerServiceList =
        NifiProcessGroupsGeneratorTest.getControllerServices();
    List<FunnelType> funnels = NifiProcessGroupsGeneratorTest.getFunnels();
    List<LabelType> labels = NifiProcessGroupsGeneratorTest.getLabels();
    List<ProcessGroupType> processGroupList = NifiProcessGroupsGeneratorTest.getProcessGroups();

    processGroupType.getConnection().addAll(connections);
    processGroupType.getInputPort().addAll(inputPorts);
    processGroupType.getOutputPort().addAll(outputPorts);
    processGroupType.getProcessor().addAll(processors);
    processGroupType.getControllerService().addAll(controllerServiceList);
    processGroupType.getFunnel().addAll(funnels);
    processGroupType.getLabel().addAll(labels);
    processGroupType.getProcessGroup().addAll(processGroupList);

    List<ProcessGroupType> processGroupTypeList = new ArrayList<>();
    processGroupTypeList.add(processGroupType);
    boolean isParent = true;
    List<String> childHolderList = new ArrayList<>();
    Map<String, String> map = new HashMap<>();
    ElementGeneratorUtil.generateTFForProcessGroups(processGroupTypeList, parentGroupId, map,
        isParent, childHolderList);
    /*
     * ClassLoader classLoader = getClass().getClassLoader(); File file = new
     * File(classLoader.getResource("generator/final.tf").getFile()); String expected =
     * FileUtils.readFileToString(file);
     */
    assertEquals("Failed to generate process group", map.values().size(), 1);
  }

  @Test
  public void testSeparateCommonElements() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_funnel.tf").getFile());
    String funnel = FileUtils.readFileToString(file);
    Map<String, String> map = new HashMap<>();
    map.put("A", funnel);
    map.put("B", funnel);
    Set<String> set = ElementGeneratorUtil.separateCommonElements(map);
    assertEquals("Failed to remove duplicate", set.size(), 1);
    assertEquals("Failed to remove duplicate", set.iterator().next().trim(), funnel.trim());
  }

  @Test
  public void testSeparateRemoteConnections() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file =
        new File(classLoader.getResource("generator/nifi_remote_connection_gen.tf").getFile());
    String remoteProcessGroup = FileUtils.readFileToString(file);
    File leftoutfile = new File(classLoader.getResource("generator/nifi_funnel.tf").getFile());
    String leftout = FileUtils.readFileToString(leftoutfile);
    assertEquals("Failed to separate remote connections",
        ElementGeneratorUtil.separateRemoteConnections(remoteProcessGroup).trim(), leftout.trim());
  }

  @Test
  public void testExtractElementsAsSet() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file =
        new File(classLoader.getResource("generator/nifi_controller_service_gen.tf").getFile());
    String finalTerraformScriptParent = FileUtils.readFileToString(file);
    Set<String> set = new HashSet<>();
    StringBuilder finalTFHolder = new StringBuilder();
    ElementGeneratorUtil.extractElementsAsSet(finalTerraformScriptParent, set, finalTFHolder);
    assertEquals("Failed to extract element as a set", set.size(), 1);
    assertEquals("Failed to extract element as a set", set.iterator().next().trim(),
        finalTerraformScriptParent.trim());
  }

  @Test
  public void testReplaceLeftoutParentGroupIdWithRootProcessGroups() throws IOException {
    String tf = "\t\tparent_group_id = \"aaaa-bbbb-cccc-dddd";
    String actual = ElementGeneratorUtil.replaceLeftoutParentGroupIdWithRootProcessGroups(tf);
    assertEquals("Failed to replace let out parent group id with root process group",
        "\t\tparent_group_id = \"${var.nifi_root_process_group_id}\"", actual);
  }

  @Ignore
  public void testGetControllerServices() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file =
        new File(classLoader.getResource("generator/nifi_controller_service_gen.tf").getFile());
    String finalTerraformScriptParent = FileUtils.readFileToString(file);
    Map<String, String> map =
        ElementGeneratorUtil.getControllerServices(finalTerraformScriptParent);
    for (Entry<String, String> entry : map.entrySet()) {
      // assertEquals("CTRL_SER", entry.getKey());
      System.out.println("${nifi_controller_service.ControllerServices_CTRL_SER.id}");
      System.out.println(entry.getValue());
      assertEquals("Failed to get controller services",
          "${nifi_controller_service.ControllerServices_CTRL_SER.id}", entry.getValue());

    }
  }

  @Test
  public void testGenerateTFForPortsForRoot() throws IOException {
    List<RootGroupPortType> portsList = new ArrayList<>();
    RootGroupPortType rootGroupPortType = new RootGroupPortType();
    rootGroupPortType.setComments("PRT_CMD");
    rootGroupPortType.setId("aaaa-bbbb-cccc-dddd");
    rootGroupPortType.setName("PRT_ROOT");
    rootGroupPortType.setMaxConcurrentTasks(BigInteger.valueOf(100));
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    rootGroupPortType.setPosition(positionType);
    rootGroupPortType.setScheduledState(ScheduledState.RUNNING);
    rootGroupPortType.setVersionedComponentId("aaaa-bbbb-cccc-dddd");
    portsList.add(rootGroupPortType);
    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    StringBuilder sb = ElementGeneratorUtil.generateTFForPortsForRoot(portsList, parentGroupId,
        ElementGeneratorUtil.INPUT_PORT);

    String[] actualarray = sb.toString().split("\n");
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_port_gen.tf").getFile());

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

    assertTrue("Response not matching the Expected string", comparison);
  }

  @Test
  public void testGenerateTFForReportingTasks() throws IOException {
    ReportingTasksType reportingTasksType = new ReportingTasksType();
    ReportingTaskType reportingTaskType = new ReportingTaskType();
    reportingTasksType.getReportingTask().add(reportingTaskType);
    reportingTaskType.setAnnotationData("RPT TSK ANNO");
    reportingTaskType.setName("RPT_TSK_NM");
    reportingTaskType.setClazz(ReportingTaskType.class.getName());
    reportingTaskType.setSchedulingPeriod("1 min");
    reportingTaskType.setSchedulingStrategy(SchedulingStrategy.PRIMARY_NODE_ONLY);

    PropertyType propertyType = new PropertyType();
    propertyType.setName("PROP_NAME");
    propertyType.setValue("PROP_VALUE");
    reportingTaskType.getProperty().add(propertyType);
    StringBuilder sb = ElementGeneratorUtil.generateTFForReportingTasks(reportingTasksType);
    String[] actualarray = sb.toString().split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("generator/nifi_reporting_task_gen.tf").getFile());

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

    assertTrue("Response not matching the Expected string", comparison);
  }

  @Test
  public void testGenerateTFForRemoteProcessGroups() throws IOException {
    List<RemoteProcessGroupType> remoteProcessGroupList = new ArrayList<>();
    RemoteProcessGroupType remoteProcessGroupType = new RemoteProcessGroupType();
    remoteProcessGroupList.add(remoteProcessGroupType);
    remoteProcessGroupType.setComment("RPG COMMENT");
    remoteProcessGroupType.setId("aaaa-bbbb-cccc-dddd");
    remoteProcessGroupType.setName("RPG_NAME");
    remoteProcessGroupType.setNetworkInterface("NET INT");
    PositionType positionType = new PositionType();
    positionType.setX(100);
    positionType.setY(100);
    remoteProcessGroupType.setPosition(positionType);
    remoteProcessGroupType.setProxyHost("PRXT HST");
    remoteProcessGroupType.setProxyPassword("PRXY PWD");
    remoteProcessGroupType.setProxyPort(8080);
    remoteProcessGroupType.setProxyUser("PRXY USR");
    remoteProcessGroupType.setTimeout("1 min");
    remoteProcessGroupType.setTransmitting(true);
    remoteProcessGroupType.setTransportProtocol("TLS");
    remoteProcessGroupType.setUrl("http://url");
    remoteProcessGroupType.setUrls("http://urls");
    remoteProcessGroupType.setVersionedComponentId("aaaa-bbbb-cccc-dddd");
    remoteProcessGroupType.setYieldPeriod("1 min");
    String parentGroupId = "aaaa-bbbb-cccc-dddd";
    StringBuilder sb = ElementGeneratorUtil.generateTFForRemoteProcessGroups(remoteProcessGroupList,
        parentGroupId);
    String[] actualarray = sb.toString().split("\n");

    ClassLoader classLoader = getClass().getClassLoader();
    File file =
        new File(classLoader.getResource("generator/nifi_remote_process_group_gen.tf").getFile());

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

    assertTrue("Response not matching the Expected string", comparison);
  }

  @Test
  public void testGetReferenceValueToReplace() {
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("CON_XXX")
        .contains("nifi_connection.CON_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("CSR_XXX")
        .contains("nifi_controller_service.CSR_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("PGP_XXX")
        .contains("nifi_process_group.PGP_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("PRT_XXX")
        .contains("nifi_port.PRT_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("PCR_XXX")
        .contains("nifi_processor.PCR_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("FNL_XXX")
        .contains("nifi_funnel.FNL_XXX.id"));
    assertTrue(ElementGeneratorUtil.getReferenceValueToReplace("RPG_XXX")
        .contains("nifi_remote_process_group.RPG_XXX.id"));
    assertTrue("Failed to get reference value to replace", ElementGeneratorUtil
        .getReferenceValueToReplace("RPT_XXX").contains("nifi_reporting_task.RPT_XXX.id"));
  }

  @Test
  public void testGetProperty() throws IOException {
    System.out.println(ElementGeneratorUtil.getProperty("nifi_machine"));
    assertTrue("Failed to get property",
        "localhost:8080".equals(ElementGeneratorUtil.getProperty("nifi_machine")));
  }

}
