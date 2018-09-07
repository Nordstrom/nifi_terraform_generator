package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * Test TFObjComponent4Funnel
 */
public class TFObjComponent4FunnelTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4Funnel tfObjComponent4Funnel = new TFObjComponent4Funnel();
    tfObjComponent4Funnel.setParent_group_id("parent_group_id");
    tfObjComponent4Funnel.setTfObjPosition(null);

    String sb = tfObjComponent4Funnel.generateStringRepresentation();
    String expectedElement = "component	{" + TFUtil.NEWLINE + TFUtil.TAB
        + "parent_group_id = \"parent_group_id\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "tfObjPosition = null" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate funnel object", expectedElement, sb);
  }

}
