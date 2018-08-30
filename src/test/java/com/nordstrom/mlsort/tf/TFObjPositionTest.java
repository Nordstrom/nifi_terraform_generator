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
 * Test for TFObjPosition
 */
public class TFObjPositionTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjPosition tfObjPosition = new TFObjPosition();
    tfObjPosition.setX(Double.valueOf(0));
    tfObjPosition.setY(Double.valueOf(0));
    String sb = tfObjPosition.generateStringRepresentation();
    String expectedElement = "position	{" + TFUtil.NEWLINE + TFUtil.TAB + "x = 0.0"
        + TFUtil.NEWLINE + TFUtil.TAB + "y = 0.0" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate position object", expectedElement, sb);
  }

}
