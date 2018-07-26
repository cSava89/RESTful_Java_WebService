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
 * <p>Classe Java per PLPtype complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="PLPtype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="host1" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyhost" />
 *       &lt;attribute name="host2" type="{http://www.example.org/NfvDeployer}FirstAlphabeticThenAlphanumeric" default="dummyhost" />
 *       &lt;attribute name="throughput" use="required" type="{http://www.example.org/NfvDeployer}positiveFloat" />
 *       &lt;attribute name="latency" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PLPtype")
public class PLPtype {

    @XmlAttribute(name = "host1")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String host1;
    @XmlAttribute(name = "host2")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String host2;
    @XmlAttribute(name = "throughput", required = true)
    protected float throughput;
    @XmlAttribute(name = "latency", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger latency;

    /**
     * Recupera il valore della proprietà host1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost1() {
        if (host1 == null) {
            return "dummyhost";
        } else {
            return host1;
        }
    }

    /**
     * Imposta il valore della proprietà host1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost1(String value) {
        this.host1 = value;
    }

    /**
     * Recupera il valore della proprietà host2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost2() {
        if (host2 == null) {
            return "dummyhost";
        } else {
            return host2;
        }
    }

    /**
     * Imposta il valore della proprietà host2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost2(String value) {
        this.host2 = value;
    }

    /**
     * Recupera il valore della proprietà throughput.
     * 
     */
    public float getThroughput() {
        return throughput;
    }

    /**
     * Imposta il valore della proprietà throughput.
     * 
     */
    public void setThroughput(float value) {
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
        return latency;
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

}
