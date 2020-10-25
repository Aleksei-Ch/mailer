//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.10.25 at 04:34:25 PM MSK 
//


package com.devinotele.smsservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Message complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Message"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DelayUntilUtc" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="DestinationAddresses" type="{http://ws.devinosms.com}ArrayOfString" minOccurs="0"/&gt;
 *         &lt;element name="SourceAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ReceiptRequested" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Validity" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Optionals" type="{http://ws.devinosms.com}ArrayOfString" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", propOrder = {
    "data",
    "delayUntilUtc",
    "destinationAddresses",
    "sourceAddress",
    "receiptRequested",
    "validity",
    "optionals"
})
public class Message {

    @XmlElement(name = "Data")
    protected String data;
    @XmlElement(name = "DelayUntilUtc", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar delayUntilUtc;
    @XmlElement(name = "DestinationAddresses")
    protected ArrayOfString destinationAddresses;
    @XmlElement(name = "SourceAddress")
    protected String sourceAddress;
    @XmlElement(name = "ReceiptRequested")
    protected boolean receiptRequested;
    @XmlElement(name = "Validity", required = true, type = Integer.class, nillable = true)
    protected Integer validity;
    @XmlElement(name = "Optionals")
    protected ArrayOfString optionals;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * Gets the value of the delayUntilUtc property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDelayUntilUtc() {
        return delayUntilUtc;
    }

    /**
     * Sets the value of the delayUntilUtc property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDelayUntilUtc(XMLGregorianCalendar value) {
        this.delayUntilUtc = value;
    }

    /**
     * Gets the value of the destinationAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getDestinationAddresses() {
        return destinationAddresses;
    }

    /**
     * Sets the value of the destinationAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setDestinationAddresses(ArrayOfString value) {
        this.destinationAddresses = value;
    }

    /**
     * Gets the value of the sourceAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Sets the value of the sourceAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceAddress(String value) {
        this.sourceAddress = value;
    }

    /**
     * Gets the value of the receiptRequested property.
     * 
     */
    public boolean isReceiptRequested() {
        return receiptRequested;
    }

    /**
     * Sets the value of the receiptRequested property.
     * 
     */
    public void setReceiptRequested(boolean value) {
        this.receiptRequested = value;
    }

    /**
     * Gets the value of the validity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValidity() {
        return validity;
    }

    /**
     * Sets the value of the validity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValidity(Integer value) {
        this.validity = value;
    }

    /**
     * Gets the value of the optionals property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getOptionals() {
        return optionals;
    }

    /**
     * Sets the value of the optionals property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setOptionals(ArrayOfString value) {
        this.optionals = value;
    }

}