package com.nordstrom.mlsort.tf;

import java.util.List;

/**
 * To create source terraform element.
 */
public class TFObjSource implements TerraformObjects {

  private String id;
  private String type;
  private String group_id;

  /**
   * The getter for id.
   * 
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * The getter for group_id.
   * 
   * @return the group_id
   */
  public String getGroup_id() {
    return group_id;
  }

  /**
   * The setter for group_id.
   * 
   * @param group_id the group_id to set
   */
  public void setGroup_id(String group_id) {
    this.group_id = group_id;
  }

  /**
   * The setter for id.
   * 
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
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
