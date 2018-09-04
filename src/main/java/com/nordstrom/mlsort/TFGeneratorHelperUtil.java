/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import com.nordstrom.mlsort.generator.ElementGeneratorUtil;
import com.nordstrom.mlsort.generator.VariablesTFGenerator;
import com.nordstrom.mlsort.jaxb.FlowControllerType;
import com.nordstrom.mlsort.jaxb.RootProcessGroupType;
import com.nordstrom.mlsort.tf.TFUtil;

/**
 * Utility class for TFGeneratorHelper
 *
 */
public class TFGeneratorHelperUtil {

  private static final String NIFI_HOST = "${var.nifi_host}";
  private static final String FLOW_XML_PATH = "/flowfiles/flow.xml";

  /**
   * Method to generate terraform scripts corresponding to the common elements.
   * 
   * @param rootElementId String
   * @param tfNameContentMap - Map<String, String> to hold the response(key - tf file name, value tf
   *        content)
   * @param idNameMapToReplace Map<String, String>
   * @param controllerServices Map<String, String>
   * @throws IOException IOException
   */
  public static void writeCommonElements(final String rootElementId,
      final Map<String, String> tfNameContentMap, final Map<String, String> idNameMapToReplace,
      final Map<String, String> controllerServices) throws IOException {
    // Remove duplicate Elements in all values and make a new entry as a
    // shared tf
    Set<String> commonElements = ElementGeneratorUtil.separateCommonElements(tfNameContentMap);

    for (Entry<String, String> mapEntry : tfNameContentMap.entrySet()) {
      String value = mapEntry.getValue();

      for (String commonElement : commonElements) {
        value = value.replace(commonElement, "");
      }

      for (Entry<String, String> controllerServiceMap : controllerServices.entrySet()) {
        value = value.replace("\"" + controllerServiceMap.getKey() + "\"",
            "\"" + controllerServiceMap.getValue() + "\"");
      }

      for (Entry<String, String> replacerMapNew : idNameMapToReplace.entrySet()) {
        value = value.replace(replacerMapNew.getKey(), replacerMapNew.getValue());
      }

      StringBuilder childElementsScript = new StringBuilder(value);
      String finalTerraformScriptChild =
          ElementGeneratorUtil.generateFinalTFScript(childElementsScript, rootElementId);
      writeToAFile("../" + mapEntry.getKey() + ".tf", finalTerraformScriptChild);

    }

    // Print common elements
    StringBuilder commonElementsScript = new StringBuilder();
    for (String commonElement : commonElements) {
      commonElementsScript.append(commonElement).append(TFUtil.NEWLINE);
    }

    // Generate only if some elements are present(possibility
    // negligible)
    if (commonElementsScript.length() > 0) {
      writeToAFile("../common.tf", commonElementsScript.toString());
    }
  }

  /**
   * Method to generate JAXBElements from flow.xml.
   * 
   * @return JAXBElement<FlowControllerType>
   * @throws JAXBException JAXBException
   */
  public static JAXBElement<FlowControllerType> parseFlowXml() throws JAXBException {
    // Parse xml start
    JAXBContext jaxbContext = JAXBContext.newInstance(FlowControllerType.class);
    InputStream inputStream = TFGenerator.class.getResourceAsStream(FLOW_XML_PATH);
    Source source = new StreamSource(inputStream);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    JAXBElement<FlowControllerType> root = (JAXBElement<FlowControllerType>) jaxbUnmarshaller
        .unmarshal(source, FlowControllerType.class);
    // Parse xml finish
    return root;
  }

  /**
   * Method to generate the terraform elements corresponding to the top level components.
   * 
   * @param parentElementsScript StringBuilder
   * @param rootProcessGroup RootProcessGroupType
   */
  public static void generateParentElements(final StringBuilder parentElementsScript,
      final RootProcessGroupType rootProcessGroup) {
    // Generate Connections tf
    if (null != rootProcessGroup.getConnection()) {
      parentElementsScript.append(ElementGeneratorUtil
          .generateTFForConnections(rootProcessGroup.getConnection(), rootProcessGroup.getId()));
    }

    // Generate InputPorts tf
    if (null != rootProcessGroup.getInputPort()) {
      parentElementsScript
          .append(ElementGeneratorUtil.generateTFForPortsForRoot(rootProcessGroup.getInputPort(),
              rootProcessGroup.getId(), ElementGeneratorUtil.INPUT_PORT));
    }

    // Generate OutputPorts tf
    if (null != rootProcessGroup.getOutputPort()) {
      parentElementsScript
          .append(ElementGeneratorUtil.generateTFForPortsForRoot(rootProcessGroup.getOutputPort(),
              rootProcessGroup.getId(), ElementGeneratorUtil.OUTPUT_PORT));
    }

    // Generate Processors tf
    if (null != rootProcessGroup.getProcessor()) {
      parentElementsScript.append(ElementGeneratorUtil
          .generateTFForProcessors(rootProcessGroup.getProcessor(), rootProcessGroup.getId()));
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

  /**
   * Method to generate the main terraform script and variables script.
   * 
   * @param rootElementId String
   * @param nifiMachine String
   * @param finalTerraformScriptParent String
   * @throws IOException IOException
   */
  public static void generateMainAndVariablesTF(final String rootElementId,
      final String nifiMachine, final String finalTerraformScriptParent) throws IOException {
    // Create provider
    String provider = TFUtil.getProvider(NIFI_HOST);

    String variablesContent = VariablesTFGenerator.generateVariablesTF(nifiMachine, rootElementId);

    writeToAFile("../variables.tf", variablesContent);
    writeToAFile("../main.tf", provider + finalTerraformScriptParent);
  }

  /**
   * Method to write <code>fileContent</code> as a file to the specified <code>filePath</code>.
   * 
   * @param filePath String
   * @param fileContent String
   * @throws IOException IOException
   */
  public static void writeToAFile(final String filePath, final String fileContent)
      throws IOException {
    String path = TFGenerator.class.getResource("/").getFile();
    File file = new File(path + filePath);
    FileUtils.writeStringToFile(file, fileContent);
  }

}
