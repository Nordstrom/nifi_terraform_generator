/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */
package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * Test TFObjComponent4Port
 */
public class TFObjComponent4PortTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4Port tfObjComponent4Port = new TFObjComponent4Port();
    tfObjComponent4Port.setName("name");
    tfObjComponent4Port.setParent_group_id("parent_group_id");
    tfObjComponent4Port.setType("type");
    tfObjComponent4Port.setTfObjPosition(null);

    String sb = tfObjComponent4Port.generateStringRepresentation();
    String expectedElement = "component	{" + TFUtil.NEWLINE + TFUtil.TAB
        + "parent_group_id = \"parent_group_id\"" + TFUtil.NEWLINE + TFUtil.TAB + "name = \"name\""
        + TFUtil.NEWLINE + TFUtil.TAB + "type = \"type\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "tfObjPosition = null" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate port object", expectedElement, sb);
  }

}
