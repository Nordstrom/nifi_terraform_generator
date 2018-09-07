package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * Test for TFObjSource
 */
public class TFObjSourceTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjSource tfObjSource = new TFObjSource();
    tfObjSource.setId("ID");
    tfObjSource.setType("TYPE");
    tfObjSource.setGroup_id("GROUP_ID");
    String sb = tfObjSource.generateStringRepresentation();
    String expectedElement = "source	{" + TFUtil.NEWLINE + TFUtil.TAB + "id = \"ID\""
        + TFUtil.NEWLINE + TFUtil.TAB + "type = \"TYPE\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "group_id = \"GROUP_ID\"" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate source object", expectedElement, sb);
  }

}
