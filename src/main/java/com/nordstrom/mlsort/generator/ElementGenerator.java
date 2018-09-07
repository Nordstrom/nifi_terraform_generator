package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.tf.TFUtil;
import com.nordstrom.mlsort.tf.TerraformObjects;

/**
 * Contains method to extract data from XML using JAXB and generate terraform specific classes.
 *
 */
public abstract class ElementGenerator<K extends Object, V extends TerraformObjects> {

  private K jaxbObject;

  /**
   * Constructor taking generic Jaxb Object.
   * 
   * @param jaxbObject K
   */
  public ElementGenerator(final K jaxbObject) {
    this.jaxbObject = jaxbObject;
  }

  /**
   * Default constructor.
   */
  public ElementGenerator() {}

  /**
   * Method to get the Jaxb Object.
   * 
   * @return K jaxbObject
   */
  public K getJaxbObject() {
    return this.jaxbObject;
  }

  /**
   * Method to set the Jaxb Object.
   * 
   * @param jaxbObject K
   */
  public void setJaxbObject(final K jaxbObject) {
    this.jaxbObject = jaxbObject;
  }

  /**
   * Method to generate terraform element.
   * 
   * @param name String
   * @param parentGroupId String
   * @return String - Generated terraform element
   */
  public String generateTFElement(final String name, final String parentGroupId) {
    V tfObjComponent = generateElementContent(this.jaxbObject, parentGroupId);
    String content = tfObjComponent.generateStringRepresentation();
    StringBuilder sb = new StringBuilder();
    sb.append("resource").append(TFUtil.SPACE).append(TFUtil.DOUBLE_QUOTE).append(getType())
        .append(TFUtil.DOUBLE_QUOTE).append(TFUtil.SPACE).append(TFUtil.DOUBLE_QUOTE).append(name)
        .append(TFUtil.DOUBLE_QUOTE).append(TFUtil.SPACE).append(TFUtil.OPEN_BRACE);
    sb.append(TFUtil.NEWLINE).append(TFUtil.shiftRight(content)).append(TFUtil.NEWLINE)
        .append(TFUtil.CLOSE_BRACE);

    return sb.toString();
  }

  /**
   * Method to generate terraform element content, implementation will be done on child classes.
   * 
   * @param jaxbObject K
   * @param parentGroupId String
   * @return V - a TerraformObjects
   */
  public abstract V generateElementContent(final K jaxbObject, final String parentGroupId);

  /**
   * To get the type of terraform element.
   * 
   * @return String - tf element type
   */
  public abstract String getType();

}
