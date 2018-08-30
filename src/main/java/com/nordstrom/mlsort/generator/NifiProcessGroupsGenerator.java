/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.LabelType;
import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.ProcessGroupType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.tf.TFObjComponent4ProcessGroup;
import com.nordstrom.mlsort.tf.TFObjPosition;
import com.nordstrom.mlsort.tf.TFUtil;
import java.util.List;
import java.util.Map;

/**
 * To generate the terraform element corresponding to nifi processor.
 */
public class NifiProcessGroupsGenerator
    extends ElementGenerator<ProcessGroupType, TFObjComponent4ProcessGroup> {

  private static final String TYPE = "nifi_process_group";

  /**
   * Constructor is made private to avoid any instantiation.
   */
  private NifiProcessGroupsGenerator() {}

  private List<ConnectionType> connections;
  private List<PortType> outputPorts;
  private List<PortType> inputPorts;
  private List<ProcessorType> processors;
  private List<ControllerServiceType> controllerServices;
  private List<FunnelType> funnels;
  private List<LabelType> labels;
  private List<ProcessGroupType> processGroups;
  private String parentGroupId;
  private List<String> childHolderList;
  private Map<String, String> map;

  private static StringBuilder tfOfChildElements = new StringBuilder();

  /**
   * To build the NifiProcessGroupsGenerator instance.
   * 
   * @return NifiProcessGroupsGenerator instance
   */
  public NifiProcessGroupsGenerator build() {
    NifiProcessGroupsGenerator nifiProcessGroupsGenerator = new NifiProcessGroupsGenerator();
    nifiProcessGroupsGenerator.connections = this.connections;
    nifiProcessGroupsGenerator.outputPorts = this.outputPorts;
    nifiProcessGroupsGenerator.inputPorts = this.inputPorts;
    nifiProcessGroupsGenerator.processors = this.processors;
    nifiProcessGroupsGenerator.controllerServices = this.controllerServices;
    nifiProcessGroupsGenerator.funnels = this.funnels;
    nifiProcessGroupsGenerator.labels = this.labels;
    nifiProcessGroupsGenerator.processGroups = this.processGroups;

    ProcessGroupType processGroupsType = super.getJaxbObject();
    nifiProcessGroupsGenerator.setJaxbObject(processGroupsType);

    // Generate tf of childElements
    generateTfofChildElements();

    return nifiProcessGroupsGenerator;
  }

  /**
   * To generate terraform scripts for the child elements.
   */
  private void generateTfofChildElements() {
    String id = super.getJaxbObject().getId();
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForConnections(this.connections, id));
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForPorts(this.outputPorts, id,
        ElementGeneratorUtil.OUTPUT_PORT));
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForPorts(this.inputPorts, id,
        ElementGeneratorUtil.INPUT_PORT));
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForProcessors(this.processors, id));
    StringBuilder sb =
        ElementGeneratorUtil.generateTFForControllerServices(this.controllerServices, id);
    tfOfChildElements.append(sb);
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForFunnels(this.funnels, id));
    tfOfChildElements.append(ElementGeneratorUtil.generateTFForLabels(this.labels, id));
    ElementGeneratorUtil.generateTFForProcessGroups(this.processGroups, id, map, false,
        this.childHolderList);
  }

  /**
   * Construct the object with the provided arguments.
   * 
   * @param processGroup ProcessGroupType
   * @param parentGroupId String
   * @param map Map<String, String>
   * @param isParent boolean
   * @param childHolderList List<String>
   */
  public NifiProcessGroupsGenerator(final ProcessGroupType processGroup, final String parentGroupId,
      final Map<String, String> map, final boolean isParent, final List<String> childHolderList) {
    super(processGroup);
    this.parentGroupId = parentGroupId;
    this.map = map;
    if (isParent) {
      tfOfChildElements = new StringBuilder();
    }
    this.childHolderList = childHolderList;
  }

  /**
   * Method to add List<ConnectionsType> to Process Groups.
   * 
   * @param connections List<ConnectionsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withConnections(final List<ConnectionType> connections) {
    this.connections = connections;
    return this;
  }

  /**
   * Method to add List<PortsType> to Process Groups.
   * 
   * @param outputPorts List<PortsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withOutputPorts(final List<PortType> outputPorts) {
    this.outputPorts = outputPorts;
    return this;
  }

  /**
   * Method to add List<PortsType> to Process Groups.
   * 
   * @param inputPorts List<PortsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withInputPorts(final List<PortType> inputPorts) {
    this.inputPorts = inputPorts;
    return this;
  }

  /**
   * Method to add List<ProcessorsType> to Process Groups.
   * 
   * @param processors List<ProcessorsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withProcessors(final List<ProcessorType> processors) {
    this.processors = processors;
    return this;
  }

  /**
   * Method to add List<ControllerServicesType> to Process Groups.
   * 
   * @param controllerServices List<ControllerServicesType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withControllerServices(
      final List<ControllerServiceType> controllerServices) {
    this.controllerServices = controllerServices;
    return this;
  }

  /**
   * Method to add List<FunnelsType> to Process Groups.
   * 
   * @param funnels List<FunnelsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withFunnels(final List<FunnelType> funnels) {
    this.funnels = funnels;
    return this;
  }

  /**
   * Method to add List<LabelsType> to Process Groups.
   * 
   * @param labels List<LabelsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withLabels(final List<LabelType> labels) {
    this.labels = labels;
    return this;
  }

  /**
   * Method to add List<ProcessGroupsType> to Process Groups.
   * 
   * @param processGroups List<ProcessGroupsType>
   * @return NifiProcessGroupsGenerator NifiProcessGroupsGenerator object
   */
  public NifiProcessGroupsGenerator withProcessGroups(final List<ProcessGroupType> processGroups) {
    this.processGroups = processGroups;
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateTFElement(java.lang.String,
   * java.lang.String)
   */
  @Override
  public String generateTFElement(final String name, final String parentGroupId) {
    TFObjComponent4ProcessGroup tfObjComponent =
        generateElementContent(super.getJaxbObject(), parentGroupId);
    String content = tfObjComponent.generateStringRepresentation();
    StringBuilder sb = new StringBuilder();
    sb.append("resource").append(TFUtil.SPACE).append(TFUtil.DOUBLE_QUOTE).append(TYPE)
        .append(TFUtil.DOUBLE_QUOTE).append(TFUtil.SPACE).append(TFUtil.DOUBLE_QUOTE).append(name)
        .append(TFUtil.DOUBLE_QUOTE).append(TFUtil.SPACE).append(TFUtil.OPEN_BRACE);
    sb.append(TFUtil.NEWLINE).append(TFUtil.shiftRight(content)).append(TFUtil.NEWLINE)
        .append(TFUtil.CLOSE_BRACE).append(TFUtil.NEWLINE).append(TFUtil.NEWLINE);
    // Add child elements
    sb.append(tfOfChildElements);
    return sb.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4ProcessGroup generateElementContent(final ProcessGroupType processGroup,
      final String parentGroupId) {
    String id = processGroup.getId();
    String name = processGroup.getName();
    String comment = processGroup.getComment();
    PositionType positionType = processGroup.getPosition();
    double x = positionType.getX();
    double y = positionType.getY();

    TFObjComponent4ProcessGroup tfObjComponent = new TFObjComponent4ProcessGroup();
    tfObjComponent.setName(name);
    tfObjComponent.setParent_group_id(parentGroupId);
    TFObjPosition tfObjPosition = new TFObjPosition();
    tfObjPosition.setX(x);
    tfObjPosition.setY(y);

    tfObjComponent.setTfObjPosition(tfObjPosition);
    return tfObjComponent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#getType()
   */
  @Override
  public String getType() {
    return TYPE;
  }

}
