//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per ReachableHostsType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ReachableHostsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.example.org/NfvDeployer}host" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="startingNode" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyNode" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReachableHostsType", propOrder = {
    "host"
})
public class ReachableHostsType {

    protected List<HostType> host;
    @XmlAttribute(name = "startingNode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String startingNode;

    /**
     * Gets the value of the host property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the host property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHost().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HostType }
     * 
     * 
     */
    public List<HostType> getHost() {
        if (host == null) {
            host = new ArrayList<HostType>();
        }
        return this.host;
    }

    /**
     * Recupera il valore della proprietà startingNode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartingNode() {
        if (startingNode == null) {
            return "dummyNode";
        } else {
            return startingNode;
        }
    }

    /**
     * Imposta il valore della proprietà startingNode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartingNode(String value) {
        this.startingNode = value;
    }

}
