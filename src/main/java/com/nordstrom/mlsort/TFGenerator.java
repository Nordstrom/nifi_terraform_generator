/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import com.nordstrom.mlsort.generator.ElementGeneratorUtil;
import com.nordstrom.mlsort.generator.VariablesTFGenerator;
import com.nordstrom.mlsort.jaxb.FlowControllerType;
import com.nordstrom.mlsort.jaxb.RootProcessGroupType;
import com.nordstrom.mlsort.tf.TFUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;



/**
 * Main classfrom where execution starts.
 *
 */
public class TFGenerator {

  private static final String NIFI_HOST = "${var.nifi_host}";

  /**
   * Starting execution point to convert xml to terraform.
   * 
   * @param args String[]
   * @throws URISyntaxException URISyntaxException
   * @throws IOException IOException
   * @throws JAXBException JAXBException
   */
  public static void main(final String[] args)
      throws URISyntaxException, IOException, JAXBException {

    String rootElementId = ElementGeneratorUtil.getProperty("root_element");
    String nifiMachine = ElementGeneratorUtil.getProperty("nifi_machine");

    if (StringUtils.isNotBlank(nifiMachine) && StringUtils.isNotBlank(rootElementId)) {

      // Parse xml start
      JAXBContext jaxbContext = JAXBContext.newInstance(FlowControllerType.class);
      InputStream inputStream = TFGenerator.class.getResourceAsStream("/flowfiles/flow.xml");
      Source source = new StreamSource(inputStream);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      JAXBElement<FlowControllerType> root = (JAXBElement<FlowControllerType>) jaxbUnmarshaller
          .unmarshal(source, FlowControllerType.class);
      // Parse xml finish
      FlowControllerType flowController = root.getValue();

      StringBuilder parentElementsScript = new StringBuilder();

      Map<String, String> map = new LinkedHashMap<>();

      if (null != flowController) {

        RootProcessGroupType rootProcessGroup = flowController.getRootGroup();

        // Need to implement later
        flowController.getControllerServices();

        if (null != rootProcessGroup) {

          // Generate Process Groups tf(the elements are populated on
          // map,
          // one for each process group
          if (null != rootProcessGroup.getProcessGroup()) {
            List<String> childHolderList = new ArrayList<>();
            ElementGeneratorUtil.generateTFForProcessGroups(rootProcessGroup.getProcessGroup(),
                rootProcessGroup.getId(), map, true, childHolderList);
          }

          // Generate Connections tf
          if (null != rootProcessGroup.getConnection()) {
            parentElementsScript.append(ElementGeneratorUtil.generateTFForConnections(
                rootProcessGroup.getConnection(), rootProcessGroup.getId()));
          }

          // Generate InputPorts tf
          if (null != rootProcessGroup.getInputPort()) {
            parentElementsScript.append(
                ElementGeneratorUtil.generateTFForPortsForRoot(rootProcessGroup.getInputPort(),
                    rootProcessGroup.getId(), ElementGeneratorUtil.INPUT_PORT));
          }

          // Generate OutputPorts tf
          if (null != rootProcessGroup.getOutputPort()) {
            parentElementsScript.append(
                ElementGeneratorUtil.generateTFForPortsForRoot(rootProcessGroup.getOutputPort(),
                    rootProcessGroup.getId(), ElementGeneratorUtil.OUTPUT_PORT));
          }

          // Generate Processors tf
          if (null != rootProcessGroup.getProcessor()) {
            parentElementsScript.append(ElementGeneratorUtil.generateTFForProcessors(
                rootProcessGroup.getProcessor(), rootProcessGroup.getId()));
          }

          // Generate Funnel tf
          if (null != rootProcessGroup.getFunnel()) {
            parentElementsScript.append(ElementGeneratorUtil
                .generateTFForFunnels(rootProcessGroup.getFunnel(), rootProcessGroup.getId()));
          }

          // Generate Remote Process Group tf
          if (null != rootProcessGroup.getRemoteProcessGroup()) {
            parentElementsScript.append(ElementGeneratorUtil.generateTFForRemoteProcessGroups(
                rootProcessGroup.getRemoteProcessGroup(), rootProcessGroup.getId()));
          }

          // Generate Controller Service tf
          if (null != rootProcessGroup.getControllerService()) {
            parentElementsScript.append(ElementGeneratorUtil.generateTFForControllerServices(
                rootProcessGroup.getControllerService(), rootProcessGroup.getId()));
          }

        }

        // Generate Reporting Tasks
        if (null != flowController.getReportingTasks()) {
          parentElementsScript.append(
              ElementGeneratorUtil.generateTFForReportingTasks(flowController.getReportingTasks()));
        }

      }

      // To replace the actual if of elements with terraform reference
      Map<String, String> ID_NAME_MAP_TO_REPLACE = new HashMap<>();

      for (Entry<String, String> mappingEntry : ElementGeneratorUtil.ID_NAME_MAP.entrySet()) {
        ID_NAME_MAP_TO_REPLACE.put("\"" + mappingEntry.getKey() + "\"",
            ElementGeneratorUtil.getReferenceValueToReplace(mappingEntry.getValue()));
      }

      // Convert the generated script into a proper terraform file (parent
      // elements)

      // Combine child elements(process groups) and parent element to find
      // a
      // single replacer map
      List<String> childElementFromMap = new ArrayList<String>(map.values());
      childElementFromMap.add(parentElementsScript.toString());

      String parentElementsScriptStr = parentElementsScript.toString();
      for (Entry<String, String> replacerMapNew : ID_NAME_MAP_TO_REPLACE.entrySet()) {
        parentElementsScriptStr =
            parentElementsScriptStr.replace(replacerMapNew.getKey(), replacerMapNew.getValue());
      }

      String finalTerraformScriptParent = ElementGeneratorUtil
          .generateFinalTFScript(new StringBuilder(parentElementsScriptStr), rootElementId);

      // Create provider
      String provider = TFUtil.getProvider(NIFI_HOST);

      String path = TFGenerator.class.getResource("/").getFile();

      File parent = new File(path + "../main.tf");
      File variablesFile = new File(path + "../variables.tf");

      String variablesContent =
          VariablesTFGenerator.generateVariablesTF(nifiMachine, rootElementId);

      FileUtils.writeStringToFile(variablesFile, variablesContent);
      FileUtils.writeStringToFile(parent, provider + finalTerraformScriptParent);

      // Find all ControllerServices
      Map<String, String> controllerServices =
          ElementGeneratorUtil.getControllerServices(finalTerraformScriptParent);

      // Remove duplicate Elements in all values and make a new entry as a
      // shared tf
      Set<String> commonElements = ElementGeneratorUtil.separateCommonElements(map);

      for (Entry<String, String> mapEntry : map.entrySet()) {
        File child = new File(path + "../" + mapEntry.getKey() + ".tf");
        String value = mapEntry.getValue();

        for (String commonElement : commonElements) {
          value = value.replace(commonElement, "");
        }

        for (Entry<String, String> controllerServiceMap : controllerServices.entrySet()) {
          value = value.replace("\"" + controllerServiceMap.getKey() + "\"",
              "\"" + controllerServiceMap.getValue() + "\"");
        }

        for (Entry<String, String> replacerMapNew : ID_NAME_MAP_TO_REPLACE.entrySet()) {
          value = value.replace(replacerMapNew.getKey(), replacerMapNew.getValue());
        }

        StringBuilder childElementsScript = new StringBuilder(value);
        String finalTerraformScriptChild =
            ElementGeneratorUtil.generateFinalTFScript(childElementsScript, rootElementId);

        FileUtils.writeStringToFile(child, finalTerraformScriptChild);
      }

      // Print common elements
      StringBuilder commonElementsScript = new StringBuilder();
      for (String commonElement : commonElements) {
        commonElementsScript.append(commonElement).append(TFUtil.NEWLINE);
      }

      // Generate only if some elements are present(possibility
      // negligible)
      if (commonElementsScript.length() > 0) {
        File common = new File(path + "../common.tf");
        FileUtils.writeStringToFile(common, commonElementsScript.toString());
      }
      System.out.println("The output successfully generated at : " + parent.getCanonicalPath());
    } else {
      throw new IOException(
          "Please provide a valid root_element and nifi_machine in the properties file");
    }
  }

}
