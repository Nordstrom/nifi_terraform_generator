package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * Test for TFObjDestination
 */
public class TFObjDestinationTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjDestination tfObjDestination = new TFObjDestination();
    tfObjDestination.setId("ID");
    tfObjDestination.setType("TYPE");
    tfObjDestination.setGroup_id("GROUP_ID");
    String sb = tfObjDestination.generateStringRepresentation();
    String expectedElement = "destination	{" + TFUtil.NEWLINE + TFUtil.TAB + "id = \"ID\""
        + TFUtil.NEWLINE + TFUtil.TAB + "type = \"TYPE\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "group_id = \"GROUP_ID\"" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate destination object", expectedElement, sb);
  }

}
