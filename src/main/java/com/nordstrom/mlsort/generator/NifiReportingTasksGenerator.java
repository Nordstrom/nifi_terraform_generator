/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import java.util.Properties;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.jaxb.ReportingTaskType;
import com.nordstrom.mlsort.tf.TFObjComponent4ReportingTask;

/**
 * To generate the terraform element corresponding to nifi funnel.
 */
public class NifiReportingTasksGenerator
    extends ElementGenerator<ReportingTaskType, TFObjComponent4ReportingTask> {

  private static final String TYPE = "nifi_reporting_task";

  /**
   * Constructor taking ReportingTaskType as argument.
   * 
   * @param reportingTaskType ReportingTaskType
   */
  public NifiReportingTasksGenerator(final ReportingTaskType reportingTaskType) {
    super(reportingTaskType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4ReportingTask generateElementContent(
      final ReportingTaskType reportingTaskType, final String parentGroupId) {
    String name = reportingTaskType.getName();
    String type = reportingTaskType.getClazz();

    TFObjComponent4ReportingTask tfObjComponent = new TFObjComponent4ReportingTask();
    tfObjComponent.setParent_group_id(parentGroupId);
    tfObjComponent.setName(name);
    tfObjComponent.setType(type);

    Properties properties = new Properties();
    for (PropertyType propertyType : reportingTaskType.getProperty()) {
      if (null != propertyType.getValue()) {
        properties.setProperty(propertyType.getName(), propertyType.getValue());
      }
    }
    tfObjComponent.setProperties(properties);

    tfObjComponent.setScheduling_period(reportingTaskType.getSchedulingPeriod());
    tfObjComponent.setScheduling_strategy(reportingTaskType.getSchedulingStrategy().value());
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
