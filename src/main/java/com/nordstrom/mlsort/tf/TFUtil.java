package com.nordstrom.mlsort.tf;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.lang.reflect.Field;

/**
 * Utility class to help the creation of terraform file.
 */
public class TFUtil {

  private static final int SIX = 6;
  public static final String TAB = "\t";
  public static final String NEWLINE = "\n";
  public static final String OPEN_BRACE = "{";
  public static final String CLOSE_BRACE = "}";
  public static final String OPEN_SQ_BRACE = "[";
  public static final String CLOSE_SQ_BRACE = "]";
  public static final String EQUAL = "=";
  public static final String COMMA = ",";
  public static final String DOUBLE_QUOTE = "\"";
  public static final String SPACE = " ";

  /**
   * Method to get the element name from class name.
   * 
   * @param className String
   * @return String element name
   */
  public static String getElementNameFromClassName(String className) {
    if (className.contains("4")) {
      className = className.substring(0, className.indexOf("4"));
    }
    return className.substring(className.lastIndexOf(".") + SIX, className.length()).toLowerCase();
  }

  /**
   * Method to get field names and values.
   * 
   * @param object Object
   * @param wrapDblQut boolean
   * @return List<String> field name and value list
   * @throws IllegalAccessException IllegalAccessException
   */
  public static List<String> getFieldNameAndValues(final Object object, final boolean wrapDblQut)
      throws IllegalAccessException {
    Class<?> cls = object.getClass();
    Field[] fields = cls.getDeclaredFields();
    List<String> list = new ArrayList<>();
    for (Field field : fields) {
      // NotNull notNull = field.getAnnotation(NotNull.class);
      if (!field.isSynthetic()) {
        field.setAccessible(true);
        String name = field.getName();
        Object value = field.get(object);
        if (value instanceof Properties) {
          list.add(TFUtil.populateProperties((Properties) value));
        } else if (value instanceof String[]) {
          list.add(TFUtil.populateArray(name, (String[]) value));
        } else if (value instanceof TerraformObjects) {
          String tfElement = ((TerraformObjects) value).generateStringRepresentation();
          // For shifting the whole element to right
          StringBuilder sb = shiftRight(tfElement);
          list.add(sb.toString());
        } else if (value instanceof TFObjInput_Ports[]) {
          TFObjInput_Ports[] array = (TFObjInput_Ports[]) value;

          String start = "input_ports     = [";
          String end = "]";
          StringBuilder sb = new StringBuilder(start).append(NEWLINE);
          int size = 0;
          for (TFObjInput_Ports tfObjInputPorts : array) {
            ++size;
            sb.append(shiftRight(
                shiftRight(tfObjInputPorts.generateStringRepresentation().substring(12).trim())
                    .toString()));
            if (size != array.length) {
              sb.append(TAB).append(TAB).append(COMMA);
            }
          }
          sb.append(TAB).append(end);
          sb.append(NEWLINE);
          list.add(sb.toString());
        } else if (value instanceof TFObjOutput_Ports[]) {
          TFObjOutput_Ports[] array = (TFObjOutput_Ports[]) value;

          String start = "output_ports     = [";
          String end = "]";
          StringBuilder sb = new StringBuilder(start).append(NEWLINE);
          int size = 0;
          for (TFObjOutput_Ports tfObjOutputPorts : array) {
            ++size;
            sb.append(shiftRight(
                shiftRight(tfObjOutputPorts.generateStringRepresentation().substring(12).trim())
                    .toString()));
            if (size != array.length) {
              sb.append(TAB).append(TAB).append(COMMA);
            }
          }
          sb.append(TAB).append(end);
          list.add(sb.toString());
        } else if (value instanceof TerraformObjects[]) {
          for (TerraformObjects terraformObject : (TerraformObjects[]) value) {
            String tfElement = terraformObject.generateStringRepresentation();
            // For shifting the whole element to right
            StringBuilder sb = shiftRight(tfElement);
            list.add(sb.toString());
          }
        } else {
          if (wrapDblQut) {
            list.add(DOUBLE_QUOTE + name.trim() + DOUBLE_QUOTE + " = " + DOUBLE_QUOTE + value
                + DOUBLE_QUOTE);
          } else {
            if (value instanceof String) {
              list.add(name.trim() + " = " + DOUBLE_QUOTE + value + DOUBLE_QUOTE);
            } else {
              list.add(name.trim() + " = " + value);
            }

          }
        }
      }
    }
    return list;
  }

  /**
   * Method to shift the whole element right with one tab space.
   * 
   * @param tfElement String
   * @return StringBuilder StringBuilder object
   */
  public static StringBuilder shiftRight(final String tfElement) {
    StringBuilder sb = new StringBuilder(NEWLINE);
    String[] array = tfElement.split(NEWLINE);
    for (String string : array) {
      sb.append(TAB).append(string).append(NEWLINE);
    }
    return sb;
  }

  /**
   * Method to generate string from element name and list of child objects
   * 
   * @param elementName String
   * @param list List<String>
   * @return String terraform element
   */
  public static String generateString(final String elementName, final List<String> list,
      final boolean isChild) {
    StringBuilder sb =
        new StringBuilder(elementName).append(TAB).append(OPEN_BRACE).append(NEWLINE);
    for (String element : list) {
      sb.append(TAB).append(element).append(NEWLINE);
    }
    if (isChild) {
      sb.append(TAB).append(CLOSE_BRACE).append(NEWLINE);
    } else {
      sb.append(CLOSE_BRACE).append(NEWLINE);
    }

    return sb.toString();
  }

  /**
   * Read from properties file and generate element.
   * 
   * @param properties Properties
   * @return String terraform element
   */
  public static String populateProperties(final Properties properties) {
    List<String> list = new ArrayList<>();
    Enumeration<Object> keys = properties.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      if (key.contains("\"")) {
        key = key.replace("\"", "\\\"");
      }
      String value = (String) properties.getProperty(key);
      if (StringUtils.isNotBlank(value)) {
        if (value.contains("\\")) {
          value = value.replace("\\", "\\\\");
        }
        if (value.contains("\"")) {
          value = value.replace("\"", "\\\"");
        }
        if (value.contains("\n")) {
          value = value.replace("\n", "");
        }
        if (value.contains("${")) {
          value = value.replace("${", "$${");
          if (value.contains("(\\\"")) {
            value = value.replace("(\\\"", "(\"");
          }
          if (value.contains("\\\")")) {
            value = value.replace("\\\")", "\")");
          }
        }
      } else {
        if (value.contains("\n")) {
          if (value.equals("\n")) {
            value = "<<EOF" + NEWLINE + "EOF";
          } else if (StringUtils.isNotBlank(value)) {
            value = value.replace("\n", "");
            // value = "<<EOF" + NEWLINE + "\"" + value + "\"" +
            // NEWLINE
            // + "EOF";
          }
        }
      }
      if (value.contains("<<EOF")) {
        list.add(TAB + DOUBLE_QUOTE + key.trim() + DOUBLE_QUOTE + " = " + value.trim());
      } else {
        list.add(TAB + DOUBLE_QUOTE + key.trim() + DOUBLE_QUOTE + " = " + DOUBLE_QUOTE
            + value.trim() + DOUBLE_QUOTE);
      }
    }

    return generateString("properties", list, true);
  }

  /**
   * Read from String[] and generate element.
   * 
   * @param elementName String
   * @param value String[]
   * @return String terraform element
   */
  public static String populateArray(final String elementName, final String[] value) {
    StringBuilder sb = new StringBuilder(elementName.trim()).append(TAB).append(EQUAL).append(TAB)
        .append(OPEN_SQ_BRACE).append(NEWLINE).append(TAB).append(TAB);
    for (String element : value) {
      if (StringUtils.isNotBlank(element)) {
        sb.append(DOUBLE_QUOTE).append(element.trim()).append(DOUBLE_QUOTE).append(COMMA);
      }
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(NEWLINE).append(TAB).append(CLOSE_SQ_BRACE).append(NEWLINE);
    return sb.toString();
  }

  /**
   * To get the terraform element corresponding to the provider.
   * 
   * @param nifiHost String
   * @return String the tf corresponding to provider
   */
  public static String getProvider(final String nifiHost) {

    StringBuilder sb =
        new StringBuilder().append("provider").append(SPACE).append("\"nifi\"").append(SPACE);
    sb.append(OPEN_BRACE).append(NEWLINE).append(TAB).append("host = \"").append(nifiHost)
        .append(DOUBLE_QUOTE).append(NEWLINE);
    sb.append(CLOSE_BRACE).append(NEWLINE).append(NEWLINE);
    return sb.toString();
  }

}
