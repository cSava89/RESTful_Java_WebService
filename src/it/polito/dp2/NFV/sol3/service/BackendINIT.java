package it.polito.dp2.NFV.sol3.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;


import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.jaxb.EnumeratedFunctionalTypes;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.LinksType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.PLPtype;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;
import it.polito.dp2.NFV.sol3.jaxb.VirtualNodeType;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.InternalServerErrorException;


public class BackendINIT {
	
	private Logger logger=Logger.getLogger(BackendINIT.class.getName());
	private NfvReader monitor;
	private ObjectFactory of;
	
	private Map<String,VNFtype> vnfMAP;
	private Map<String,HostType> hostMAP;
	private Map<String,PLPtype> connectionsMAP;
	private Map<String,List<VirtualNodeType>> virtualizedNodesOnHostMAP;
	private Map<String,HostStatus> hostStatusMAP;
	
	protected BackendINIT() {
		
		vnfMAP=BackendDB.getVnfMAP();
		hostMAP=BackendDB.getHostMAP();
		connectionsMAP=BackendDB.getConnectionsMAP();
		virtualizedNodesOnHostMAP=BackendDB.getVirtualizedNodesOnHostMAP();
		hostStatusMAP=BackendDB.getHostStatusMAP();
		of=new ObjectFactory();
		
		NfvReaderFactory factory=null;
		
		try {
			factory=NfvReaderFactory.newInstance();
			monitor=factory.newNfvReader();
		}catch(FactoryConfigurationError| NfvReaderException e) {
			
			logger.log(Level.SEVERE,"Error during init phase: factory or monitor initialization failed");
			throw new InternalServerErrorException();
			
		}
		
		
		
	}
	
	protected NFFGtype init() {
		
		setCatalogue();
		setHosts();
		setConnections();
		NFFGtype nffg0=null;
		
		nffg0=getNFFG0();
		
		if(nffg0==null) {
			logger.log(Level.SEVERE,"Error during init phase:  Nffg0 built error!");
			throw new InternalServerErrorException();
		}
		
		
		
		return nffg0;
	}

	private NFFGtype getNFFG0() {
		 
				NffgReader nffg0 = monitor.getNffg("Nffg0");
				NFFGtype nffg0_t = null;
				if(nffg0!=null) {
					try {
						nffg0_t=of.createNFFGtype();
						nffg0_t.setName(nffg0.getName());
						
						Set<NodeReader> nodeSet = nffg0.getNodes();
						
						for (NodeReader nr: nodeSet) {
							
							NodeType node=of.createNodeType();
							
							node.setName(nr.getName());
							node.setNFFG(nffg0.getName());
							node.setHost(nr.getHost().getName());
							node.setFunctionalType(nr.getFuncType().getName());
							
							
							Set<LinkReader> linkSet = nr.getLinks();
							
							LinksType links=of.createLinksType();
							
							for (LinkReader lr: linkSet) {
								
								LinkType link=of.createLinkType();
								
								link.setName(lr.getSourceNode().getName()+"_"+lr.getDestinationNode().getName());
								//link.setName(lr.getName());
								link.setSourceNode(lr.getSourceNode().getName());
								link.setDestinationNode(lr.getDestinationNode().getName());
								link.setThroughput(lr.getThroughput());
								link.setLatency(new BigInteger(String.valueOf(lr.getLatency())));
								
								links.getLink().add(link);
								
							}
							
							node.setLinks(links);
								
						nffg0_t.getNode().add(node);
						}
					}catch(NullPointerException n) {
						logger.log(Level.SEVERE,"Error during init phase: some of the requested Nffg0 item is null!");
						throw new InternalServerErrorException();
						
						
					}
				}else {
					logger.log(Level.SEVERE,"Error during init phase: input Nffg0 is empty!");
					throw new InternalServerErrorException();
				}
				return nffg0_t;
				
				//NODE LINKS E NODE IN NFFG FULFILLED BY LOADGRAPH (Node_Host_Allocated_On and Forwardint_to_loading)
			
	}

	private void setConnections() {
		
		Set<HostReader> hosts = monitor.getHosts();
		
		if(!hosts.isEmpty()) {
				
			try {
				for (HostReader sri: hosts) {
					
					for (HostReader srj: hosts) {
						PLPtype connectionPerformance=of.createPLPtype();
						
						connectionPerformance.setHost1(sri.getName());
						connectionPerformance.setHost2(srj.getName());
						
						ConnectionPerformanceReader cpr = monitor.getConnectionPerformance(sri, srj);
						connectionPerformance.setThroughput(cpr.getThroughput());
						connectionPerformance.setLatency(new BigInteger(String.valueOf(cpr.getLatency())));
						
						String connectionKey=new String(sri.getName()+"_"+srj.getName());
						connectionsMAP.put(connectionKey, connectionPerformance);
					}
					
				}
			}catch(NullPointerException n) {
				logger.log(Level.SEVERE,"Error during init phase: null pointer setting connection performances");
				throw new InternalServerErrorException();
				
			}
			logger.log(Level.INFO,"init phase: set connection complete!");
		}
		else {
			logger.log(Level.SEVERE,"Error during init phase: input hosts are empty!");
			throw new InternalServerErrorException();
		}
		
		
		
	}

	private void setHosts() {
		Set<HostReader> hosts=monitor.getHosts();
		
		if(!hosts.isEmpty()) {
			
			for (HostReader host_r: hosts) {
				
				HostType host=of.createHostType();
				host.setName(host_r.getName());
				try {
				host.setNumberOfVNFs(new BigInteger(String.valueOf(host_r.getMaxVNFs())));
				host.setAmountOfMemory(new BigInteger(String.valueOf(host_r.getAvailableMemory())));
				host.setDiskStorage(new BigInteger(String.valueOf(host_r.getAvailableStorage())));
				}catch(NumberFormatException n) {
					logger.log(Level.SEVERE,"Error during init phase:  hosts' parameters are not valid!");
					throw new InternalServerErrorException();
					
				}
				hostMAP.put(host_r.getName(), host);
				
				List<VirtualNodeType> nodesOnHost=new CopyOnWriteArrayList<VirtualNodeType>();
				virtualizedNodesOnHostMAP.put(host_r.getName(), nodesOnHost);
				
				HostStatus status=new HostStatus();
				status.setHostName(host_r.getName());
				status.setRemainingVNF(host_r.getMaxVNFs());
				status.setRemainingMemory(host_r.getAvailableMemory());
				status.setRemainingStorage(host_r.getAvailableStorage());
				
				hostStatusMAP.put(host_r.getName(), status);
				
				
			}
			
			
		}else {
			logger.log(Level.SEVERE,"Error during init phase: input hosts are empty!");
			throw new InternalServerErrorException();
		}
		
	}

	private void setCatalogue() {
		
		Set<VNFTypeReader> catalogue=monitor.getVNFCatalog();
		
		if(!catalogue.isEmpty()) {
			for(VNFTypeReader v:catalogue) {
				
				VNFtype vnf=of.createVNFtype();
				vnf.setName(v.getName());
				vnf.setType(EnumeratedFunctionalTypes.fromValue(v.getFunctionalType().value()));
				vnf.setAmountOfMemory(new BigInteger(String.valueOf(v.getRequiredMemory())));
				vnf.setDiskStorage(new BigInteger(String.valueOf(v.getRequiredStorage())));
				
				vnfMAP.put(v.getName(), vnf);
			}
		}
		else {
			logger.log(Level.SEVERE,"Error during init phase: input catalogue is empty!");
			throw new InternalServerErrorException();
		}
		
	}

}
