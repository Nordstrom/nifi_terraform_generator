package com.nordstrom.mlsort.tf;

import java.util.List;
import java.util.Map;

/**
 * To create bends terraform element.
 */
public class TFObjBends implements TerraformObjects {

  private List<Map<String, Double>> bends;

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
