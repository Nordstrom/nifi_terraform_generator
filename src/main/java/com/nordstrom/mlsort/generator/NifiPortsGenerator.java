package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.PortType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.tf.TFObjComponent4Port;
import com.nordstrom.mlsort.tf.TFObjPosition;

/**
 * To generate the terraform element corresponding to nifi ports (Input and Output).
 */
public class NifiPortsGenerator extends ElementGenerator<PortType, TFObjComponent4Port> {

  private static final String TYPE = "nifi_port";
  private String portType;

  /**
   * Constructor taking PortType as argument.
   * 
   * @param portType PortType
   */
  public NifiPortsGenerator(final PortType portType) {
    super(portType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4Port generateElementContent(final PortType portType,
      final String parentGroupId) {
    String name = portType.getName();
    // String type = portType.getPosition().Type();
    PositionType positionType = portType.getPosition();
    double x = positionType.getX();
    double y = positionType.getY();

    TFObjComponent4Port tfObjComponent = new TFObjComponent4Port();
    tfObjComponent.setParent_group_id(parentGroupId);
    tfObjComponent.setName(name);
    tfObjComponent.setType(getPortType());
    TFObjPosition tfObjPosition = new TFObjPosition();
    tfObjPosition.setX(x);
    tfObjPosition.setY(y);

    tfObjComponent.setTfObjPosition(tfObjPosition);
    return tfObjComponent;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#getType()
   */
  @Override
  public String getType() {
    return TYPE;
  }

  /**
   * Method to set the type of PORT as INPUT/OUTPUT etc.
   * 
   * @param portType String
   */
  public void setPortType(final String portType) {
    this.portType = portType;
  }

  /**
   * Method to return port type.
   * 
   * @return String portType
   */
  public String getPortType() {
    return this.portType;
  }

}
