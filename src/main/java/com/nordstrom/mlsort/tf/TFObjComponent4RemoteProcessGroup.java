package com.nordstrom.mlsort.tf;

import java.util.List;

/**
 * To create Component terraform element for remote process group.
 */
public class TFObjComponent4RemoteProcessGroup implements TerraformObjects {

  private String parent_group_id;
  private String name;
  private TFObjPosition tfObjPosition;
  private String targetUris;
  private String yieldPeriod;
  private String timeout;
  private String transportProtocol;
  private String proxyHost;
  private String proxyUser;
  private TFObjContents tfObjContents;

  /**
   * The getter for yieldPeriod.
   * 
   * @return the yieldPeriod
   */
  public String getYieldPeriod() {
    return yieldPeriod;
  }

  /**
   * The setter for yieldPeriod.
   * 
   * @param yieldPeriod the yieldPeriod to set
   */
  public void setYieldPeriod(String yieldPeriod) {
    this.yieldPeriod = yieldPeriod;
  }

  /**
   * The getter for timeout.
   * 
   * @return the timeout
   */
  public String getTimeout() {
    return timeout;
  }

  /**
   * The setter for timeout.
   * 
   * @param timeout the timeout to set
   */
  public void setTimeout(String timeout) {
    this.timeout = timeout;
  }

  /**
   * The getter for proxyHost.
   * 
   * @return the proxyHost
   */
  public String getProxyHost() {
    return proxyHost;
  }

  /**
   * The setter for proxyHost.
   * 
   * @param proxyHost the proxyHost to set
   */
  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }

  /**
   * The getter for proxyUser.
   * 
   * @return the proxyUser
   */
  public String getProxyUser() {
    return proxyUser;
  }

  /**
   * The setter for proxyUser.
   * 
   * @param proxyUser the proxyUser to set
   */
  public void setProxyUser(String proxyUser) {
    this.proxyUser = proxyUser;
  }

  /**
   * The getter for tfObjContents.
   * 
   * @return the tfObjContents
   */
  public TFObjContents getTfObjContents() {
    return tfObjContents;
  }

  /**
   * The setter for tfObjContents.
   * 
   * @param tfObjContents the tfObjContents to set
   */
  public void setTfObjContents(TFObjContents tfObjContents) {
    this.tfObjContents = tfObjContents;
  }

  /**
   * The getter for targetUris.
   * 
   * @return the targetUris
   */
  public String getTargetUris() {
    return targetUris;
  }

  /**
   * The setter for targetUris.
   * 
   * @param targetUris the targetUris to set
   */
  public void setTargetUris(String targetUris) {
    this.targetUris = targetUris;
  }

  /**
   * The getter for transportProtocol.
   * 
   * @return the transportProtocol
   */
  public String getTransportProtocol() {
    return transportProtocol;
  }

  /**
   * The setter for transportProtocol.
   * 
   * @param transportProtocol the transportProtocol to set
   */
  public void setTransportProtocol(String transportProtocol) {
    this.transportProtocol = transportProtocol;
  }

  /**
   * The getter for parent_group_id.
   * 
   * @return the parent_group_id
   */
  public String getParent_group_id() {
    return parent_group_id;
  }

  /**
   * The setter for parent_group_id.
   * 
   * @param parent_group_id the parent_group_id to set
   */
  public void setParent_group_id(String parent_group_id) {
    this.parent_group_id = parent_group_id;
  }

  /**
   * The getter for name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * The setter for name.
   * 
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The getter for tfObjPosition.
   * 
   * @return the tfObjPosition
   */
  public TFObjPosition getTfObjPosition() {
    return tfObjPosition;
  }

  /**
   * The setter for tfObjPosition.
   * 
   * @param tfObjPosition the tfObjPosition to set
   */
  public void setTfObjPosition(TFObjPosition tfObjPosition) {
    this.tfObjPosition = tfObjPosition;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.nordstrom.mlsort.tf.TerraformObjects#generateStringRepresentation()
   */
  @Override
  public String generateStringRepresentation() {
    String element = null;
    try {
      List<String> list = TFUtil.getFieldNameAndValues(this, false);
      element = TFUtil.generateString(TFUtil.getElementNameFromClassName(this.getClass().getName()),
          list, false);
    } catch (IllegalAccessException exception) {
      exception.printStackTrace();
    }
    return element;
  }

}
