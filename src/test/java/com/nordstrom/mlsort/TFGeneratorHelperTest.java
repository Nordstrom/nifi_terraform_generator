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
    String[] args = new String[0];
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
