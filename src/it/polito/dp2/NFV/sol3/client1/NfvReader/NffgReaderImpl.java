package it.polito.dp2.NFV.sol3.client1.NfvReader;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.NffgReader;

import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;


public class NffgReaderImpl extends NamedEntityImpl implements NffgReader{

	
	private Set<NodeReader> nodes;
	private Calendar deployTime;
	
	
	public NffgReaderImpl(NFFGtype nffg) {
		super(nffg.getName());//to set the name
		
		XMLGregorianCalendar gc=nffg.getDeployTime();
		Date date=gc.toGregorianCalendar().getTime();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		
		this.deployTime=c;
		
		
		this.nodes=new HashSet<>();
		
		for(NodeType node:nffg.getNode()) {
			NodeReader nd=new NodeReaderImpl(node);
			this.nodes.add(nd);
		}
		
	}

	@Override
	public Calendar getDeployTime() {
		
			return this.deployTime;
		
		
	}

	@Override
	public NodeReader getNode(String node) {
		
		for(NodeReader n:this.nodes) {
			if(node.equals(n.getName())) {
				return n;
			}
		}
		return null;
	}

	@Override
	public Set<NodeReader> getNodes() {
		
			return nodes;
		
	}
	
	

	

}
