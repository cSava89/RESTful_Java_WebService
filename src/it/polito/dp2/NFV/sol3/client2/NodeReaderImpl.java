package it.polito.dp2.NFV.sol3.client2;

import java.util.HashSet;
import java.util.Set;



import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;


public class NodeReaderImpl extends NamedEntityImpl implements NodeReader {
	
	private Set<LinkReader> links;
	
	private NFFGtype nffg;
	   
	private HostType host;
	
	private VNFtype functionalType;

	public NodeReaderImpl(NodeType node) {
		super(node.getName());
		
		this.links=new HashSet<>();
		
		Set<LinkType> nodeLink=new HashSet<>(node.getLinks().getLink());
		
		if(!nodeLink.isEmpty()) { //possible null
		
			for(LinkType l:nodeLink) {
				LinkReader linkReader=new LinkReaderImpl(l,node);
				this.links.add(linkReader);
			
			}
		}
		
	    this.nffg=NfvReaderImpl.getNffgByName(node.getNFFG());
	    if(node.getHost()!=null)
	    	this.host=NfvReaderImpl.getHostByName(node.getHost());
	    else
	    	this.host=null;
	    this.functionalType=NfvReaderImpl.getVNFbyName(node.getFunctionalType());
	}

	@Override
	public VNFTypeReader getFuncType() {
		
		if(this.functionalType!=null)
			return new VNFTypeReaderImpl(this.functionalType);
			
		return null;
		
		
	}


	@Override
	public HostReader getHost() {
		if(this.host!=null)
			return new HostReaderImpl(this.host);
			
		return null;
	}

	@Override
	public Set<LinkReader> getLinks() {
		if(this.links!=null)
			return this.links;
		return null;
	}

	@Override
	public NffgReader getNffg() {
		
		if(this.nffg!=null)
			return new NffgReaderImpl(this.nffg);
			
		return null;
	}
	
	
}
