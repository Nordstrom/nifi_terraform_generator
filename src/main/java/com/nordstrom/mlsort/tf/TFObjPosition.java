package com.nordstrom.mlsort.tf;

import java.util.List;

/**
 * To create position terraform element.
 */
public class TFObjPosition implements TerraformObjects {

  private double x;
  private double y;

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
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return element;
  }

  /**
   * The getter for x.
   * 
   * @return the x
   */
  public double getX() {
    return x;
  }

  /**
   * The setter for x.
   * 
   * @param x the x to set
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * The getter for y.
   * 
   * @return the y
   */
  public double getY() {
    return y;
  }

  /**
   * The setter for y.
   * 
   * @param y the y to set
   */
  public void setY(double y) {
    this.y = y;
  }

}
