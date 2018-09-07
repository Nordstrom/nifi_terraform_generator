package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import org.junit.Test;

/**
 * Test for TFObjConfig
 */
public class TFObjConfigTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjConfig tfObjConfig = new TFObjConfig();
    String[] array = {"A", "B"};
    tfObjConfig.setAuto_terminated_relationships(array);
    tfObjConfig.setConcurrently_schedulable_task_count(BigInteger.valueOf(10));
    tfObjConfig.setExecution_node("EN");
    Properties properties = new Properties();
    properties.setProperty("key", "value");
    tfObjConfig.setProperties(properties);
    tfObjConfig.setScheduling_period("A");
    tfObjConfig.setScheduling_strategy("A");

    String sb = tfObjConfig.generateStringRepresentation();
    String expectedElement = "config	{" + TFUtil.NEWLINE + TFUtil.TAB
        + "concurrently_schedulable_task_count = 10" + TFUtil.NEWLINE + TFUtil.TAB
        + "scheduling_strategy = \"A\"" + TFUtil.NEWLINE + TFUtil.TAB + "scheduling_period = \"A\""
        + TFUtil.NEWLINE + TFUtil.TAB + "execution_node = \"EN\"" + TFUtil.NEWLINE + TFUtil.TAB
        + "properties	{" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB + "\"key\" = \"value\""
        + TFUtil.NEWLINE + TFUtil.TAB + "}" + TFUtil.NEWLINE + TFUtil.NEWLINE + TFUtil.TAB
        + "auto_terminated_relationships	=	[" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB
        + "\"A\",\"B\"" + TFUtil.NEWLINE + TFUtil.TAB + "]" + TFUtil.NEWLINE + TFUtil.NEWLINE + "}"
        + TFUtil.NEWLINE;
    assertEquals("Failed to generate config object", expectedElement, sb);
  }

}
