package com.nordstrom.mlsort.generator;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import com.nordstrom.mlsort.TFGenerator;
import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.LabelType;
import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.ProcessGroupType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.jaxb.RemoteProcessGroupType;
import com.nordstrom.mlsort.jaxb.ReportingTaskType;
import com.nordstrom.mlsort.jaxb.ReportingTasksType;
import com.nordstrom.mlsort.jaxb.RootGroupPortType;
import com.nordstrom.mlsort.tf.TFUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Utility class for modularization.
 *
 */
public final class ElementGeneratorUtil {

  public static final String ROOT_PROCESS_GROUP_ID = "\"${var.nifi_root_process_group_id}\"";
  public static final String INPUT_PORT = "INPUT_PORT";
  public static final String OUTPUT_PORT = "OUTPUT_PORT";
  public static final Map<String, String> ID_NAME_MAP = new HashMap<>();

  /**
   * Method to create tf script corresponding to Connections.
   * 
   * @param ConnectionsTypeList List<ConnectionType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForConnections(List<ConnectionType> ConnectionsTypeList,
      String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    for (ConnectionType connections : ConnectionsTypeList) {
      String name = generateElementName("Connections", connections.getName());
      connections.setName(name);
      ID_NAME_MAP.put(connections.getId(), name);
      NifiConnectionsGenerator nifiConnectionsGenerator = new NifiConnectionsGenerator(connections);

      String output = nifiConnectionsGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }

    return finalTF;
  }

  /**
   * Method to create tf script corresponding to ControllerServices.
   * 
   * @param controllerServicesTypeList List<ControllerServiceType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForControllerServices(
      List<ControllerServiceType> controllerServicesTypeList, String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    for (ControllerServiceType controllerServices : controllerServicesTypeList) {
      String name = generateElementName("ControllerServices", controllerServices.getName());
      ID_NAME_MAP.put(controllerServices.getId(), name);
      controllerServices.setName(name);
      NifiControllerServicesGenerator nifiControllerServicesGenerator =
          new NifiControllerServicesGenerator(controllerServices);

      String output = nifiControllerServicesGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }

    return finalTF;
  }

  /**
   * Method to create tf script corresponding to Process groups. Map is populated with key - tf file
   * name and value tf content
   * 
   * @param processGroupTypeList - List of process groups
   * @param parentGroupId - String
   * @param map - Map<String, String> to hold the response(key - tf file name, value tf content)
   * @param isParent - flag to indicate whether the execution is initiated by child or parent
   *        process
   * @param childHolderList - for holding the elements corresponding to child process groups of each
   *        parent process group
   */
  public static void generateTFForProcessGroups(List<ProcessGroupType> processGroupTypeList,
      String parentGroupId, Map<String, String> map, boolean isParent,
      List<String> childHolderList) {

    for (ProcessGroupType processGroup : processGroupTypeList) {
      String name = generateElementName("ProcessGroups", processGroup.getName());
      ID_NAME_MAP.put(processGroup.getId(), name);
      processGroup.setName(name);
      NifiProcessGroupsGenerator nifiProcessGroupGenerator = null;
      // Read all the child elements
      List<ConnectionType> connections = processGroup.getConnection();
      List<PortType> outputPorts = processGroup.getOutputPort();
      List<PortType> inputPorts = processGroup.getInputPort();
      List<ProcessorType> processors = processGroup.getProcessor();
      List<FunnelType> funnels = processGroup.getFunnel();
      List<LabelType> labels = processGroup.getLabel();
      List<ProcessGroupType> processGroupList = processGroup.getProcessGroup();
      List<ControllerServiceType> controllerServiceList = processGroup.getControllerService();

      nifiProcessGroupGenerator = new NifiProcessGroupsGenerator(processGroup, parentGroupId, map,
          isParent, childHolderList).withConnections(connections).withOutputPorts(outputPorts)
              .withInputPorts(inputPorts).withProcessors(processors)
              .withControllerServices(controllerServiceList).withFunnels(funnels).withLabels(labels)
              .withProcessGroups(processGroupList).build();

      String output = nifiProcessGroupGenerator.generateTFElement(name, parentGroupId);
      StringBuilder finalTF = new StringBuilder();
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
      // If parent add the child elements
      // to parent and create a combined
      // terraform file
      if (isParent) {
        for (String childPG : childHolderList) {
          finalTF.append(childPG);
        }

        map.put(processGroup.getName(), finalTF.toString());
        childHolderList = new ArrayList<>();
      } else {// if child as to List which will be
        // merged with parent later
        childHolderList.add(finalTF.toString());
      }
    }
  }

  /**
   * Method to generate terraform element name.
   * 
   * @param elementType String
   * @param name String
   * @return String - terraform element name
   */
  private static String generateElementName(final String elementType, final String name) {
    return getDynamicElementName(elementType, name);
  }

  /**
   * Method to generate a dynamic name for the first time.
   * 
   * @param elementType String
   * @param name String
   * @return String dynamic name
   */
  private static String getDynamicElementName(final String elementType, final String name) {
    String prefix = "";
    RandomStringGenerator randomStringGenerator =
        new RandomStringGenerator.Builder().withinRange('0', 'Z')
            .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();
    String dynamic = randomStringGenerator.generate(6);

    if (StringUtils.isNotBlank(name)) {
      dynamic += "_" + removeSpaceAndMakeNextCharUpperCase(name);
    }

    if (elementType.equals("Connections")) {
      prefix = "CON";
    } else if (elementType.equals("ControllerServices")) {
      prefix = "CSR";
    } else if (elementType.equals("ProcessGroups")) {
      prefix = "PGP";
    } else if (elementType.equals("Ports")) {
      prefix = "PRT";
    } else if (elementType.equals("Processors")) {
      prefix = "PCR";
    } else if (elementType.equals("Funnels")) {
      prefix = "FNL";
    } else if (elementType.equals("RemoteProcessGroups")) {
      prefix = "RPG";
    } else if (elementType.equals("ReportingTasks")) {
      prefix = "RPT";
    }

    if (name.startsWith(prefix)) {
      return name;
    }
    return prefix + "_" + dynamic;
  }

  /**
   * Method to remove the space in the sentence and make the first letter upper case.
   * 
   * @param sentenceCase String
   * @return String sentence without space
   */
  private static String removeSpaceAndMakeNextCharUpperCase(final String sentenceCase) {
    String out = "";
    for (String word : sentenceCase.split(" ")) {
      if (StringUtils.isNotBlank(word))
        out += word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    return out;
  }

  /**
   * Method to create tf script corresponding to INPUT, OUTPUT and REMOTE port.
   * 
   * @param portsList List<PortType>
   * @param parentGroupId String
   * @param portType String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForPorts(final List<PortType> portsList,
      final String parentGroupId, final String portType) {
    StringBuilder finalTF = new StringBuilder();
    for (PortType ports : portsList) {
      NifiPortsGenerator nifiPortsGenerator = new NifiPortsGenerator(ports);
      nifiPortsGenerator.setPortType(portType);
      String name = generateElementName("Ports", ports.getName());
      ID_NAME_MAP.put(ports.getId(), name);
      String output = nifiPortsGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }

    return finalTF;
  }

  /**
   * Method to create tf script corresponding to Root port.
   * 
   * @param portsList List<RootGroupPortType>
   * @param parentGroupId String
   * @param portType String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForPortsForRoot(final List<RootGroupPortType> portsList,
      final String parentGroupId, final String portType) {
    StringBuilder finalTF = new StringBuilder();
    for (RootGroupPortType ports : portsList) {
      String name = generateElementName("Ports", ports.getName());
      ports.setName(name);
      ID_NAME_MAP.put(ports.getId(), name);
      NifiPortsGenerator nifiPortsGenerator = new NifiPortsGenerator(ports);
      nifiPortsGenerator.setPortType(portType);

      String output = nifiPortsGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }

    return finalTF;
  }

  /**
   * Method to create tf script corresponding to Processors.
   * 
   * @param processorsTypeList List<ProcessorType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForProcessors(final List<ProcessorType> processorsTypeList,
      final String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    for (ProcessorType processors : processorsTypeList) {
      String name = generateElementName("Processors", processors.getName());
      processors.setName(name);
      ID_NAME_MAP.put(processors.getId(), name);
      NifiProcessorsGenerator nifiProcessorGenerator = new NifiProcessorsGenerator(processors);
      String output = nifiProcessorGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }

    return finalTF;
  }

  /**
   * Method to create tf script corresponding to Funnels.
   * 
   * @param funnelsTypeList List<FunnelType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForFunnels(final List<FunnelType> funnelsTypeList,
      final String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    for (FunnelType funnel : funnelsTypeList) {
      String name = generateElementName("Funnels", "");
      NifiFunnelGenerator nifiFunnelGenerator = new NifiFunnelGenerator(funnel);
      ID_NAME_MAP.put(funnel.getId(), name);
      String output = nifiFunnelGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }
    return finalTF;
  }

  /**
   * Method to create tf script corresponding to Labels. Will implement after support added on
   * terraform-nifi-provider.
   * 
   * @param labelsTypeList List<LabelType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static Object generateTFForLabels(final List<LabelType> labelsTypeList,
      final String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    // TODO Label is currently not supported
    return finalTF;
  }

  /**
   * Does all the massaging required to generate the final tf file. Actions are explained on method
   * calls.
   * 
   * @param terraformScript StringBuilder
   * @param rootElementId String
   * @return String - final terraform script
   * @throws IOException IOException
   */
  public static String generateFinalTFScript(final StringBuilder terraformScript,
      final String rootElementId) throws IOException {
    // To replace left out ids with root process group id
    String tfWithRootProcessGroups =
        replaceLeftoutParentGroupIdWithRootProcessGroups(terraformScript.toString());

    // To separate remote connections in terraform
    String finalTF = separateRemoteConnections(tfWithRootProcessGroups);

    return finalTF;
  }

  /**
   * In case some ids could not be mapped with the corresponding terraform element, it might be
   * referring to the root process group. This method identified all such elements and replaces them
   * with root process group mapping.
   * 
   * @param terraformScript String
   * @return String - modified terraformScript
   */
  public static String replaceLeftoutParentGroupIdWithRootProcessGroups(String terraformScript) {
    // Read the file line by line
    String[] scriptLines = terraformScript.split("\n");
    Set<String> parentGroupidLines = new HashSet<>();
    for (String scriptLine : scriptLines) {
      if (scriptLine.startsWith("\t\tparent_group_id = \"")) {
        if (!scriptLine.startsWith("\t\tparent_group_id = \"${")) {
          String untrackedParentId = scriptLine.substring("\t\tparent_group_id = ".length());
          parentGroupidLines.add(untrackedParentId);
        }
      }
    }
    for (String parentGroupidLine : parentGroupidLines) {
      terraformScript = terraformScript.replace(parentGroupidLine, ROOT_PROCESS_GROUP_ID);
    }

    return terraformScript;
  }

  /**
   * Duplicate elements(if any) are removed. The connections corresponding to remote process groups
   * are separated and stored in remoteconnections.tf. This is a temporary action and will be
   * removed once the support is added for remote port connections.
   * 
   * @param tf String
   * @return String terraform script with unique elements
   * @throws IOException IOException
   */
  public static String separateRemoteConnections(final String tf) throws IOException {

    Set<String> set = new HashSet<>();
    StringBuilder finalTFHolder = new StringBuilder();

    extractElementsAsSet(tf, set, finalTFHolder);
    StringBuilder remoteConnections = new StringBuilder();
    for (String string : set) {
      // Additional step to remove REMOTE_INPUT_PORT and
      // REMOTE_OUTPUT_PORT connections
      if (string.contains("resource \"nifi_connection\" \"CON_")) {
        if (string.contains("type = \"REMOTE_OUTPUT_PORT\"")
            || string.contains("type = \"REMOTE_INPUT_PORT\"")) {
          remoteConnections.append(TFUtil.NEWLINE).append(string).append(TFUtil.NEWLINE);
        } else {
          finalTFHolder.append(TFUtil.NEWLINE).append(string).append(TFUtil.NEWLINE);
        }
      } else {
        finalTFHolder.append(TFUtil.NEWLINE).append(string).append(TFUtil.NEWLINE);
      }

    }

    String path = TFGenerator.class.getResource("/").getFile();
    // Write remote connections to a separate file
    if (remoteConnections.length() > 0) {
      File remoteConnectionsFile = new File(path + "../remoteconnections.tf");
      FileUtils.writeStringToFile(remoteConnectionsFile, remoteConnections.toString());
    }
    return finalTFHolder.toString();
  }

  /**
   * Convert the terraform file into a Set of independent tf elements, just to make sure duplicate
   * are removed.
   * 
   * @param tf String
   * @param set Set<String>
   * @param finalTFHolder StringBuilder (to capture the non resource elements)
   */
  public static void extractElementsAsSet(final String tf, final Set<String> set,
      final StringBuilder finalTFHolder) {
    String[] scriptLines = tf.split("\n");
    StringBuilder elementHolder = null;
    for (String scriptLine : scriptLines) {
      if (scriptLine.startsWith("resource \"")) {
        // Clear StringBuilder
        if (null != elementHolder && elementHolder.length() > 0) {
          set.add(StringUtils.trim(elementHolder.toString()));
        }
        elementHolder = new StringBuilder();
        elementHolder.append(scriptLine).append(TFUtil.NEWLINE);
      } else {
        if (null != elementHolder) {
          elementHolder.append(scriptLine).append(TFUtil.NEWLINE);
        } else {
          finalTFHolder.append(scriptLine).append(TFUtil.NEWLINE);
        }
      }
    }
    if (null != elementHolder) {
      set.add(StringUtils.trim(elementHolder.toString()));
    }
  }

  /**
   * Generate terraform script corresponding to ReportingTasks.
   * 
   * @param reportingTasks ReportingTasksType
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForReportingTasks(final ReportingTasksType reportingTasks) {
    StringBuilder finalTF = new StringBuilder();
    for (ReportingTaskType reportingTask : reportingTasks.getReportingTask()) {
      String name = generateElementName("ReportingTasks", reportingTask.getName());
      ID_NAME_MAP.put(reportingTask.getId(), name);
      reportingTask.setName(name);
      NifiReportingTasksGenerator nifiReportingTasksGenerator =
          new NifiReportingTasksGenerator(reportingTask);

      String output = nifiReportingTasksGenerator.generateTFElement(name, ROOT_PROCESS_GROUP_ID);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }
    return finalTF;
  }

  /**
   * Generate terraform script corresponding to RemoteProcessGroups.
   * 
   * @param remoteProcessGroupList ReportingTasksType List<RemoteProcessGroupType>
   * @param parentGroupId String
   * @return StringBuilder - the tf script
   */
  public static StringBuilder generateTFForRemoteProcessGroups(
      final List<RemoteProcessGroupType> remoteProcessGroupList, final String parentGroupId) {
    StringBuilder finalTF = new StringBuilder();
    for (RemoteProcessGroupType remoteProcessGroup : remoteProcessGroupList) {
      String name = generateElementName("RemoteProcessGroups", remoteProcessGroup.getName());
      remoteProcessGroup.setName(name);
      ID_NAME_MAP.put(remoteProcessGroup.getId(), name);
      NifiRemoteProcessGroupsGenerator nifiRemoteProcessGroupsGenerator =
          new NifiRemoteProcessGroupsGenerator(remoteProcessGroup);
      String output = nifiRemoteProcessGroupsGenerator.generateTFElement(name, parentGroupId);
      finalTF.append(output).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    }
    return finalTF;
  }

  /**
   * Create a map with key as the ControllerServices Id and value as the corresponding tf element
   * reference
   * 
   * @param finalTerraformScriptParent String
   * @return Map<String, String> - id - tf reference map
   */
  public static Map<String, String> getControllerServices(final String finalTerraformScriptParent) {
    Map<String, String> map = new HashMap<>();
    String[] scriptLines = finalTerraformScriptParent.split("\n");
    String prefix = "resource \"nifi_controller_service\" \"ControllerServices_";
    for (String scriptLine : scriptLines) {
      if (scriptLine.startsWith(prefix)) {
        String id = scriptLine.substring(prefix.length(), scriptLine.length() - 3).trim();
        String value = "${nifi_controller_service.ControllerServices_" + id + ".id}";
        map.put(id, value);
      }
    }
    return map;
  }

  /**
   * Method to separate common elements from the map values(tf scripts) and return as a Set.
   * 
   * @param map Map<String, String>
   * @return Set<String> - common elements
   */
  public static Set<String> separateCommonElements(final Map<String, String> map) {
    Set<String> commonElementsSet = new HashSet<>();
    Map<String, Integer> elementCountMap = new HashMap<>();
    for (Entry<String, String> mapEntry : map.entrySet()) {
      String content = mapEntry.getValue();
      Set<String> set = new HashSet<>();
      StringBuilder finalTFHolder = new StringBuilder();
      extractElementsAsSet(content, set, finalTFHolder);
      if (finalTFHolder.length() > 0) {
        commonElementsSet.add(finalTFHolder.toString());
      }

      for (String element : set) {
        if (elementCountMap.containsKey(element)) {
          Integer count = elementCountMap.get(element);
          ++count;
          elementCountMap.put(element, count);
        } else {
          elementCountMap.put(element, new Integer(1));
        }
      }

    }

    for (Entry<String, Integer> elementCountEntry : elementCountMap.entrySet()) {
      if (elementCountEntry.getValue() > 1) {
        commonElementsSet.add(elementCountEntry.getKey());
      }
    }
    return commonElementsSet;

  }


  /**
   * Method to get reference variables.
   * 
   * @param value String
   * @return String - reference variable
   */
  public static String getReferenceValueToReplace(final String value) {
    String type = null;
    if (value.startsWith("CON")) {
      type = "nifi_connection";
    } else if (value.startsWith("CSR")) {
      type = "nifi_controller_service";
    } else if (value.startsWith("PGP")) {
      type = "nifi_process_group";
    } else if (value.startsWith("PRT")) {
      type = "nifi_port";
    } else if (value.startsWith("PCR")) {
      type = "nifi_processor";
    } else if (value.startsWith("FNL")) {
      type = "nifi_funnel";
    } else if (value.startsWith("RPG")) {
      type = "nifi_remote_process_group";
    } else if (value.startsWith("RPT")) {
      type = "nifi_reporting_task";
    }
    return "\"${" + type + "." + value + ".id}\"";

  }

  /**
   * Method to get the property from configuration file.
   * 
   * @param propertyKey String
   * @return String - value of property
   */
  public static String getProperty(final String propertyKey) {
    Properties prop = new Properties();
    InputStream input = null;
    String propertyValue = null;
    try {
      // load a properties file
      input = TFGenerator.class.getClassLoader().getResourceAsStream("config.properties");
      prop.load(input);
      propertyValue = prop.getProperty(propertyKey);

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return propertyValue;
  }

}
