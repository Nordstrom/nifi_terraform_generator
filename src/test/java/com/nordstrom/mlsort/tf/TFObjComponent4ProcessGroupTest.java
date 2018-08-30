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
 * Test TFObjComponent4ProcessGroup
 */
public class TFObjComponent4ProcessGroupTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4ProcessGroup tfObjComponent4ProcessGroup = new TFObjComponent4ProcessGroup();
    tfObjComponent4ProcessGroup.setName("name");
    tfObjComponent4ProcessGroup.setParent_group_id("parent_group_id");
    tfObjComponent4ProcessGroup.setTfObjPosition(null);

    String sb = tfObjComponent4ProcessGroup.generateStringRepresentation();
    String expectedElement =
        "component	{" + TFUtil.NEWLINE + TFUtil.TAB + "parent_group_id = \"parent_group_id\""
            + TFUtil.NEWLINE + TFUtil.TAB + "name = \"name\"" + TFUtil.NEWLINE + TFUtil.TAB
            + "tfObjPosition = null" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate process group object", expectedElement, sb);
  }

}
