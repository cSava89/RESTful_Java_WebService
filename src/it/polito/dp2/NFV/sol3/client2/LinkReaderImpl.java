package it.polito.dp2.NFV.sol3.client2;

import it.polito.dp2.NFV.LinkReader;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;


public class LinkReaderImpl extends NamedEntityImpl implements LinkReader {

	private NodeType sourceNode;
	private NodeType destinationNode;
	private int latency;
	private float throughput;
	
	public LinkReaderImpl(LinkType link,NodeType source)  {
		
		super(link.getName());
		
		this.sourceNode=source;
		
		this.destinationNode=getDestinationNodeType(link, source);
		
		this.latency=link.getLatency().intValue();
		
		this.throughput=link.getThroughput();
	}
	
	private static NodeType getDestinationNodeType(LinkType l,NodeType source) {
		for(NodeType n:NfvReaderImpl.getNffgByName(source.getNFFG()).getNode()) {
			if(l.getDestinationNode().equals(n.getName()))
				return n;
			
		}
		return null;
	}

	@Override
	public NodeReader getDestinationNode() {
		if(this.destinationNode!=null)
			
				return new NodeReaderImpl(this.destinationNode);
			
		return null;
	}

	@Override
	public int getLatency() {
		
			return this.latency;
		
	}

	@Override
	public NodeReader getSourceNode() {
		if(this.sourceNode!=null)
			
				return new NodeReaderImpl(this.sourceNode);
			
		return null;
	}

	@Override
	public float getThroughput() {
		
			return this.throughput;
		
	}

}
