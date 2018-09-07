package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.junit.Test;

/**
 * Test TFObjComponent4Processor
 */
public class TFObjComponent4ProcessorTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4Processor tfObjComponent4Processor = new TFObjComponent4Processor();
    tfObjComponent4Processor.setName("name");
    tfObjComponent4Processor.setParent_group_id("parent_group_id");
    tfObjComponent4Processor.setTfObjConfig(null);
    tfObjComponent4Processor.setTfObjPosition(null);
    tfObjComponent4Processor.setType("type");

    String sb = tfObjComponent4Processor.generateStringRepresentation();
    String expectedElement = "component	{" + TFUtil.NEWLINE + TFUtil.TAB
        + "parent_group_id = \"parent_group_id\"" + TFUtil.NEWLINE + TFUtil.TAB + "name = \"name\""
        + TFUtil.NEWLINE + TFUtil.TAB + "type = \"type\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "tfObjPosition = null" + TFUtil.NEWLINE + TFUtil.TAB + "tfObjConfig = null"
        + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate processor object", expectedElement, sb);
  }

}
