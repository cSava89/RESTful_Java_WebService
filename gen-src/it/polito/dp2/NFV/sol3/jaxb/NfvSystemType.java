//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per NfvSystemType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="NfvSystemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}nffgs"/>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}hosts"/>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}connections"/>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}catalogue"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NfvSystemType", propOrder = {
    "nffgs",
    "hosts",
    "connections",
    "catalogue"
})
public class NfvSystemType {

    @XmlElement(required = true)
    protected NFFGsType nffgs;
    @XmlElement(required = true)
    protected HostsType hosts;
    @XmlElement(required = true)
    protected ConnectionsType connections;
    @XmlElement(required = true)
    protected CatalogueType catalogue;

    /**
     * Recupera il valore della proprietà nffgs.
     * 
     * @return
     *     possible object is
     *     {@link NFFGsType }
     *     
     */
    public NFFGsType getNffgs() {
        return nffgs;
    }

    /**
     * Imposta il valore della proprietà nffgs.
     * 
     * @param value
     *     allowed object is
     *     {@link NFFGsType }
     *     
     */
    public void setNffgs(NFFGsType value) {
        this.nffgs = value;
    }

    /**
     * Recupera il valore della proprietà hosts.
     * 
     * @return
     *     possible object is
     *     {@link HostsType }
     *     
     */
    public HostsType getHosts() {
        return hosts;
    }

    /**
     * Imposta il valore della proprietà hosts.
     * 
     * @param value
     *     allowed object is
     *     {@link HostsType }
     *     
     */
    public void setHosts(HostsType value) {
        this.hosts = value;
    }

    /**
     * Recupera il valore della proprietà connections.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionsType }
     *     
     */
    public ConnectionsType getConnections() {
        return connections;
    }

    /**
     * Imposta il valore della proprietà connections.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionsType }
     *     
     */
    public void setConnections(ConnectionsType value) {
        this.connections = value;
    }

    /**
     * Recupera il valore della proprietà catalogue.
     * 
     * @return
     *     possible object is
     *     {@link CatalogueType }
     *     
     */
    public CatalogueType getCatalogue() {
        return catalogue;
    }

    /**
     * Imposta il valore della proprietà catalogue.
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogueType }
     *     
     */
    public void setCatalogue(CatalogueType value) {
        this.catalogue = value;
    }

}
