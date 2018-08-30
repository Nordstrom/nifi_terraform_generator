/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */
package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.Properties;
import org.junit.Test;

/**
 * Test TFObjComponent4ControllerService
 */
public class TFObjComponent4ControllerServiceTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4ControllerService tfObjComponent4ControllerService =
        new TFObjComponent4ControllerService();
    tfObjComponent4ControllerService.setParent_group_id("parent_group_id");
    tfObjComponent4ControllerService.setName("name");
    Properties properties = new Properties();
    properties.setProperty("key", "value");
    tfObjComponent4ControllerService.setProperties(properties);
    tfObjComponent4ControllerService.setType("type");

    String sb = tfObjComponent4ControllerService.generateStringRepresentation();
    String expectedElement =
        "component	{" + TFUtil.NEWLINE + TFUtil.TAB + "parent_group_id = \"parent_group_id\""
            + TFUtil.NEWLINE + TFUtil.TAB + "name = \"name\"" + TFUtil.NEWLINE + TFUtil.TAB
            + "type = \"type\"" + TFUtil.NEWLINE + TFUtil.TAB + "properties	{" + TFUtil.NEWLINE
            + TFUtil.TAB + TFUtil.TAB + "\"key\" = \"value\"" + TFUtil.NEWLINE + TFUtil.TAB + "}"
            + TFUtil.NEWLINE + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate controller service object", expectedElement, sb);
  }
}
