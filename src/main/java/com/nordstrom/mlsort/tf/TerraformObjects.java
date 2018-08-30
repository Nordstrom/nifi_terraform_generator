/**
 * Copyright(c) Nordstrom,Inc. All Rights reserved. This software is the confidential and
 * proprietary information of Nordstrom, Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Nordstrom, Inc.
 */

package com.nordstrom.mlsort.tf;

/**
 * Interface representing Terraform Objects.
 */
public interface TerraformObjects {

  /**
   * Method to generate String output of element.
   * 
   * @return String - the string representation of Terraform
   */
  String generateStringRepresentation();

}
