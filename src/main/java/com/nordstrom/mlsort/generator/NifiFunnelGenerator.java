package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.FunnelType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.tf.TFObjComponent4Funnel;
import com.nordstrom.mlsort.tf.TFObjPosition;

/**
 * To generate the terraform element corresponding to nifi funnel.
 */
public class NifiFunnelGenerator extends ElementGenerator<FunnelType, TFObjComponent4Funnel> {

  private static final String TYPE = "nifi_funnel";

  /**
   * Constructor taking FunnelType as argument.
   * 
   * @param funnelType FunnelType
   */

  public NifiFunnelGenerator(final FunnelType funnelType) {
    super(funnelType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4Funnel generateElementContent(final FunnelType funnelType,
      final String parentGroupId) {
    String id = funnelType.getId();
    PositionType positionType = funnelType.getPosition();
    double x = positionType.getX();
    double y = positionType.getY();

    TFObjComponent4Funnel tfObjComponent = new TFObjComponent4Funnel();
    tfObjComponent.setParent_group_id(parentGroupId);
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

}
