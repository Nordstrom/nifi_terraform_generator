package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.Test;

/**
 * Test TFObjComponent4Connection
 */
public class TFObjComponent4ConnectionTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjComponent4Connection tfObjComponent4Connection = new TFObjComponent4Connection();
    tfObjComponent4Connection.setParent_group_id("parent_group_id");
    tfObjComponent4Connection
        .setBack_pressure_data_size_threshold("back_pressure_data_size_threshold");
    tfObjComponent4Connection.setBack_pressure_object_threshold(BigInteger.valueOf(10));
    tfObjComponent4Connection.setBends(null);
    tfObjComponent4Connection.setDestination(null);
    String[] selected_relationships = {"A", "B"};
    tfObjComponent4Connection.setSelected_relationships(selected_relationships);
    tfObjComponent4Connection.setSource(null);

    String sb = tfObjComponent4Connection.generateStringRepresentation();
    String expectedElement = "component	{" + TFUtil.NEWLINE + TFUtil.TAB
        + "parent_group_id = \"parent_group_id\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "back_pressure_data_size_threshold = \"back_pressure_data_size_threshold\""
        + TFUtil.NEWLINE + TFUtil.TAB + "back_pressure_object_threshold = 10" + TFUtil.NEWLINE
        + TFUtil.TAB + "source = null" + TFUtil.NEWLINE + TFUtil.TAB + "destination = null"
        + TFUtil.NEWLINE + TFUtil.TAB + "selected_relationships	=	[" + TFUtil.NEWLINE + TFUtil.TAB
        + TFUtil.TAB + "\"A\",\"B\"" + TFUtil.NEWLINE + TFUtil.TAB + "]" + TFUtil.NEWLINE
        + TFUtil.NEWLINE + TFUtil.TAB + "bends = null" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate connection object", expectedElement, sb);
  }
}
