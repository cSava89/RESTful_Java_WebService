package it.polito.dp2.NFV.sol3.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.PLPtype;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;
import it.polito.dp2.NFV.sol3.jaxb.VirtualNodeType;

public class BackendDB {
	
	private static long nextNffg = 0;
	private static Map<String,VNFtype> vnfMAP=new ConcurrentHashMap<String,VNFtype>(); 
	private static Map<String,HostType> hostMAP=new ConcurrentHashMap<String,HostType>(); //MAP OF HOSTS WITHOUT VIRTUALIZED NODES
	private static Map<String,PLPtype> connectionsMAP=new ConcurrentHashMap<String,PLPtype>();
	private static Map<String,List<VirtualNodeType>> virtualizedNodesOnHostMAP=new ConcurrentHashMap<String,List<VirtualNodeType>>(); //Virtual node has just name and belonging to nffg
	private static Map<String,HostStatus> hostStatusMAP=new ConcurrentHashMap<String,HostStatus>();
	private static Map<String,String> node_id=new ConcurrentHashMap<String,String>();
	private static Map<String,String> relationships_id=new ConcurrentHashMap<String,String>();
	private static Map<String,Boolean> loadedNffgs=new ConcurrentHashMap<String,Boolean>();
	private static Map<String,NFFGtype> nffgsMap= new ConcurrentHashMap<String,NFFGtype>(); //NFFG IS COMPLETE, EACH NODE HAS ITS OWN LINKS
	private static Map<String,List<NodeType>> nodesInNffg=new ConcurrentHashMap<String,List<NodeType>>(); //Nodes have void set of links and reachable nodes
	private static Map<String,List<LinkType>> nodeLinks = new ConcurrentHashMap<String,List<LinkType>>();
	
	public static synchronized long getNextNffg()
	{
		return nextNffg++;
	}
	public static Map<String,VNFtype> getVnfMAP() {
		return vnfMAP;
	}

	public static Map<String,HostType> getHostMAP() {
		return hostMAP;
	}

	public static Map<String,PLPtype> getConnectionsMAP() {
		return connectionsMAP;
	}

	public static Map<String,List<VirtualNodeType>> getVirtualizedNodesOnHostMAP() {
		return virtualizedNodesOnHostMAP;
	}

	public static Map<String,HostStatus> getHostStatusMAP() {
		return hostStatusMAP;
	}

	public static Map<String,String> getNode_id() {
		return node_id;
	}


	public static Map<String,String> getRelationships_id() {
		return relationships_id;
	}

	public static Map<String,Boolean> getLoadedNffgs() {
		return loadedNffgs;
	}

	public static Map<String,NFFGtype> getNffgsMap() {
		return nffgsMap;
	}

	public static Map<String,List<NodeType>> getNodesInNffg() {
		return nodesInNffg;
	}

	public static Map<String,List<LinkType>> getNodeLinks() {
		return nodeLinks;
	}

	

	

	

	

	

	

	

	

	

	


	

	

	
	
}
