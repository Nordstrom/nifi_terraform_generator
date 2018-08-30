/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.jaxb.BundleType;
import com.nordstrom.mlsort.jaxb.ControllerServiceType;
import com.nordstrom.mlsort.jaxb.PropertyType;
import com.nordstrom.mlsort.tf.TFObjComponent4ControllerService;
import java.util.List;
import java.util.Properties;

/**
 * To generate the terraform element corresponding to nifi controller service.
 */
public class NifiControllerServicesGenerator
    extends ElementGenerator<ControllerServiceType, TFObjComponent4ControllerService> {

  private static final String TYPE = "nifi_controller_service";

  /**
   * Constructor taking ControllerServiceType as argument.
   * 
   * @param controllerServicesType ControllerServiceType
   */
  public NifiControllerServicesGenerator(final ControllerServiceType controllerServicesType) {
    super(controllerServicesType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.generator.ElementGenerator#generateElementContent(java.lang.Object,
   * java.lang.String)
   */
  @Override
  public TFObjComponent4ControllerService generateElementContent(
      final ControllerServiceType controllerService, final String parentGroupId) {
    String id = controllerService.getId();
    BundleType bundle = controllerService.getBundle();
    String comments = controllerService.getComment();
    String name = controllerService.getName();
    List<PropertyType> propertyList = controllerService.getProperty();
    // String state = controllerService.getState();
    String type = controllerService.getClazz();

    Properties properties = new Properties();
    for (PropertyType propertyType : propertyList) {
      if (null != propertyType.getValue()) {
        properties.setProperty(propertyType.getName(), propertyType.getValue());
      }
    }

    TFObjComponent4ControllerService tfObjComponent4ControllerService =
        new TFObjComponent4ControllerService();
    tfObjComponent4ControllerService.setName(name);
    tfObjComponent4ControllerService.setParent_group_id(parentGroupId);
    tfObjComponent4ControllerService.setProperties(properties);
    tfObjComponent4ControllerService.setType(type);
    return tfObjComponent4ControllerService;
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
