/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.generator;

import com.nordstrom.mlsort.tf.TFUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Class to generate variables.tf.
 */
public class VariablesTFGenerator {

  private static String VARIABLES_TF_CONTENT;

  static {
    VARIABLES_TF_CONTENT = "variable \"nifi_host\" {" + TFUtil.NEWLINE + TFUtil.TAB
        + "description = \"NiFi instance where the flow should be created\"" + TFUtil.NEWLINE
        + TFUtil.TAB + "default = \"localhost:8080\"" + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE
        + TFUtil.NEWLINE + "variable \"nifi_root_process_group_id\" {" + TFUtil.NEWLINE + TFUtil.TAB
        + "description = \"ID of process group where flow resources should be created\""
        + TFUtil.NEWLINE + TFUtil.TAB + "default = \"<ROOT_PROCESS_GROUP>\"" + TFUtil.NEWLINE + "}"
        + TFUtil.NEWLINE;
  }

  /**
   * Method to generate variables.tf content.
   * 
   * @param nifiMachine String
   * @param rootProcessGroup String
   * @return variables.tf content
   */
  public static String generateVariablesTF(final String nifiMachine,
      final String rootProcessGroup) {
    VARIABLES_TF_CONTENT = VARIABLES_TF_CONTENT.replace("<ROOT_PROCESS_GROUP>", rootProcessGroup);
    if (StringUtils.isNotBlank(nifiMachine)) {
      VARIABLES_TF_CONTENT = VARIABLES_TF_CONTENT.replace("localhost:8080", nifiMachine);
    }
    return VARIABLES_TF_CONTENT;
  }

}
