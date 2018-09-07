package com.nordstrom.mlsort.tf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.Test;

/**
 * Test class for TFUtil
 */
public class TFUtilTest {

  @Test
  public void testGetElementNameFromClassName() throws IOException {
    String className = TFObjBends.class.getName();
    String elementName = TFUtil.getElementNameFromClassName(className);
    assertEquals("Failed to get element name from class name", "bends", elementName);
  }

  @Test
  public void testGetElementNameFromClassNameWith4() throws IOException {
    String className = TFObjComponent4Connection.class.getName();
    String elementName = TFUtil.getElementNameFromClassName(className);
    assertEquals("Failed to get element name from class name with 4", "component", elementName);
  }

  @Test
  public void testGetFieldNameAndValues() throws IOException, IllegalAccessException {
    List<String> list = TFUtil.getFieldNameAndValues(new TFObjComponent4Funnel(), false);
    assertTrue("Failed to get field name and value", list.get(0).contains("parent_group_id"));
    assertTrue("Failed to get field name and value", list.get(1).contains("tfObjPosition"));

  }

  @Test
  public void testGetFieldNameAndValuesWithWrapDblQut() throws IOException, IllegalAccessException {
    List<String> list = TFUtil.getFieldNameAndValues(new TFObjComponent4Funnel(), true);
    assertTrue("Failed to get field name and value with double quote",
        list.get(0).contains("\"parent_group_id\""));
    assertTrue("Failed to get field name and value with double quote",
        list.get(1).contains("\"tfObjPosition\""));
  }

  @Test
  public void testGetFieldNameAndValuesWithPropertiesField()
      throws IOException, IllegalAccessException {
    TFObjComponent4ControllerService tfObjComponent4ControllerService =
        new TFObjComponent4ControllerService();
    Properties properties = new Properties();
    properties.setProperty("key", "value");
    tfObjComponent4ControllerService.setProperties(properties);
    List<String> list = TFUtil.getFieldNameAndValues(tfObjComponent4ControllerService, false);
    assertTrue("Failed to get field name and value with properties field",
        list.get(3).contains("\"key\" = \"value\""));

  }

  @Test
  public void testGetFieldNameAndValuesWithStringArrayField()
      throws IOException, IllegalAccessException {
    TFObjConfig tFObjConfig = new TFObjConfig();
    String[] array = {"A", "B"};
    tFObjConfig.setAuto_terminated_relationships(array);
    List<String> list = TFUtil.getFieldNameAndValues(tFObjConfig, false);
    assertTrue("Failed to get field name and value with string array field",
        list.get(5).contains("\"A\",\"B\""));
  }

  @Test
  public void testShiftRight() {
    String multiline = "A\nB";
    StringBuilder sb = TFUtil.shiftRight(multiline);
    String[] lines = sb.toString().split(TFUtil.NEWLINE);
    assertTrue("Failed to shift element right", lines.length == 3);
    assertTrue("Failed to shift element right", lines[0].trim().equals(""));
    assertTrue("Failed to shift element right", lines[1].startsWith(TFUtil.TAB));
    assertTrue("Failed to shift element right", lines[2].startsWith(TFUtil.TAB));
  }

  @Test
  public void testGenerateStringNotChild() {
    List<String> list = new ArrayList<>();
    list.add("property");
    String sb = TFUtil.generateString("ELEM_NAME", list, false);
    String expectedElement = "ELEM_NAME	{" + TFUtil.NEWLINE + TFUtil.TAB + "property"
        + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate element without a child", expectedElement, sb.toString());
  }

  @Test
  public void testPopulateProperties() {

    Properties properties = new Properties();
    properties.setProperty("key1", "A\\B");
    properties.setProperty("key2", "A\"B");
    properties.setProperty("key3", "A\nB");
    properties.setProperty("key4", "${}");
    properties.setProperty("key5", "\n");
    properties.setProperty("key6", "<<EOF Hello EOF");

    String sb = TFUtil.populateProperties(properties);
    String expectedElement = "properties	{" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB
        + "\"key6\" = <<EOF Hello EOF" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB
        + "\"key5\" = <<EOF\nEOF" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB + "\"key4\" = \"$${}\""
        + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB + "\"key3\" = \"AB\"" + TFUtil.NEWLINE
        + TFUtil.TAB + TFUtil.TAB + "\"key2\" = \"A\\\"B\"" + TFUtil.NEWLINE + TFUtil.TAB
        + TFUtil.TAB + "\"key1\" = \"A\\\\B\"" + TFUtil.NEWLINE + TFUtil.TAB + "}" + TFUtil.NEWLINE;
    assertEquals("Failed to generate properties element", expectedElement, sb);
  }

  @Test
  public void testPopulateArray() {
    String[] array = {"A", "B"};
    String sb = TFUtil.populateArray("ELEMENT", array);
    String expectedElement = "ELEMENT	=	[" + TFUtil.NEWLINE + TFUtil.TAB + TFUtil.TAB
        + "\"A\",\"B\"" + TFUtil.NEWLINE + TFUtil.TAB + "]" + TFUtil.NEWLINE;

    assertEquals("Failed to generate array element", expectedElement, sb);
  }

  @Test
  public void testGetProvider() {
    String host = "HOST";
    String sb = TFUtil.getProvider(host);

    String expectedElement = "provider \"nifi\" {" + TFUtil.NEWLINE + TFUtil.TAB + "host = \"HOST\""
        + TFUtil.NEWLINE + "}" + TFUtil.NEWLINE + TFUtil.NEWLINE;

    assertEquals("Failed to generate nifi provider", expectedElement, sb);
  }

}
