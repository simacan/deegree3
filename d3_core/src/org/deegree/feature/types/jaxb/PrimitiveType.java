//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.19 at 03:38:24 PM MEZ 
//


package org.deegree.feature.types.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrimitiveType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrimitiveType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="decimal"/>
 *     &lt;enumeration value="double"/>
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="dateTime"/>
 *     &lt;enumeration value="time"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrimitiveType")
@XmlEnum
public enum PrimitiveType {


    /**
     * String-valued property (=xs:string)
     * 
     */
    @XmlEnumValue("string")
    STRING("string"),

    /**
     * Boolean-valued property (=xs:boolean)
     * 
     */
    @XmlEnumValue("boolean")
    BOOLEAN("boolean"),

    /**
     * Decimal-valued property (=xs:decimal)
     * 
     */
    @XmlEnumValue("decimal")
    DECIMAL("decimal"),

    /**
     * Double-valued property (=xs:double)
     * 
     */
    @XmlEnumValue("double")
    DOUBLE("double"),

    /**
     * Integer-valued property (=xs:integer)
     * 
     */
    @XmlEnumValue("integer")
    INTEGER("integer"),

    /**
     * Date-valued property (=xs:date)
     * 
     */
    @XmlEnumValue("date")
    DATE("date"),

    /**
     * Date-valued property (=xs:dateTime)
     * 
     */
    @XmlEnumValue("dateTime")
    DATE_TIME("dateTime"),

    /**
     * Time-valued property (=xs:time)
     * 
     */
    @XmlEnumValue("time")
    TIME("time");
    private final String value;

    PrimitiveType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrimitiveType fromValue(String v) {
        for (PrimitiveType c: PrimitiveType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
