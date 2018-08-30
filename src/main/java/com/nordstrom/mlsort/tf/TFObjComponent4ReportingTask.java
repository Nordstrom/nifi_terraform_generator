/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

import java.util.List;
import java.util.Properties;

/**
 * To create Component terraform element for Reporting Task.
 */
public class TFObjComponent4ReportingTask implements TerraformObjects {

  private String parent_group_id;
  private String name;
  private String type;
  private Properties properties;
  private String scheduling_strategy;
  private String scheduling_period;

  /**
   * The getter for scheduling_strategy.
   * 
   * @return the scheduling_strategy
   */
  public String getScheduling_strategy() {
    return scheduling_strategy;
  }

  /**
   * The setter for scheduling_strategy.
   * 
   * @param scheduling_strategy the scheduling_strategy to set
   */
  public void setScheduling_strategy(String scheduling_strategy) {
    this.scheduling_strategy = scheduling_strategy;
  }

  /**
   * The getter for scheduling_period.
   * 
   * @return the scheduling_period
   */
  public String getScheduling_period() {
    return scheduling_period;
  }

  /**
   * The getter for scheduling_period.
   * 
   * @param scheduling_period the scheduling_period to set
   */
  public void setScheduling_period(String scheduling_period) {
    this.scheduling_period = scheduling_period;
  }

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
   * The getter for properties.
   * 
   * @return the properties
   */
  public Properties getProperties() {
    return properties;
  }

  /**
   * The setter for properties.
   * 
   * @param properties the properties to set
   */
  public void setProperties(Properties properties) {
    this.properties = properties;
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
