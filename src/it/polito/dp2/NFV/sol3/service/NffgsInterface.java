package it.polito.dp2.NFV.sol3.service;



import javax.xml.bind.JAXBElement;


import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGsType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ReachableHostsType;


public interface NffgsInterface {
	
	public JAXBElement<NFFGsType> getNffgs(String from);
	
	
	public JAXBElement<NFFGtype> postNffg(JAXBElement<NFFGtype> nffg);
	
		
		public JAXBElement<NFFGtype> getNffg(String nffg);
		
		
		public void deleteNffg(String nffg);
		
		
		public JAXBElement<NodeType> postNode(String nffg, JAXBElement<NodeType> node);
		
		
			
			public JAXBElement<NodeType> getNode(String nffg, String node);
			
			
			public void deleteNode( String nffg,  String node);
			
				
				public JAXBElement<ReachableHostsType> getReachableHosts( String nffg, String sourceNode);
				
				
				public JAXBElement<LinkType> postLink( String nffg, String sourceNode, JAXBElement<LinkType> link);
				
				
				public void deleteLink( String nffg, String sourceNode, String link);
				
				
				
		
			

}
