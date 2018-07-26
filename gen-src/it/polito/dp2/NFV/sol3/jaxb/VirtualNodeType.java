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
 * <p>Classe Java per VirtualNodeType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VirtualNodeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="nodeName" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyNode" />
 *       &lt;attribute name="nodeNFFG" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyNffg" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualNodeType")
public class VirtualNodeType {

    @XmlAttribute(name = "nodeName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String nodeName;
    @XmlAttribute(name = "nodeNFFG")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String nodeNFFG;

    /**
     * Recupera il valore della proprietà nodeName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeName() {
        if (nodeName == null) {
            return "dummyNode";
        } else {
            return nodeName;
        }
    }

    /**
     * Imposta il valore della proprietà nodeName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Recupera il valore della proprietà nodeNFFG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeNFFG() {
        if (nodeNFFG == null) {
            return "dummyNffg";
        } else {
            return nodeNFFG;
        }
    }

    /**
     * Imposta il valore della proprietà nodeNFFG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeNFFG(String value) {
        this.nodeNFFG = value;
    }

}
