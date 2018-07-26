//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per LinkType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="LinkType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" />
 *       &lt;attribute name="sourceNode" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummySrcNode" />
 *       &lt;attribute name="destinationNode" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyDstNode" />
 *       &lt;attribute name="throughput" type="{http://www.example.org/NfvDeployer}positiveFloat" default="0.0" />
 *       &lt;attribute name="latency" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *       &lt;attribute name="overwrite" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LinkType")
public class LinkType {

    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "sourceNode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sourceNode;
    @XmlAttribute(name = "destinationNode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String destinationNode;
    @XmlAttribute(name = "throughput")
    protected Float throughput;
    @XmlAttribute(name = "latency")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger latency;
    @XmlAttribute(name = "overwrite")
    protected Boolean overwrite;

    /**
     * Recupera il valore della proprietà name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta il valore della proprietà name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Recupera il valore della proprietà sourceNode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceNode() {
        if (sourceNode == null) {
            return "dummySrcNode";
        } else {
            return sourceNode;
        }
    }

    /**
     * Imposta il valore della proprietà sourceNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceNode(String value) {
        this.sourceNode = value;
    }

    /**
     * Recupera il valore della proprietà destinationNode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationNode() {
        if (destinationNode == null) {
            return "dummyDstNode";
        } else {
            return destinationNode;
        }
    }

    /**
     * Imposta il valore della proprietà destinationNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationNode(String value) {
        this.destinationNode = value;
    }

    /**
     * Recupera il valore della proprietà throughput.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public float getThroughput() {
        if (throughput == null) {
            return  0.0F;
        } else {
            return throughput;
        }
    }

    /**
     * Imposta il valore della proprietà throughput.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setThroughput(Float value) {
        this.throughput = value;
    }

    /**
     * Recupera il valore della proprietà latency.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLatency() {
        if (latency == null) {
            return new BigInteger("0");
        } else {
            return latency;
        }
    }

    /**
     * Imposta il valore della proprietà latency.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLatency(BigInteger value) {
        this.latency = value;
    }

    /**
     * Recupera il valore della proprietà overwrite.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOverwrite() {
        return overwrite;
    }

    /**
     * Imposta il valore della proprietà overwrite.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverwrite(Boolean value) {
        this.overwrite = value;
    }

}
