/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * To create Component terraform element for Connection.
 */
public class TFObjComponent4Connection implements TerraformObjects {

  private String parent_group_id;
  private String back_pressure_data_size_threshold;
  private BigInteger back_pressure_object_threshold;

  private TFObjSource source;
  private TFObjDestination destination;
  private String[] selected_relationships;
  private List<Map<String, Double>> bends;

  /**
   * The getter for back_pressure_data_size_threshold.
   * 
   * @return the back_pressure_data_size_threshold
   */
  public String getBack_pressure_data_size_threshold() {
    return back_pressure_data_size_threshold;
  }

  /**
   * The setter for back_pressure_data_size_threshold.
   * 
   * @param back_pressure_data_size_threshold the back_pressure_data_size_threshold to set
   */
  public void setBack_pressure_data_size_threshold(String back_pressure_data_size_threshold) {
    this.back_pressure_data_size_threshold = back_pressure_data_size_threshold;
  }

  /**
   * The getter for back_pressure_object_threshold.
   * 
   * @return the back_pressure_object_threshold
   */
  public BigInteger getBack_pressure_object_threshold() {
    return back_pressure_object_threshold;
  }

  /**
   * The setter for back_pressure_object_threshold.
   * 
   * @param back_pressure_object_threshold the back_pressure_object_threshold to set
   */
  public void setBack_pressure_object_threshold(BigInteger back_pressure_object_threshold) {
    this.back_pressure_object_threshold = back_pressure_object_threshold;
  }

  /**
   * The getter for bends.
   * 
   * @return the bends
   */
  public List<Map<String, Double>> getBends() {
    return bends;
  }

  /**
   * The setter for bends.
   * 
   * @param bends the bends to set
   */
  public void setBends(List<Map<String, Double>> bends) {
    this.bends = bends;
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
   * The getter for source.
   * 
   * @return the source
   */
  public TFObjSource getSource() {
    return source;
  }

  /**
   * The setter for source.
   * 
   * @param source the source to set
   */
  public void setSource(TFObjSource source) {
    this.source = source;
  }

  /**
   * The getter for destination.
   * 
   * @return the destination
   */
  public TFObjDestination getDestination() {
    return destination;
  }

  /**
   * The setter for destination.
   * 
   * @param destination the destination to set
   */
  public void setDestination(TFObjDestination destination) {
    this.destination = destination;
  }

  /**
   * The getter for selected_relationships.
   * 
   * @return the selected_relationships
   */
  public String[] getSelected_relationships() {
    return selected_relationships;
  }

  /**
   * The setter for selected_relationships.
   * 
   * @param selected_relationships the selected_relationships to set
   */
  public void setSelected_relationships(String[] selected_relationships) {
    this.selected_relationships = selected_relationships;
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
