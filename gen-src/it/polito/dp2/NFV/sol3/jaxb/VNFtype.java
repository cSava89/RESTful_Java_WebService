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
 * <p>Classe Java per VNFtype complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VNFtype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyVNF" />
 *       &lt;attribute name="type" use="required" type="{http://www.example.org/NfvDeployer}EnumeratedFunctionalTypes" />
 *       &lt;attribute name="amountOfMemory" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="diskStorage" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VNFtype")
public class VNFtype {

    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "type", required = true)
    protected EnumeratedFunctionalTypes type;
    @XmlAttribute(name = "amountOfMemory", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger amountOfMemory;
    @XmlAttribute(name = "diskStorage", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger diskStorage;

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
            return "dummyVNF";
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
     * Recupera il valore della proprietà type.
     * 
     * @return
     *     possible object is
     *     {@link EnumeratedFunctionalTypes }
     *     
     */
    public EnumeratedFunctionalTypes getType() {
        return type;
    }

    /**
     * Imposta il valore della proprietà type.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumeratedFunctionalTypes }
     *     
     */
    public void setType(EnumeratedFunctionalTypes value) {
        this.type = value;
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
        return amountOfMemory;
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
        return diskStorage;
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
