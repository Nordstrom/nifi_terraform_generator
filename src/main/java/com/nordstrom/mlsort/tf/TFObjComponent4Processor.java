/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

import java.util.List;

/**
 * To create Component terraform element for processor.
 */
public class TFObjComponent4Processor implements TerraformObjects {

  private String parent_group_id;
  private String name;
  private String type;
  private TFObjPosition tfObjPosition;
  private TFObjConfig tfObjConfig;

  /**
   * The getter for parent_group_id.
   * 
   * @return the parent_group_id
   */
  public String getParent_group_id() {
    return parent_group_id;
  }

  /**
   * The setter for parent_group_id.
   * 
   * @param parent_group_id the parent_group_id to set
   */
  public void setParent_group_id(String parent_group_id) {
    this.parent_group_id = parent_group_id;
  }

  /**
   * The getter for name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * The setter for name.
   * 
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The getter for type.
   * 
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * The setter for type.
   * 
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * The getter for tfObjPosition.
   * 
   * @return the tfObjPosition
   */
  public TFObjPosition getTfObjPosition() {
    return tfObjPosition;
  }

  /**
   * The setter for tfObjPosition.
   * 
   * @param tfObjPosition the tfObjPosition to set
   */
  public void setTfObjPosition(TFObjPosition tfObjPosition) {
    this.tfObjPosition = tfObjPosition;
  }

  /**
   * The getter for tfObjConfig.
   * 
   * @return the tfObjConfig
   */
  public TFObjConfig getTfObjConfig() {
    return tfObjConfig;
  }

  /**
   * The setter for tfObjConfig.
   * 
   * @param tfObjConfig the tfObjConfig to set
   */
  public void setTfObjConfig(TFObjConfig tfObjConfig) {
    this.tfObjConfig = tfObjConfig;
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
