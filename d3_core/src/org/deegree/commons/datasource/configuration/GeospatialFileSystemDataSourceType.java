//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-792 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.19 at 03:09:15 PM MEZ 
//


package org.deegree.commons.datasource.configuration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         The filesystem is a common datasource, examples are rasters, elevationmodels or xml files. Heck... every file can be a datasource.
 *       
 * 
 * <p>Java class for GeospatialFileSystemDataSourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeospatialFileSystemDataSourceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.deegree.org/datasource}AbstractGeospatialDataSourceType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.deegree.org/datasource}AbstractFile" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.deegree.org/datasource}AbstractFileSet"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeospatialFileSystemDataSourceType", propOrder = {
    "abstractFile",
    "abstractFileSet"
})
public class GeospatialFileSystemDataSourceType
    extends AbstractGeospatialDataSourceType
{

    @XmlElementRef(name = "AbstractFile", namespace = "http://www.deegree.org/datasource", type = JAXBElement.class)
    protected List<JAXBElement<? extends FileType>> abstractFile;
    @XmlElementRef(name = "AbstractFileSet", namespace = "http://www.deegree.org/datasource", type = JAXBElement.class)
    protected JAXBElement<? extends FileSetType> abstractFileSet;

    /**
     * Gets the value of the abstractFile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractFile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link FileType }{@code >}
     * {@link JAXBElement }{@code <}{@link FileType }{@code >}
     * {@link JAXBElement }{@code <}{@link RasterFileType }{@code >}
     * {@link JAXBElement }{@code <}{@link FileType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends FileType>> getAbstractFile() {
        if (abstractFile == null) {
            abstractFile = new ArrayList<JAXBElement<? extends FileType>>();
        }
        return this.abstractFile;
    }

    /**
     * Gets the value of the abstractFileSet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FileSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RasterFileSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FileSetType }{@code >}
     *     
     */
    public JAXBElement<? extends FileSetType> getAbstractFileSet() {
        return abstractFileSet;
    }

    /**
     * Sets the value of the abstractFileSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FileSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RasterFileSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link FileSetType }{@code >}
     *     
     */
    public void setAbstractFileSet(JAXBElement<? extends FileSetType> value) {
        this.abstractFileSet = ((JAXBElement<? extends FileSetType> ) value);
    }

}
