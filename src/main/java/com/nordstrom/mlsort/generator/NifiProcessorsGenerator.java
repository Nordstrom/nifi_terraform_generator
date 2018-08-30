/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.PositionType;
import com.nordstrom.mlsort.jaxb.ProcessorType;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.Styles;
import com.nordstrom.mlsort.tf.TFObjComponent4Processor;
import com.nordstrom.mlsort.tf.TFObjConfig;
import com.nordstrom.mlsort.tf.TFObjPosition;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

/**
 * To generate the terraform element corresponding to nifi processor.
 */
public class NifiProcessorsGenerator
    extends ElementGenerator<ProcessorType, TFObjComponent4Processor> {

  private static final String TYPE = "nifi_processor";

  /**
   * Constructor taking ProcessorType as argument.
   * 
   * @param processorsType ProcessorType
   */
  public NifiProcessorsGenerator(final ProcessorType processorsType) {
    super(processorsType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4Processor generateElementContent(final ProcessorType processor,
      final String parentGroupId) {
    String id = processor.getId();
    String name = processor.getName();
    String comment = processor.getComment();
    Styles styles = processor.getStyles();
    PositionType positionType = processor.getPosition();
    double x = positionType.getX();
    double y = positionType.getY();
    String type = processor.getClazz();
    List<String> autoTerminatedRelationships = processor.getAutoTerminatedRelationship();
    List<PropertyType> propertiesList = processor.getProperty();

    // ConfigType configType = processor.getConfig();

    /*
     * BundleType buntle = processor.getBundle(); buntle.getGroup(); buntle.getArtifact();
     * buntle.getVersion();
     */

    BigInteger concurrently_schedulable_task_count = processor.getMaxConcurrentTasks();
    String scheduling_period = processor.getSchedulingPeriod();
    /*
     * configType.getPenaltyDuration(); configType.getYieldDuration();
     * configType.getBulletinLevel(); configType.getLossTolerant();
     */

    // configType.getScheduledState();
    String scheduling_strategy = processor.getSchedulingStrategy().value();
    String execution_node = processor.getExecutionNode().value();
    // configType.getRunDurationMillis();

    TFObjComponent4Processor tfObjComponent = new TFObjComponent4Processor();
    tfObjComponent.setName(name);
    tfObjComponent.setParent_group_id(parentGroupId);
    tfObjComponent.setType(type);

    TFObjConfig tfObjConfig = new TFObjConfig();
    tfObjConfig.setConcurrently_schedulable_task_count(concurrently_schedulable_task_count);
    tfObjConfig.setScheduling_strategy(scheduling_strategy);
    tfObjConfig.setScheduling_period(scheduling_period);
    tfObjConfig.setExecution_node(execution_node);

    if (null != autoTerminatedRelationships && autoTerminatedRelationships.size() > 0) {
      tfObjConfig.setAuto_terminated_relationships(
          autoTerminatedRelationships.toArray(new String[autoTerminatedRelationships.size()]));
    } else {
      String[] array = {};
      tfObjConfig.setAuto_terminated_relationships(array);
    }

    Properties properties = new Properties();
    for (PropertyType propertyType : propertiesList) {
      if (null != propertyType.getValue()) {
        properties.setProperty(propertyType.getName(), propertyType.getValue());
      }
    }
    tfObjConfig.setProperties(properties);

    tfObjComponent.setTfObjConfig(tfObjConfig);

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
