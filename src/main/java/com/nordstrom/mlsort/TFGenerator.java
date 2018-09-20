package com.nordstrom.mlsort;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.bind.JAXBException;


/**
 * Main classfrom where execution starts.
 *
 */
public class TFGenerator {

  /**
   * Starting execution point to convert xml to terraform.
   * 
   * @param args String[]
   * @throws URISyntaxException URISyntaxException
   * @throws IOException IOException
   * @throws JAXBException JAXBException
   */
  public static void main(final String[] args)
      throws URISyntaxException, IOException, JAXBException {
    TFGeneratorHelper.executeMainActions(args);
  }

}
