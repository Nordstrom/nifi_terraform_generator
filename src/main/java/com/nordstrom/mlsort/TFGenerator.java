/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

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
