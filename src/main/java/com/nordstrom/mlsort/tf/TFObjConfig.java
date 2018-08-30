/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

/**
 * To create config terraform element.
 */
public class TFObjConfig implements TerraformObjects {

  private BigInteger concurrently_schedulable_task_count;
  private String scheduling_strategy;
  private String scheduling_period;
  private String execution_node;
  private Properties properties;
  private String[] auto_terminated_relationships;

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
   * The setter for scheduling_period.
   * 
   * @param scheduling_period the scheduling_period to set
   */
  public void setScheduling_period(String scheduling_period) {
    this.scheduling_period = scheduling_period;
  }

  /**
   * The getter for execution_node.
   * 
   * @return the execution_node
   */
  public String getExecution_node() {
    return execution_node;
  }

  /**
   * The setter for execution_node.
   * 
   * @param execution_node the execution_node to set
   */
  public void setExecution_node(String execution_node) {
    this.execution_node = execution_node;
  }

  /**
   * The getter for concurrently_schedulable_task_count.
   * 
   * @return the concurrently_schedulable_task_count
   */
  public BigInteger getConcurrently_schedulable_task_count() {
    return concurrently_schedulable_task_count;
  }

  /**
   * The setter for concurrently_schedulable_task_count.
   * 
   * @param concurrently_schedulable_task_count the concurrently_schedulable_task_count to set
   */
  public void setConcurrently_schedulable_task_count(
      BigInteger concurrently_schedulable_task_count) {
    this.concurrently_schedulable_task_count = concurrently_schedulable_task_count;
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

  /**
   * The getter for auto_terminated_relationships.
   * 
   * @return the auto_terminated_relationships
   */
  public String[] getAuto_terminated_relationships() {
    return auto_terminated_relationships;
  }

  /**
   * The setter for auto_terminated_relationships.
   * 
   * @param auto_terminated_relationships the auto_terminated_relationships to set
   */
  public void setAuto_terminated_relationships(String[] auto_terminated_relationships) {
    this.auto_terminated_relationships = auto_terminated_relationships;
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
