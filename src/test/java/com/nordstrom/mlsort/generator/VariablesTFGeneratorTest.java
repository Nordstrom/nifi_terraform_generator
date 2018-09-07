package com.nordstrom.mlsort.generator;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.nordstrom.mlsort.tf.TFUtil;

/**
 * Test class for VariablesTFGenerator
 */
public class VariablesTFGeneratorTest {

  private final static String nifiMachine = "localhost:8080";
  private final static String rootProcessGroup = "aaaa-bbbb-cccc-dddd";
  private final static String expected = "variable \"nifi_host\" {" + TFUtil.NEWLINE + TFUtil.TAB
      + "description = \"NiFi instance where the flow should be created\"" + TFUtil.NEWLINE
      + TFUtil.TAB + "default = \"" + nifiMachine + "\"" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE
      + TFUtil.NEWLINE + "variable \"nifi_root_process_group_id\" {" + TFUtil.NEWLINE + TFUtil.TAB
      + "description = \"ID of process group where flow resources should be created\""
      + TFUtil.NEWLINE + TFUtil.TAB + "default = \"" + rootProcessGroup + "\"" + TFUtil.NEWLINE
      + "}" + TFUtil.NEWLINE;

  private final static String expectedNoNifi = "variable \"nifi_host\" {" + TFUtil.NEWLINE
      + TFUtil.TAB + "description = \"NiFi instance where the flow should be created\""
      + TFUtil.NEWLINE + TFUtil.TAB + "default = \"localhost:8080\"" + TFUtil.NEWLINE + "}"
      + TFUtil.NEWLINE + TFUtil.NEWLINE + "variable \"nifi_root_process_group_id\" {"
      + TFUtil.NEWLINE + TFUtil.TAB
      + "description = \"ID of process group where flow resources should be created\""
      + TFUtil.NEWLINE + TFUtil.TAB + "default = \"" + rootProcessGroup + "\"" + TFUtil.NEWLINE
      + "}" + TFUtil.NEWLINE;

  @Test
  public void testGenerateVariablesTF() {
    String actual = VariablesTFGenerator.generateVariablesTF(nifiMachine, rootProcessGroup);
    assertEquals("Response id not matching the Expected string", expected, actual);
  }

  @Test
  public void testGenerateVariablesTFNoNifi() {
    String actual = VariablesTFGenerator.generateVariablesTF(null, rootProcessGroup);
    assertEquals("Response id not matching the Expected string", expectedNoNifi, actual);
  }


}
