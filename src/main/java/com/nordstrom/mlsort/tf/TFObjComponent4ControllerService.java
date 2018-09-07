package com.nordstrom.mlsort.tf;

import java.util.List;
import java.util.Properties;

/**
 * To create Component terraform element for Controller Service
 */
public class TFObjComponent4ControllerService implements TerraformObjects {

  private String parent_group_id;
  private String name;
  private String type;
  private Properties properties;

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
