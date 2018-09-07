package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Test TFObjBends
 */
public class TFObjBendsTest {

  @Test
  public void testGenerateStringRepresentation() throws IOException {
    TFObjBends tfObjBends = new TFObjBends();
    List<Map<String, Double>> bends = new ArrayList<>();
    Map<String, Double> map = new HashMap<>();
    map.put("key", Double.valueOf(1));
    bends.add(map);
    tfObjBends.setBends(bends);
    String sb = tfObjBends.generateStringRepresentation();
    String expectedElement =
        "bends	{" + TFUtil.NEWLINE + TFUtil.TAB + "bends = [{key=1.0}]" + TFUtil.NEWLINE

            + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate bends object", expectedElement, sb);
  }
}
