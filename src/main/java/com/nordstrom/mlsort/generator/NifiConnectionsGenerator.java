package com.nordstrom.mlsort.generator;


import com.nordstrom.mlsort.jaxb.BendPointsType;
import com.nordstrom.mlsort.jaxb.ConnectionType;
import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.tf.TFObjComponent4Connection;
import com.nordstrom.mlsort.tf.TFObjDestination;
import com.nordstrom.mlsort.tf.TFObjSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * To generate the terraform element corresponding to nifi connections
 */
public class NifiConnectionsGenerator
    extends ElementGenerator<ConnectionType, TFObjComponent4Connection> {

  private static final String TYPE = "nifi_connection";

  /**
   * Constructor taking ConnectionType as argument.
   * 
   * @param connectionsType ConnectionType
   */
  public NifiConnectionsGenerator(final ConnectionType connectionsType) {
    super(connectionsType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4Connection generateElementContent(final ConnectionType connection,
      final String parentGroupId) {
    String id = connection.getId();
    String name = connection.getName();
    Integer labelIndex = connection.getLabelIndex();
    Integer zIndex = connection.getZIndex();

    String sourceId = connection.getSourceId();
    String sourceGroupId = connection.getSourceGroupId();
    String sourceType = connection.getSourceType();

    String destinationId = connection.getDestinationId();
    String destinationGroupId = connection.getDestinationGroupId();
    String destinationType = connection.getDestinationType();

    List<String> relationships = connection.getRelationship();
    BigInteger back_pressure_object_threshold = connection.getMaxWorkQueueSize();
    String back_pressure_data_size_threshold = connection.getMaxWorkQueueDataSize();
    String flowFileExpiration = connection.getFlowFileExpiration();

    TFObjComponent4Connection tfObjComponent4Connection = new TFObjComponent4Connection();

    TFObjSource tfObjSource = new TFObjSource();
    tfObjSource.setId(sourceId);
    tfObjSource.setType(sourceType);
    tfObjSource.setGroup_id(sourceGroupId);
    tfObjComponent4Connection.setSource(tfObjSource);

    TFObjDestination tfObjDestination = new TFObjDestination();
    tfObjDestination.setId(destinationId);
    tfObjDestination.setType(destinationType);
    tfObjDestination.setGroup_id(destinationGroupId);
    tfObjComponent4Connection.setDestination(tfObjDestination);

    tfObjComponent4Connection.setParent_group_id(parentGroupId);
    tfObjComponent4Connection
        .setBack_pressure_data_size_threshold(back_pressure_data_size_threshold);
    tfObjComponent4Connection.setBack_pressure_object_threshold(back_pressure_object_threshold);
    if (relationships.size() > 0) {
      tfObjComponent4Connection
          .setSelected_relationships(relationships.toArray(new String[relationships.size()]));
    }
    List<Map<String, Double>> bends = new ArrayList<>();
    BendPointsType bendPoints = connection.getBendPoints();
    if (null != bendPoints) {
      for (PositionType bendPoint : bendPoints.getBendPoint()) {
        Map<String, Double> map = new HashMap<>();
        map.put("x", bendPoint.getX());
        map.put("y", bendPoint.getY());
        bends.add(map);
      }

      tfObjComponent4Connection.setBends(bends);
    }
    return tfObjComponent4Connection;
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
