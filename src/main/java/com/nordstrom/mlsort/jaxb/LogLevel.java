//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.07.13 at 11:44:21 AM IST 
//


package com.nordstrom.mlsort.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LogLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LogLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TRACE"/>
 *     &lt;enumeration value="DEBUG"/>
 *     &lt;enumeration value="INFO"/>
 *     &lt;enumeration value="WARN"/>
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="FATAL"/>
 *     &lt;enumeration value="NONE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LogLevel")
@XmlEnum
public enum LogLevel {

    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
    NONE;

    public String value() {
        return name();
    }

    public static LogLevel fromValue(String v) {
        return valueOf(v);
    }

}
