/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.RemoteGroupPortType;
import com.nordstrom.mlsort.jaxb.RemoteProcessGroupType;
import com.nordstrom.mlsort.tf.TFObjComponent4RemoteProcessGroup;
import com.nordstrom.mlsort.tf.TFObjContents;
import com.nordstrom.mlsort.tf.TFObjInput_Ports;
import com.nordstrom.mlsort.tf.TFObjOutput_Ports;
import com.nordstrom.mlsort.tf.TFObjPosition;
import java.util.ArrayList;
import java.util.List;

/**
 * To generate the terraform element corresponding to nifi processor.
 */
public class NifiRemoteProcessGroupsGenerator
    extends ElementGenerator<RemoteProcessGroupType, TFObjComponent4RemoteProcessGroup> {

  private static final String TYPE = "nifi_remote_process_group";

  /**
   * Constructor taking RemoteProcessGroupType as argument.
   * 
   * @param remoteProcessGroup RemoteProcessGroupType
   */
  public NifiRemoteProcessGroupsGenerator(final RemoteProcessGroupType remoteProcessGroup) {
    super(remoteProcessGroup);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4RemoteProcessGroup generateElementContent(
      final RemoteProcessGroupType remoteProcessGroup, final String parentGroupId) {
    String id = remoteProcessGroup.getId();
    String name = remoteProcessGroup.getName();
    String comment = remoteProcessGroup.getComment();
    PositionType positionType = remoteProcessGroup.getPosition();
    double x = positionType.getX();
    double y = positionType.getY();
    remoteProcessGroup.getInputPort();
    remoteProcessGroup.getOutputPort();
    remoteProcessGroup.getNetworkInterface();
    String proxyHost = remoteProcessGroup.getProxyHost();
    remoteProcessGroup.getProxyPassword();
    remoteProcessGroup.getProxyPort();
    String proxyUser = remoteProcessGroup.getProxyUser();
    String timeout = remoteProcessGroup.getTimeout();
    String transportProtocol = remoteProcessGroup.getTransportProtocol();
    remoteProcessGroup.getUrl();
    String targetUris = remoteProcessGroup.getUrls();
    remoteProcessGroup.getVersionedComponentId();
    String yieldPeriod = remoteProcessGroup.getYieldPeriod();

    TFObjComponent4RemoteProcessGroup tfObjComponent = new TFObjComponent4RemoteProcessGroup();
    tfObjComponent.setName(name);
    tfObjComponent.setParent_group_id(parentGroupId);
    TFObjPosition tfObjPosition = new TFObjPosition();
    tfObjPosition.setX(x);
    tfObjPosition.setY(y);
    tfObjComponent.setTfObjPosition(tfObjPosition);
    tfObjComponent.setTargetUris(targetUris);
    tfObjComponent.setYieldPeriod(yieldPeriod);
    tfObjComponent.setTimeout(timeout);
    tfObjComponent.setProxyHost(proxyHost);
    tfObjComponent.setProxyUser(proxyUser);

    List<TFObjInput_Ports> inputList = new ArrayList<>();
    if (null != remoteProcessGroup.getInputPort()) {
      for (RemoteGroupPortType iterable_element : remoteProcessGroup.getInputPort()) {
        TFObjInput_Ports tfObjInputPorts = new TFObjInput_Ports();
        tfObjInputPorts.setId(iterable_element.getId());
        tfObjInputPorts.setConcurrentlySchedulableTaskCount(
            iterable_element.getMaxConcurrentTasks().intValue());
        tfObjInputPorts.setName(iterable_element.getName());
        // tfObjInputPorts.setUseCompression(iterable_element.isUseCompression());
        inputList.add(tfObjInputPorts);
      }
    }

    List<TFObjOutput_Ports> outputList = new ArrayList<>();
    if (null != remoteProcessGroup.getOutputPort()) {
      for (RemoteGroupPortType iterable_element : remoteProcessGroup.getOutputPort()) {
        TFObjOutput_Ports tfObjOutputPorts = new TFObjOutput_Ports();
        tfObjOutputPorts.setId(iterable_element.getId());
        tfObjOutputPorts.setConcurrentlySchedulableTaskCount(
            iterable_element.getMaxConcurrentTasks().intValue());
        tfObjOutputPorts.setName(iterable_element.getName());
        // tfObjOutputPorts.setUseCompression(iterable_element.isUseCompression());
        outputList.add(tfObjOutputPorts);
      }
    }

    TFObjInput_Ports[] tfObjInputPortsArr = new TFObjInput_Ports[inputList.size()];
    tfObjInputPortsArr = inputList.toArray(tfObjInputPortsArr);
    TFObjOutput_Ports[] tfObjOutputPortsArr = new TFObjOutput_Ports[outputList.size()];
    tfObjOutputPortsArr = outputList.toArray(tfObjOutputPortsArr);

    TFObjContents tfObjContents = new TFObjContents();
    tfObjContents.setInput_ports(tfObjInputPortsArr);
    tfObjContents.setOutput_ports(tfObjOutputPortsArr);
    tfObjComponent.setTfObjContents(tfObjContents);
    tfObjComponent.setTransportProtocol(transportProtocol);
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
