/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

import java.util.List;

/**
 * To create contents terraform element.
 * 
 * @author a8n1
 *
 */
public class TFObjContents implements TerraformObjects {

  private TFObjInput_Ports[] input_ports;
  private TFObjOutput_Ports[] output_ports;

  /**
   * The getter for input_ports.
   * 
   * @return the input_ports
   */
  public TFObjInput_Ports[] getInput_ports() {
    return input_ports;
  }

  /**
   * The setter for input_ports.
   * 
   * @param input_ports the input_ports to set
   */
  public void setInput_ports(TFObjInput_Ports[] input_ports) {
    this.input_ports = input_ports;
  }

  /**
   * The getter for output_ports.
   * 
   * @return the output_ports
   */
  public TFObjOutput_Ports[] getOutput_ports() {
    return output_ports;
  }

  /**
   * The setter for output_ports.
   * 
   * @param output_ports the output_ports to set
   */
  public void setOutput_ports(TFObjOutput_Ports[] output_ports) {
    this.output_ports = output_ports;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.tf.TerraformObjects#generateStringRepresentation()
   */
  @Override
  public String generateStringRepresentation() {
    String element = null;
    try {
      List<String> list = TFUtil.getFieldNameAndValues(this, false);
      element = TFUtil.generateString(TFUtil.getElementNameFromClassName(this.getClass().getName()),
          list, false);
    } catch (IllegalAccessException exception) {
      exception.printStackTrace();
    }
    return element;
  }

}
