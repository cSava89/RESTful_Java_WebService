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
 * <p>Classe Java per HostType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="HostType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}virtualizednodes" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyHost" />
 *       &lt;attribute name="numberOfVNFs" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *       &lt;attribute name="amountOfMemory" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *       &lt;attribute name="diskStorage" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" default="0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostType", propOrder = {
    "virtualizednodes"
})
public class HostType {

    protected VirtualizedNodesType virtualizednodes;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "numberOfVNFs")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfVNFs;
    @XmlAttribute(name = "amountOfMemory")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger amountOfMemory;
    @XmlAttribute(name = "diskStorage")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger diskStorage;

    /**
     * Recupera il valore della proprietà virtualizednodes.
     * 
     * @return
     *     possible object is
     *     {@link VirtualizedNodesType }
     *     
     */
    public VirtualizedNodesType getVirtualizednodes() {
        return virtualizednodes;
    }

    /**
     * Imposta il valore della proprietà virtualizednodes.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualizedNodesType }
     *     
     */
    public void setVirtualizednodes(VirtualizedNodesType value) {
        this.virtualizednodes = value;
    }

    /**
     * Recupera il valore della proprietà name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        if (name == null) {
            return "dummyHost";
        } else {
            return name;
        }
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
     * Recupera il valore della proprietà numberOfVNFs.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfVNFs() {
        if (numberOfVNFs == null) {
            return new BigInteger("0");
        } else {
            return numberOfVNFs;
        }
    }

    /**
     * Imposta il valore della proprietà numberOfVNFs.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfVNFs(BigInteger value) {
        this.numberOfVNFs = value;
    }

    /**
     * Recupera il valore della proprietà amountOfMemory.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAmountOfMemory() {
        if (amountOfMemory == null) {
            return new BigInteger("0");
        } else {
            return amountOfMemory;
        }
    }

    /**
     * Imposta il valore della proprietà amountOfMemory.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAmountOfMemory(BigInteger value) {
        this.amountOfMemory = value;
    }

    /**
     * Recupera il valore della proprietà diskStorage.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDiskStorage() {
        if (diskStorage == null) {
            return new BigInteger("0");
        } else {
            return diskStorage;
        }
    }

    /**
     * Imposta il valore della proprietà diskStorage.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDiskStorage(BigInteger value) {
        this.diskStorage = value;
    }

}
