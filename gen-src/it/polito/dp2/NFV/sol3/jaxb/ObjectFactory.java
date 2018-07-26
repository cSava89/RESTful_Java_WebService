//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2018.06.26 alle 11:25:27 PM CEST 
//


package it.polito.dp2.NFV.sol3.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.dp2.NFV.sol3.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Node_QNAME = new QName("http://www.example.org/NfvDeployer", "node");
    private final static QName _Virtualizednode_QNAME = new QName("http://www.example.org/NfvDeployer", "virtualizednode");
    private final static QName _Plink_QNAME = new QName("http://www.example.org/NfvDeployer", "plink");
    private final static QName _Links_QNAME = new QName("http://www.example.org/NfvDeployer", "links");
    private final static QName _Host_QNAME = new QName("http://www.example.org/NfvDeployer", "host");
    private final static QName _Virtualizednodes_QNAME = new QName("http://www.example.org/NfvDeployer", "virtualizednodes");
    private final static QName _NfvSystem_QNAME = new QName("http://www.example.org/NfvDeployer", "NfvSystem");
    private final static QName _Connections_QNAME = new QName("http://www.example.org/NfvDeployer", "connections");
    private final static QName _Hosts_QNAME = new QName("http://www.example.org/NfvDeployer", "hosts");
    private final static QName _Nffgs_QNAME = new QName("http://www.example.org/NfvDeployer", "nffgs");
    private final static QName _Reachablehosts_QNAME = new QName("http://www.example.org/NfvDeployer", "reachablehosts");
    private final static QName _Nffg_QNAME = new QName("http://www.example.org/NfvDeployer", "nffg");
    private final static QName _Link_QNAME = new QName("http://www.example.org/NfvDeployer", "link");
    private final static QName _Vnf_QNAME = new QName("http://www.example.org/NfvDeployer", "vnf");
    private final static QName _Catalogue_QNAME = new QName("http://www.example.org/NfvDeployer", "catalogue");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.dp2.NFV.sol3.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NFFGsType }
     * 
     */
    public NFFGsType createNFFGsType() {
        return new NFFGsType();
    }

    /**
     * Create an instance of {@link HostsType }
     * 
     */
    public HostsType createHostsType() {
        return new HostsType();
    }

    /**
     * Create an instance of {@link LinkType }
     * 
     */
    public LinkType createLinkType() {
        return new LinkType();
    }

    /**
     * Create an instance of {@link NFFGtype }
     * 
     */
    public NFFGtype createNFFGtype() {
        return new NFFGtype();
    }

    /**
     * Create an instance of {@link ReachableHostsType }
     * 
     */
    public ReachableHostsType createReachableHostsType() {
        return new ReachableHostsType();
    }

    /**
     * Create an instance of {@link CatalogueType }
     * 
     */
    public CatalogueType createCatalogueType() {
        return new CatalogueType();
    }

    /**
     * Create an instance of {@link VNFtype }
     * 
     */
    public VNFtype createVNFtype() {
        return new VNFtype();
    }

    /**
     * Create an instance of {@link NodeType }
     * 
     */
    public NodeType createNodeType() {
        return new NodeType();
    }

    /**
     * Create an instance of {@link VirtualNodeType }
     * 
     */
    public VirtualNodeType createVirtualNodeType() {
        return new VirtualNodeType();
    }

    /**
     * Create an instance of {@link PLPtype }
     * 
     */
    public PLPtype createPLPtype() {
        return new PLPtype();
    }

    /**
     * Create an instance of {@link HostType }
     * 
     */
    public HostType createHostType() {
        return new HostType();
    }

    /**
     * Create an instance of {@link VirtualizedNodesType }
     * 
     */
    public VirtualizedNodesType createVirtualizedNodesType() {
        return new VirtualizedNodesType();
    }

    /**
     * Create an instance of {@link LinksType }
     * 
     */
    public LinksType createLinksType() {
        return new LinksType();
    }

    /**
     * Create an instance of {@link ConnectionsType }
     * 
     */
    public ConnectionsType createConnectionsType() {
        return new ConnectionsType();
    }

    /**
     * Create an instance of {@link NfvSystemType }
     * 
     */
    public NfvSystemType createNfvSystemType() {
        return new NfvSystemType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "node")
    public JAXBElement<NodeType> createNode(NodeType value) {
        return new JAXBElement<NodeType>(_Node_QNAME, NodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VirtualNodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "virtualizednode")
    public JAXBElement<VirtualNodeType> createVirtualizednode(VirtualNodeType value) {
        return new JAXBElement<VirtualNodeType>(_Virtualizednode_QNAME, VirtualNodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PLPtype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "plink")
    public JAXBElement<PLPtype> createPlink(PLPtype value) {
        return new JAXBElement<PLPtype>(_Plink_QNAME, PLPtype.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinksType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "links")
    public JAXBElement<LinksType> createLinks(LinksType value) {
        return new JAXBElement<LinksType>(_Links_QNAME, LinksType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "host")
    public JAXBElement<HostType> createHost(HostType value) {
        return new JAXBElement<HostType>(_Host_QNAME, HostType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VirtualizedNodesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "virtualizednodes")
    public JAXBElement<VirtualizedNodesType> createVirtualizednodes(VirtualizedNodesType value) {
        return new JAXBElement<VirtualizedNodesType>(_Virtualizednodes_QNAME, VirtualizedNodesType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NfvSystemType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "NfvSystem")
    public JAXBElement<NfvSystemType> createNfvSystem(NfvSystemType value) {
        return new JAXBElement<NfvSystemType>(_NfvSystem_QNAME, NfvSystemType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "connections")
    public JAXBElement<ConnectionsType> createConnections(ConnectionsType value) {
        return new JAXBElement<ConnectionsType>(_Connections_QNAME, ConnectionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HostsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "hosts")
    public JAXBElement<HostsType> createHosts(HostsType value) {
        return new JAXBElement<HostsType>(_Hosts_QNAME, HostsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NFFGsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "nffgs")
    public JAXBElement<NFFGsType> createNffgs(NFFGsType value) {
        return new JAXBElement<NFFGsType>(_Nffgs_QNAME, NFFGsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReachableHostsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "reachablehosts")
    public JAXBElement<ReachableHostsType> createReachablehosts(ReachableHostsType value) {
        return new JAXBElement<ReachableHostsType>(_Reachablehosts_QNAME, ReachableHostsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NFFGtype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "nffg")
    public JAXBElement<NFFGtype> createNffg(NFFGtype value) {
        return new JAXBElement<NFFGtype>(_Nffg_QNAME, NFFGtype.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LinkType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "link")
    public JAXBElement<LinkType> createLink(LinkType value) {
        return new JAXBElement<LinkType>(_Link_QNAME, LinkType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VNFtype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "vnf")
    public JAXBElement<VNFtype> createVnf(VNFtype value) {
        return new JAXBElement<VNFtype>(_Vnf_QNAME, VNFtype.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CatalogueType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/NfvDeployer", name = "catalogue")
    public JAXBElement<CatalogueType> createCatalogue(CatalogueType value) {
        return new JAXBElement<CatalogueType>(_Catalogue_QNAME, CatalogueType.class, null, value);
    }

}
