package it.polito.dp2.NFV.sol3.client2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.VirtualNodeType;


public class HostReaderImpl extends NamedEntityImpl implements HostReader {

	
	private Set<NodeReader> nodes;
	private int availableMemory;
	private int availableStorage;
	private int maxVNFs;
	
	public HostReaderImpl(HostType h) {
		super(h.getName());
		nodes=new HashSet<>();
		
		Set<VirtualNodeType> allocatedNodes=new HashSet<>(h.getVirtualizednodes().getVirtualizednode());
		
		if(!allocatedNodes.isEmpty()) {	//possible null
			for(VirtualNodeType n:allocatedNodes) {
				NodeReader hNode=new NodeReaderImpl(HostReaderImpl.getNodeTypeByHostTypeNode(n));
			
				nodes.add(hNode);
				}
		}
		this.availableMemory=h.getAmountOfMemory().intValue();
		this.availableStorage=h.getDiskStorage().intValue();
		this.maxVNFs=h.getNumberOfVNFs().intValue();
	}

	private static NodeType getNodeTypeByHostTypeNode(VirtualNodeType node) {
		
		
		for(NodeType n:NfvReaderImpl.getNffgByName(node.getNodeNFFG()).getNode()) {
			if(n.getName().equals(node.getNodeName())) {
				NodeType trovato=(new ObjectFactory()).createNodeType();
				trovato=n;
				return trovato;
			}
			
		}
		return null;
	}
	
	
	@Override
	public int getAvailableMemory() {
		
			return this.availableMemory;
		
	}

	@Override
	public int getAvailableStorage() {
		
			return this.availableStorage;
		
	}

	@Override
	public int getMaxVNFs() {
		
			return this.maxVNFs;
		
	}

	@Override
	public Set<NodeReader> getNodes() {
		if(this.nodes!=null)
			return this.nodes;
		return null;
	}

}
