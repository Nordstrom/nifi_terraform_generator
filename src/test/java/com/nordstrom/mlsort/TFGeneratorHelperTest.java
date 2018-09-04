/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.bind.JAXBException;
import org.junit.Test;

/**
 * Test TFGeneratorHelper
 */
public class TFGeneratorHelperTest {

  @Test
  public void testExecuteMainActions() throws IOException, URISyntaxException, JAXBException {
    String[] args = new String[1];
    TFGeneratorHelper.executeMainActions(args);

    String path = TFGenerator.class.getResource("/").getFile();
    File directory = new File(path + "../");
    int count = 0;
    if (directory.isDirectory()) {
      for (File f : directory.listFiles()) {
        if (f.getName().endsWith(".tf")) {
          count++;
        }
      }
    }
    assertTrue("Failed to generate the terraform files for the flow.xml", count >= 4);
  }
}
