//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per EnumeratedFunctionalTypes.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="EnumeratedFunctionalTypes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CACHE"/>
 *     &lt;enumeration value="DPI"/>
 *     &lt;enumeration value="FW"/>
 *     &lt;enumeration value="MAIL_CLIENT"/>
 *     &lt;enumeration value="MAIL_SERVER"/>
 *     &lt;enumeration value="NAT"/>
 *     &lt;enumeration value="SPAM"/>
 *     &lt;enumeration value="VPN"/>
 *     &lt;enumeration value="WEB_CLIENT"/>
 *     &lt;enumeration value="WEB_SERVER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EnumeratedFunctionalTypes")
@XmlEnum
public enum EnumeratedFunctionalTypes {

    CACHE,
    DPI,
    FW,
    MAIL_CLIENT,
    MAIL_SERVER,
    NAT,
    SPAM,
    VPN,
    WEB_CLIENT,
    WEB_SERVER;

    public String value() {
        return name();
    }

    public static EnumeratedFunctionalTypes fromValue(String v) {
        return valueOf(v);
    }

}
