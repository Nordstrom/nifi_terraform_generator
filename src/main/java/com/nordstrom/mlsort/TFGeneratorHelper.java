package com.nordstrom.mlsort;

import static com.nordstrom.mlsort.TFGeneratorHelperUtil.generateMainAndVariablesTF;
import static com.nordstrom.mlsort.TFGeneratorHelperUtil.generateParentElements;
import static com.nordstrom.mlsort.TFGeneratorHelperUtil.parseFlowXml;
import static com.nordstrom.mlsort.TFGeneratorHelperUtil.writeCommonElements;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import com.nordstrom.mlsort.generator.ElementGeneratorUtil;
import com.nordstrom.mlsort.jaxb.FlowControllerType;
import com.nordstrom.mlsort.jaxb.RootProcessGroupType;

/**
 * Helper class for the operations in main class - TFGenerator
 *
 */
public class TFGeneratorHelper {

  /**
   * Starting execution point to convert xml to terraform.
   * 
   * @param args String[]
   * @throws URISyntaxException URISyntaxException
   * @throws IOException IOException
   * @throws JAXBException JAXBException
   */
  public static void executeMainActions(final String[] args)
      throws URISyntaxException, IOException, JAXBException {

    String nifiMachine = null;
    String rootElementId = null;
    if (args.length >= 2) {
      nifiMachine = args[0];
      rootElementId = args[1];
    } else {
      // To get the data from properties
      nifiMachine = ElementGeneratorUtil.getProperty("nifi_machine");
      rootElementId = ElementGeneratorUtil.getProperty("root_element");
    }

    if (StringUtils.isNotBlank(nifiMachine) && StringUtils.isNotBlank(rootElementId)) {
      String xmlFilePath = "";
      if (args.length == 3) {
        xmlFilePath = args[2];
      }
      // Get the JAXB element by parsing the flow xml
      JAXBElement<FlowControllerType> root = parseFlowXml(xmlFilePath);
      FlowControllerType flowController = root.getValue();
      Map<String, String> tfNameContentMap = new LinkedHashMap<>();
      // To hold all the parent terraform elements
      StringBuilder parentElementsScript = new StringBuilder();
      if (null != flowController) {
        RootProcessGroupType rootProcessGroup = flowController.getRootGroup();
        // Need to implement later
        flowController.getControllerServices();
        if (null != rootProcessGroup) {
          // Generate Process Groups tf(the elements are populated on map,
          // one for each process group
          if (null != rootProcessGroup.getProcessGroup()) {
            List<String> childHolderList = new ArrayList<>();
            ElementGeneratorUtil.generateTFForProcessGroups(rootProcessGroup.getProcessGroup(),
                rootProcessGroup.getId(), tfNameContentMap, true, childHolderList);
          }
          generateParentElements(parentElementsScript, rootProcessGroup);
        }
        // Generate Reporting Tasks
        if (null != flowController.getReportingTasks()) {
          parentElementsScript.append(
              ElementGeneratorUtil.generateTFForReportingTasks(flowController.getReportingTasks()));
        }
      }

      // To replace the actual id of elements with terraform reference
      Map<String, String> ID_NAME_MAP_TO_REPLACE = new HashMap<>();
      for (Entry<String, String> mappingEntry : ElementGeneratorUtil.ID_NAME_MAP.entrySet()) {
        ID_NAME_MAP_TO_REPLACE.put("\"" + mappingEntry.getKey() + "\"",
            ElementGeneratorUtil.getReferenceValueToReplace(mappingEntry.getValue()));
      }

      // Convert the generated script into a proper terraform file (parent
      // elements)
      String parentElementsScriptStr = parentElementsScript.toString();
      for (Entry<String, String> replacerMapNew : ID_NAME_MAP_TO_REPLACE.entrySet()) {
        parentElementsScriptStr =
            parentElementsScriptStr.replace(replacerMapNew.getKey(), replacerMapNew.getValue());
      }

      String finalTerraformScriptParent = ElementGeneratorUtil
          .generateFinalTFScript(new StringBuilder(parentElementsScriptStr), rootElementId);

      // Method to generate main.tf and variables.tf
      generateMainAndVariablesTF(rootElementId, nifiMachine, finalTerraformScriptParent);

      // Find all ControllerServices
      Map<String, String> controllerServices =
          ElementGeneratorUtil.getControllerServices(finalTerraformScriptParent);

      // The possibility of common elements is very less, being on a safer side
      writeCommonElements(rootElementId, tfNameContentMap, ID_NAME_MAP_TO_REPLACE,
          controllerServices);

      System.out.println("The output successfully generated at target folder");
    } else {
      throw new IOException(
          "Please provide a valid root_element and nifi_machine in the properties file");
    }
  }

}
