//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per NodeType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}links" minOccurs="0"/>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}reachablehosts" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyNode" />
 *       &lt;attribute name="NF-FG" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyNffg" />
 *       &lt;attribute name="host" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyHost" />
 *       &lt;attribute name="functionalType" use="required" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeType", propOrder = {
    "links",
    "reachablehosts"
})
public class NodeType {

    protected LinksType links;
    protected ReachableHostsType reachablehosts;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlAttribute(name = "NF-FG")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String nffg;
    @XmlAttribute(name = "host")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String host;
    @XmlAttribute(name = "functionalType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String functionalType;

    /**
     * Recupera il valore della proprietà links.
     * 
     * @return
     *     possible object is
     *     {@link LinksType }
     *     
     */
    public LinksType getLinks() {
        return links;
    }

    /**
     * Imposta il valore della proprietà links.
     * 
     * @param value
     *     allowed object is
     *     {@link LinksType }
     *     
     */
    public void setLinks(LinksType value) {
        this.links = value;
    }

    /**
     * Recupera il valore della proprietà reachablehosts.
     * 
     * @return
     *     possible object is
     *     {@link ReachableHostsType }
     *     
     */
    public ReachableHostsType getReachablehosts() {
        return reachablehosts;
    }

    /**
     * Imposta il valore della proprietà reachablehosts.
     * 
     * @param value
     *     allowed object is
     *     {@link ReachableHostsType }
     *     
     */
    public void setReachablehosts(ReachableHostsType value) {
        this.reachablehosts = value;
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
            return "dummyNode";
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
     * Recupera il valore della proprietà nffg.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNFFG() {
        if (nffg == null) {
            return "dummyNffg";
        } else {
            return nffg;
        }
    }

    /**
     * Imposta il valore della proprietà nffg.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNFFG(String value) {
        this.nffg = value;
    }

    /**
     * Recupera il valore della proprietà host.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        if (host == null) {
            return "dummyHost";
        } else {
            return host;
        }
    }

    /**
     * Imposta il valore della proprietà host.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Recupera il valore della proprietà functionalType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionalType() {
        return functionalType;
    }

    /**
     * Imposta il valore della proprietà functionalType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionalType(String value) {
        this.functionalType = value;
    }

}
