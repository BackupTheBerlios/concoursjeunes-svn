
package org.concoursjeunes.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pluginDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pluginDescription">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="logicalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shortDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="minVersionApp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maxVersionApp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pluginDescription", propOrder = {
    "logicalName",
    "displayName",
    "version",
    "shortDescription",
    "longDescription",
    "minVersionApp",
    "maxVersionApp"
})
public class PluginDescription {

    protected String logicalName;
    protected String displayName;
    protected String version;
    protected String shortDescription;
    protected String longDescription;
    protected String minVersionApp;
    protected String maxVersionApp;

    /**
     * Gets the value of the logicalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalName() {
        return logicalName;
    }

    /**
     * Sets the value of the logicalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalName(String value) {
        this.logicalName = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the longDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Sets the value of the longDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongDescription(String value) {
        this.longDescription = value;
    }

    /**
     * Gets the value of the minVersionApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinVersionApp() {
        return minVersionApp;
    }

    /**
     * Sets the value of the minVersionApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinVersionApp(String value) {
        this.minVersionApp = value;
    }

    /**
     * Gets the value of the maxVersionApp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxVersionApp() {
        return maxVersionApp;
    }

    /**
     * Sets the value of the maxVersionApp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxVersionApp(String value) {
        this.maxVersionApp = value;
    }

}
