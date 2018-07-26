package it.polito.dp2.NFV.sol3.service;




import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import it.polito.dp2.NFV.sol3.service.ServiceException;
import it.polito.dp2.NFV.sol3.neo4j.Nodes;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.LinksType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;
import it.polito.dp2.NFV.sol3.jaxb.VirtualNodeType;
import it.polito.dp2.NFV.sol3.neo4j.Labels;
import it.polito.dp2.NFV.sol3.neo4j.Node;
import it.polito.dp2.NFV.sol3.neo4j.Properties;
import it.polito.dp2.NFV.sol3.neo4j.Property;
import it.polito.dp2.NFV.sol3.neo4j.Relationship;

public class BackendSERVICE {
	
	private static BackendSERVICE bService;
	
	private Logger logger=Logger.getLogger(BackendSERVICE.class.getName());
	private ObjectFactory serviceOf;
	private it.polito.dp2.NFV.sol3.neo4j.ObjectFactory neo4jOf;
	private String targetURL;
	private WebTarget webTarget;
	private Map<String,String> node_id;
	private Map<String,String> relationship_id;
	private Map<String,Boolean> loadedNffgs; 
	private Map<String,HostStatus> hostmapStatus;
	private Map<String,List<VirtualNodeType>> nodesOnHost;
	private Map<String,VNFtype> vnfMap;
	private Map<String, NFFGtype> nffgsMap;
	private Map<String,List<NodeType>> nodesInNffg;
	private Map<String,List<LinkType>> nodeLinks;
	
	private BackendSERVICE() throws BadRequestException,ClientErrorException,ServerErrorException  {
		
		serviceOf=new ObjectFactory();
		neo4jOf= new it.polito.dp2.NFV.sol3.neo4j.ObjectFactory();
		node_id=BackendDB.getNode_id();
		relationship_id=BackendDB.getRelationships_id();
		loadedNffgs=BackendDB.getLoadedNffgs();
		hostmapStatus=BackendDB.getHostStatusMAP();
		nodesOnHost=BackendDB.getVirtualizedNodesOnHostMAP();
		vnfMap=BackendDB.getVnfMAP();
		nffgsMap=BackendDB.getNffgsMap();
		nodesInNffg = BackendDB.getNodesInNffg();
		nodeLinks = BackendDB.getNodeLinks();
		
		targetURL=System.getProperty("it.polito.dp2.NFV.lab3.Neo4JSimpleXMLURL");
		
		if(targetURL==null)
			targetURL="http://localhost:8080/Neo4JSimpleXML/rest";
		
		Client client = ClientBuilder.newClient();
				try {
					webTarget = client.target(targetURL);
				}
				catch (IllegalArgumentException iae) {
					logger.log(Level.SEVERE,"Backend Service: illegal argument exception during web target creation\n");
					throw new InternalServerErrorException();
				}
				
		BackendINIT bInit=new BackendINIT();
		NFFGtype nffg0=bInit.init(); /*THIS FUNCTION INIT THE SYSTEM AND 
		
									RETURN COMPLETE NFFGTYPE SO IT CONTAINS ALSO NODES AND LINKS*/
		
		if(nffg0 != null) {
		CheckAllocationAndLoadGraph(nffg0);
		
		loadedNffgs.put("Nffg0", true); 
		nffgsMap.put("Nffg0", nffg0); /*BY THE PREVIOUS INIT FUNCTION NFFG0 IS COMPLETE*/
		}
		
		
		
	}
	
	public static synchronized BackendSERVICE getInstance() throws BadRequestException,ClientErrorException,ServerErrorException
	{
		if (bService == null)
			bService = new BackendSERVICE();
		return bService;
	}
	
	protected void CheckAllocationAndLoadGraph(NFFGtype nffg)throws BadRequestException,ClientErrorException,ServerErrorException,InternalServerErrorException{
		try { 
			boolean found=false;
			
			synchronized(hostmapStatus) {
				
				Map<String,HostStatus> hostStatusCopy=new ConcurrentHashMap<String,HostStatus>(hostmapStatus); //make a copy for rollback
				
				for(NodeType node:nffg.getNode()) { 
					
					String host=node.getHost(); //for each node check the host
					
					VNFtype vnfUnderCheck=vnfMap.get(node.getFunctionalType()); //take the vnf virtualized by the node
					if(vnfUnderCheck==null) {
						logger.log(Level.SEVERE,"Backend Service, CheckAllocationAndLoadGraph: vnf null in check allocation process");
						throw new BadRequestException();
					}
					
					HostStatus hostUnderCheck=null;
					
					if(host!=null) {	//the client has suggested a host/////////////////////////////////////////////////////////////////////////
						
							hostUnderCheck= hostStatusCopy.get(host);
							
							if(hostUnderCheck==null) {
								logger.log(Level.SEVERE,"Backend Service, CheckAllocationAndLoadGraph: suggested host is unknown in check allocation process");
								throw new BadRequestException();
							}
						
							if(hostUnderCheck.getRemainingVNF()!=0 && 
								(hostUnderCheck.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
								&&(hostUnderCheck.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
							
									hostUnderCheck.setRemainingVNF(hostUnderCheck.getRemainingVNF()-1);
									hostUnderCheck.setRemainingMemory(hostUnderCheck.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
									hostUnderCheck.setRemainingStorage(hostUnderCheck.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
									
									found=true;
							
								}else {
										for(HostStatus h:hostStatusCopy.values()) {
											
													if(h.getRemainingVNF()!=0 && 
														(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
														&&(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
													
													h.setRemainingVNF(h.getRemainingVNF()-1);
													h.setRemainingMemory(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
													h.setRemainingStorage(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
													
													found=true;
													node.setHost(h.getHostName());
													break;
											
												}
									
											}
										
										
								
								}
								if(!found) {
									logger.log(Level.SEVERE,"Backend Service, CheckAllocationAndLoadGraph: suggested host cannot allocate new virtual nodes");
									throw new ClientErrorException(Response.Status.CONFLICT); //throw a conflict exception if allocation is not possible
								}
								found=false;
					
						}/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
					else { //if the host is not suggested ///////////////////////////////////////////////////////////////////////////////////
									for(HostStatus h:hostStatusCopy.values()) {
											
											if(h.getRemainingVNF()!=0 && 
												(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
												&&(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
											
											h.setRemainingVNF(h.getRemainingVNF()-1);
											h.setRemainingMemory(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
											h.setRemainingStorage(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
											
											found=true;
											node.setHost(h.getHostName());
											break;
									
										}
							
									}
									if(!found) {
										logger.log(Level.SEVERE,"Backend Service, CheckAllocationAndLoadGraph: no host can allocate new virtual nodes");
										throw new ClientErrorException(Response.Status.CONFLICT); //throw a conflict exception if allocation is not possible
									}
									found=false;
									
						}/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				}
				
				
				loadGraph(nffg);
					
				hostmapStatus.putAll(hostStatusCopy); //commit the changes
				
			
			}
		}catch (BadRequestException bdr) {
			throw bdr; //re-throw it
		} 
		catch (ClientErrorException cee) {
			throw cee; //re-throw it
		}
		
		catch (ServiceException se) {
			logger.log(Level.SEVERE,"Backend Service, Check...loadGraph: something wrong trying to load nffg \n");
			throw new ServerErrorException(Response.Status.BAD_GATEWAY); //502
			
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Backend Service, Check...loadGraph: unespected exception \n");
			throw new InternalServerErrorException(e.getMessage()); 
			
		}
		
		
	}
	
	protected void checkAllocationAndLoadNode(NodeType node)throws BadRequestException,ClientErrorException,ServerErrorException,InternalServerErrorException {
		try {
			
			boolean found=false;
				
				synchronized(hostmapStatus) {
					
					Map<String,HostStatus> hostStatusCopy=new ConcurrentHashMap<String,HostStatus>(hostmapStatus); //make a copy for rollback
					
						String host=node.getHost(); //for each node check the host
						
						VNFtype vnfUnderCheck=vnfMap.get(node.getFunctionalType());
						if(vnfUnderCheck==null) {
							logger.log(Level.SEVERE,"Backend Service, Check...loadNode: vnf null in node check allocation process");
							throw new BadRequestException();
						}
						
						HostStatus hostUnderCheck=null;
						
						if(host!=null) {	//the client has suggested a host/////////////////////////////////////////////////////////////////////////
							
								hostUnderCheck= hostStatusCopy.get(host);
								
								if(hostUnderCheck==null) {
									logger.log(Level.SEVERE,"Backend Service, Check...loadNode: suggested host is unknown in check allocation process");
									throw new BadRequestException();
								}
							
								if(hostUnderCheck.getRemainingVNF()!=0 && 
									(hostUnderCheck.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
									&&(hostUnderCheck.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
								
									    hostUnderCheck.setRemainingVNF(hostUnderCheck.getRemainingVNF()-1);
										hostUnderCheck.setRemainingMemory(hostUnderCheck.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
										hostUnderCheck.setRemainingStorage(hostUnderCheck.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
										
										found=true;
								
									}else {
											for(HostStatus h:hostStatusCopy.values()) {
												
														if(h.getRemainingVNF()!=0 && 
															(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
															&&(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
														
														h.setRemainingVNF(h.getRemainingVNF()-1);
														h.setRemainingMemory(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
														h.setRemainingStorage(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
														
														found=true;
														node.setHost(h.getHostName());
														break;
												
													}
										
												}
											
											
									
									}
									if(!found) {
										logger.log(Level.SEVERE,"Backend Service, Check...loadNode: node allocation not possible on suggested host");
										throw new ClientErrorException(Response.Status.CONFLICT); //throw a conflict exception if allocation is not possible
									
									}
									found=false;
						
							}/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						
						else { //if the host is not suggested ///////////////////////////////////////////////////////////////////////////////////
										for(HostStatus h:hostStatusCopy.values()) {
												
												if(h.getRemainingVNF()!=0 && 
													(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue())>=0 
													&&(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue())>=0) {
												
												h.setRemainingVNF(h.getRemainingVNF()-1);
												h.setRemainingMemory(h.getRemainingMemory()-vnfUnderCheck.getAmountOfMemory().intValue());
												h.setRemainingStorage(h.getRemainingStorage()-vnfUnderCheck.getDiskStorage().intValue());
												
												found=true;
												node.setHost(h.getHostName());
												break;
										
											}
								
										}
										if(!found) {
											logger.log(Level.SEVERE,"Backend Service, Check...loadNode: node allocation not possible because no host is available");
											throw new ClientErrorException(Response.Status.CONFLICT); //throw a conflict exception if allocation is not possible
										}
										found=false;
										
							}/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
						Map<String,String> node_id_copy;
						Map<String,String> relationship_id_copy;
						Map<String,List<VirtualNodeType>> nodesOnHost_copy;
						Map<String,List<NodeType>> nodesInNffg_copy;				
						
							 node_id_copy=new ConcurrentHashMap<String,String>();
							 relationship_id_copy=new ConcurrentHashMap<String,String>();
							nodesOnHost_copy=new ConcurrentHashMap<String,List<VirtualNodeType>>(nodesOnHost);
							nodesInNffg_copy=new ConcurrentHashMap<String,List<NodeType>>(nodesInNffg);
							
							Node_Host_AllocatedOn(node,node_id_copy,relationship_id_copy,nodesOnHost_copy,nodesInNffg_copy);
						
						
					nodesOnHost.putAll(nodesOnHost_copy);
					node_id.putAll(node_id_copy); //if I am here i have to commit the changes
					relationship_id.putAll(relationship_id_copy);
					hostmapStatus.putAll(hostStatusCopy); //commit the changes
					nodesInNffg.putAll(nodesInNffg_copy); 
					// I have to update the nffgType
					NFFGtype belongingNffg = nffgsMap.get(node.getNFFG());
					belongingNffg.getNode().add(node);
				
				}
		}catch (BadRequestException bre) {
			
			throw bre;
			
		}catch (ClientErrorException cee) {
			
			throw cee;
			
		} catch (ProcessingException|WebApplicationException e) {
			
			logger.log(Level.SEVERE,"Backend Service, Check...loadNode: something wrong trying to load the new added node \n");
			throw new ServerErrorException(Response.Status.BAD_GATEWAY); //502
			
		}catch (Exception e) {
			
			logger.log(Level.SEVERE,"Backend Service, Check...loadNode: unexpected exception \n");
			throw new InternalServerErrorException();
			
		}
		
	}
	
	
	
	protected void loadGraph(NFFGtype nffg) throws ServiceException {
		
		
		try {
				Node_Host_AllocatedOn_loading_fromNffg(nffg);		
				ForwardTo_loading(nffg);	
				
			} catch(ResponseProcessingException rpe ){
				
				logger.log(Level.SEVERE,"Backed Service, loadGraph: error during JAX-RS response processing, more details following... \n"+rpe.getMessage());
				throw new ServiceException("Backed Service, loadGraph: error during JAX-RS response processing, more details following... \n"+rpe.getMessage());
			
			} catch(ProcessingException pe ){
				
				logger.log(Level.SEVERE,"Backed Service, loadGraph: error during JAX-RS request processing, more details following... \n"+pe.getMessage());
				throw new ServiceException("Backed Service, loadGraph: error during JAX-RS request processing, more details following... \n"+pe.getMessage());
				
			} catch(WebApplicationException wa) {
				logger.log(Level.SEVERE,"Backed Service, loadGraph: bad request or bad server response, more details following... \n"+wa.getMessage());
				throw new ServiceException("Backed Service, loadGraph: bad request or bad server response, more details following... \n"+wa.getMessage());
			
			}catch(Exception e) {
				logger.log(Level.SEVERE,"Backed Service, loadGraph: unexpected exception, more details following... \n"+e.getMessage() );
				throw new ServiceException("Backed Service, loadGraph: unexpected exception, more details following... \n"+e.getMessage());
				
			}
			
		try {
			nffg.setDeployTime( DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()) );
		} catch (DatatypeConfigurationException e) {
			logger.log(Level.SEVERE,"Backed Service, loadGraph: DatatypeConfigurationException setting nffg deploy time \n"+e.getMessage() );
			throw new ServiceException("Backed Service, loadGraph: DatatypeConfigurationException setting nffg deploy time \n"+e.getMessage());
		}
	
		
	}

protected void Node_Host_AllocatedOn_loading_fromNffg(NFFGtype nffg)throws ResponseProcessingException,ProcessingException,WebApplicationException {

	
	Map<String,String> node_id_copy=new ConcurrentHashMap<String,String>(); //for possible rollback
	Map<String,String> relationship_id_copy=new ConcurrentHashMap<String,String>();
	Map<String,List<VirtualNodeType>> nodesOnHost_copy=new ConcurrentHashMap<String,List<VirtualNodeType>>(nodesOnHost);
	Map<String,List<NodeType>> nodesInNffg_copy = new ConcurrentHashMap<String,List<NodeType>>(nodesInNffg);
	
	for(NodeType src:nffg.getNode()) {
		
		Node_Host_AllocatedOn(src,node_id_copy,relationship_id_copy,nodesOnHost_copy,nodesInNffg_copy);
		
		}
	nodesOnHost.putAll(nodesOnHost_copy);//if I am here i have to commit the changes
	node_id.putAll(node_id_copy); 
	relationship_id.putAll(relationship_id_copy);
	nodesInNffg.putAll(nodesInNffg_copy);
	
	
			
}

protected void Node_Host_AllocatedOn(NodeType src, Map<String,String> node_id_copy,Map<String,String> relationship_id_copy,Map<String,List<VirtualNodeType>> nodesOnHost_copy,Map<String,List<NodeType>> nodesInNffg_copy)throws ResponseProcessingException,ProcessingException,WebApplicationException {
	

	
	Node sourceNode = null;
	Node srcNodeResponse = null;
	Node hostNode = null;
	Node hostResponse = null;
	Relationship srcNodeOnHostRel = null;
	Relationship respNodeOnHostRel = null;
	
			//for each node create a graph node
			sourceNode=createNodeWithPropertiesAndLabel(src.getName(),"Node");
		
			//perform the post for loading node with properties
	
			srcNodeResponse=loadNodeAndPropertiesByPost(sourceNode);
		
			//add the source node to the map
			node_id_copy.put(src.getName(), srcNodeResponse.getId());
			
			//add node WITHOUT LINKS to nffg node list
			
			NodeType newNodeType = serviceOf.createNodeType();
			newNodeType.setFunctionalType(src.getFunctionalType());
			newNodeType.setName(src.getName());
			if(src.getHost()!=null)
				newNodeType.setHost(src.getHost());
			newNodeType.setNFFG(src.getNFFG());
			newNodeType.setLinks(serviceOf.createLinksType());
			newNodeType.setReachablehosts(serviceOf.createReachableHostsType());
			
			List<NodeType> nodesInNffg = nodesInNffg_copy.get(src.getNFFG());
			if(nodesInNffg != null) { //first time will be null
				
				nodesInNffg.add(newNodeType);
				
			}else {
				nodesInNffg = new CopyOnWriteArrayList<NodeType>();
				nodesInNffg.add(newNodeType);
				
				
			}
			nodesInNffg_copy.put(src.getNFFG(), nodesInNffg);
			
			//set the node id with the one in the response
			sourceNode.setId(srcNodeResponse.getId()); 
			
			// DEBUG System.out.println("The set id is "+sourceNode.getId());
			
			// set its label
			loadLabelByPost(sourceNode);
			
			//check if the node is allocated on a host
			String host=src.getHost();
			
			
			
			//create the host which source node is allocated on
			if(!node_id_copy.containsKey(host)) { // just if not yet created, because different node can be allocated on the same host
				
				
				
				// create the host
				hostNode=createNodeWithPropertiesAndLabel(host,"Host");
				
				//perform the post for loading host with properties
				hostResponse=loadNodeAndPropertiesByPost(hostNode);
				
				//add the host node to the map
				node_id_copy.put(host, hostResponse.getId());
				
				//set the host id with the one in the response
				hostNode.setId(hostResponse.getId()); 
				
				// set its label
				loadLabelByPost(hostNode);
				
			}
			
			
			
			// the relation allocated on for SOURCE NODE
			
			VirtualNodeType newNode=serviceOf.createVirtualNodeType();
			newNode.setNodeName(src.getName());
			newNode.setNodeNFFG(src.getNFFG());
			List<VirtualNodeType> hostNodes=nodesOnHost_copy.get(host);
			
			if(hostNodes != null) {
				hostNodes.add(newNode);
			}else {
				hostNodes = new CopyOnWriteArrayList<VirtualNodeType>();
				hostNodes.add(newNode);
			}
			nodesOnHost_copy.put(host, hostNodes);
			
			
			srcNodeOnHostRel=createRelationshipByIDs(node_id_copy.get(src.getName()),node_id_copy.get(host),"AllocatedOn");
			
			//load the relationship
			respNodeOnHostRel=loadRelationshipByPost(srcNodeOnHostRel);
			
			String AllocatedOnKey=null;
			AllocatedOnKey=new String(src.getName()+"_"+host);
			relationship_id_copy.put(AllocatedOnKey, respNodeOnHostRel.getId());
			
			
	
}

protected void ForwardTo_loading(NFFGtype nffg)throws ResponseProcessingException,ProcessingException,WebApplicationException {
		
	Map<String,String> relationship_id_copy=new ConcurrentHashMap<String,String>();
	Map<String,List<LinkType>> nodeLinks_copy=new ConcurrentHashMap<String,List<LinkType>>(nodeLinks);
	for(NodeType src:nffg.getNode()) {
			Relationship nodeToNodeRel = null;
			Relationship resp=null;
			if(src.getLinks()==null)
				continue;
				for(LinkType link:src.getLinks().getLink()) {
					
					
					
						nodeToNodeRel=createRelationshipByIDs(node_id.get(src.getName()),node_id.get(link.getDestinationNode()),"ForwardsTo");
						
						//load the relationship
						resp=loadRelationshipByPost(nodeToNodeRel);
						String ForwardsToKey=null;
						ForwardsToKey=new String(src.getName()+"_"+link.getDestinationNode());
						relationship_id_copy.put(ForwardsToKey, resp.getId());
						
						List<LinkType> links = nodeLinks_copy.get(src.getName());
						if(links != null)
							links.add(link);
						else {
							links = new CopyOnWriteArrayList<LinkType>();
							links.add(link);
						}
						nodeLinks_copy.put(src.getName(), links);
						
				}
			
			
		}
	relationship_id.putAll(relationship_id_copy);
	nodeLinks.putAll(nodeLinks_copy);
	
}

protected void overwriteLink(LinkType link,String nffg)throws InternalServerErrorException {
	//overwrite nffgsMap and nodeLinks
	try {
		int bothOk = 0;
		
		NFFGtype providedNffg = nffgsMap.get(nffg);
		for(NodeType node:providedNffg.getNode()) {
			if(node.getName().equals(link.getSourceNode())) {
				LinksType links = node.getLinks();
				if(links!=null) {
					for(LinkType l:links.getLink()) {
						if(l.getSourceNode().equals(link.getSourceNode()) && l.getDestinationNode().equals(link.getDestinationNode())) {
							l.setLatency(link.getLatency());
							l.setName(link.getSourceNode()+"_"+link.getDestinationNode());
							l.setThroughput(link.getThroughput());
							bothOk++;
						}
					}
				}
			}
		}
		for(LinkType l : nodeLinks.get(link.getSourceNode())) {
			if(l.getDestinationNode().equals(link.getDestinationNode())) {
				l.setLatency(link.getLatency());
				l.setName(link.getSourceNode()+"_"+link.getDestinationNode());
				l.setThroughput(link.getThroughput());
				bothOk++;
			}
		}
		if(bothOk == 2)
			logger.log(Level.INFO,"OverwriteLink succesfull");
	}catch(Exception e) {
		logger.log(Level.SEVERE,"Backend Service, overwriteLink: unexpected exception");
		throw new InternalServerErrorException(e.getMessage());
	}
}

protected void loadSingleLink(LinkType link, String nffg)throws ServerErrorException,InternalServerErrorException{
	
	try {
		logger.log(Level.INFO,"AddSingleLink: loadSingleLink invoked...");
		
		Relationship nodeToNodeRel = null;
		Relationship resp=null;
		nodeToNodeRel=createRelationshipByIDs(node_id.get(link.getSourceNode()),node_id.get(link.getDestinationNode()),"ForwardsTo");
		
		//load the relationship
		resp=loadRelationshipByPost(nodeToNodeRel);
		String ForwardsToKey=null;
		ForwardsToKey=new String(link.getSourceNode()+"_"+link.getDestinationNode());
		relationship_id.put(ForwardsToKey, resp.getId());
		link.setName(link.getSourceNode()+"_"+link.getDestinationNode());
		//update link list if exists otherwise create new one
		List<LinkType> nodeLinksList = nodeLinks.get(link.getSourceNode());
		if(nodeLinksList != null && !nodeLinksList.isEmpty())
			nodeLinksList.add(link);
		else {
			nodeLinksList = new CopyOnWriteArrayList<>();
			nodeLinksList.add(link);
			nodeLinks.put(link.getSourceNode(), nodeLinksList);
		}
		//update NFFG too
		NFFGtype providedNffg = nffgsMap.get(nffg);
		for(NodeType node:providedNffg.getNode()) {
			if(node.getName().equals(link.getSourceNode())) {
				LinksType links = node.getLinks();
				if(links!=null) {
					links.getLink().add(link);
				}else {
					links = serviceOf.createLinksType();
					links.getLink().add(link);
					node.setLinks(links);
				}
			}
		}
		
		logger.log(Level.INFO,"AddSingleLink: link "+link.getName()+" succesful load");
	}catch(ProcessingException|WebApplicationException exc) {
		logger.log(Level.INFO,"Backed service, AddSingleLink: backend server error");
		throw new ServerErrorException(Response.Status.BAD_GATEWAY);
	}catch(Exception e) {
		logger.log(Level.INFO,"Backed service, AddSingleLink: unexpected exception");
		throw new InternalServerErrorException();
	}
}

protected  Node createNodeWithPropertiesAndLabel(String name,String label) {
	Property property=neo4jOf.createProperty();
	property.setName("name");
	property.setValue(name);
	
	//add the property to node's properties
	Properties properties=neo4jOf.createProperties();
	properties.getProperty().add(property);
	
	//for each node create its labels
	Labels labels=neo4jOf.createLabels();
	
	labels.getLabel().add(label);
	
	//attach labels and properties to the node
	Node node=neo4jOf.createNode();
	node.setProperties(properties);
	node.setLabels(labels);
	node.setId("0");
	
	return node;
}

protected  Node loadNodeAndPropertiesByPost(Node node)throws ResponseProcessingException,ProcessingException,WebApplicationException {
	
		Node response = webTarget.path("data").path("node")
    		                   .request(MediaType.APPLICATION_XML_TYPE)//start to construct the request defining the response accepted media type
    		                   .post(Entity.entity(node,MediaType.APPLICATION_XML_TYPE),Node.class);// throws ResponseProcessingException in case processing RESPONSE fails
								//ProcessingException in case the REQUEST PROCESSING FAILS
    							//WebApplicationException in case response status code is not 2xx
    	
		return response;
}

protected void loadLabelByPost(Node node) throws ResponseProcessingException,ProcessingException,WebApplicationException {

Response response = webTarget.path("data").path("node").path(node.getId()).path("labels")
        .request(MediaType.APPLICATION_XML_TYPE)
        .post(Entity.entity(node.getLabels(),MediaType.APPLICATION_XML_TYPE));// throws ResponseProcessingException in case processing RESPONSE fails
//ProcessingException in case the REQUEST PROCESSING FAILS

try {
	if(response.getStatus()!=204) {
		throw new WebApplicationException(response);
	}
}finally {
	response.close();
}

}

protected  Relationship createRelationshipByIDs(String src,String dst,String type) {
	
	Relationship rel= neo4jOf.createRelationship();
	rel.setSrcNode(src); 
	rel.setDstNode(dst);
	rel.setType(type);
	
	return rel;
	
}

protected Relationship loadRelationshipByPost(Relationship rel)throws ResponseProcessingException,ProcessingException,WebApplicationException {
	
    Relationship response = webTarget.path("data").path("node").path(rel.getSrcNode()).path("relationships")
    		                   .request(MediaType.APPLICATION_XML_TYPE)
    		                   .post(Entity.entity(rel,MediaType.APPLICATION_XML_TYPE),Relationship.class);
		
	return response;	
}

protected boolean isLoaded(String nffgName) throws ServiceException {
	
	if(nffgName==null) {
		logger.log(Level.SEVERE,"Backend servive, isLoaded:the nffg name is null \n" );
		throw new ServiceException("Backend servive, isLoaded: the name of the required nffg is null");
	}
	if(!loadedNffgs.containsKey(nffgName)) {
		logger.log(Level.SEVERE,"the name of the required nffg is unknown \n" );
		throw new ServiceException("Backend servive, isLoaded: the name of the required nffg is unknown");
	}
	
	return new Boolean(loadedNffgs.get(nffgName));
}

protected Nodes getReachableNodes(String nodeID) throws ServiceException {
	Nodes response;
	try {
		 response=webTarget.path("data").path("node").path(nodeID).path("reachableNodes")
			.queryParam("relationshipTypes", "ForwardsTo")
			.queryParam("nodeLabel", "Node")
			.request()
			.accept(MediaType.APPLICATION_XML_TYPE)
			.get(Nodes.class);
	
	}catch(ResponseProcessingException rpe ){
	
		throw new ServiceException("Backend servive, getReachableNodes: error during JAX-RS response processing, more details following... \n"+rpe.getMessage());
	
	
	} catch(ProcessingException pe ){
	
		throw new ServiceException("Backend servive, getReachableNodes: error during JAX-RS request processing, more details following... \n"+pe.getMessage());
	
	
	} catch(WebApplicationException wae ){
	
		throw new ServiceException("Backend servive, getReachableNodes: bad client request or bad server response, more details following... \n"+wae.getMessage());
	
	} 
	catch(Exception e) {
	
		throw new ServiceException("Backend servive, getReachableNodes: unexpected exception, more details following... \n"+e.getMessage());
	
}
	return response;
}
/***********************************DELETE NODE*********************************************/
protected void deleteNode(NFFGtype nffg, NodeType toEliminate, boolean updateNffg)throws WebApplicationException,InternalServerErrorException {
	try {
		//eliminate from the NFFG
		if(updateNffg)
		{
			NFFGtype newNffg =serviceOf.createNFFGtype();
			newNffg.setName(nffg.getName());
			
			List<NodeType> nodeSet = nffg.getNode();
			
			for (NodeType nr: nodeSet) {
				
				if(nr.getName().equals(toEliminate.getName()))
					continue;
				
				NodeType node=serviceOf.createNodeType();
				
				node.setName(nr.getName());
				node.setNFFG(nffg.getName());
				node.setHost(nr.getHost());
				node.setFunctionalType(nr.getFunctionalType());
				
				
				List<LinkType> linkSet = nr.getLinks().getLink();
				
				LinksType links=serviceOf.createLinksType();
				
				for (LinkType lr: linkSet) {
					
					
					LinkType link=serviceOf.createLinkType();
					
					link.setName(lr.getSourceNode()+"_"+lr.getDestinationNode());
					
					link.setSourceNode(lr.getSourceNode());
					link.setDestinationNode(lr.getDestinationNode());
					link.setThroughput(lr.getThroughput());
					link.setLatency(new BigInteger(String.valueOf(lr.getLatency())));
					
					links.getLink().add(link);
					
				}
				
				if(links!=null && !links.getLink().isEmpty());
					node.setLinks(links);
					
			newNffg.getNode().add(node);
			}
			
			nffgsMap.put(nffg.getName(), newNffg);
	}
		
		//eliminate from nodeinNffg
		List<NodeType> nodesUnmodified = nodesInNffg.get(nffg.getName());
		nodesUnmodified.remove(toEliminate);
		nodesInNffg.put(nffg.getName(), nodesUnmodified);
		
		//eliminate from NEO4J  relationship id and node_id
		if(toEliminate.getHost()!=null) {
			String allocatedOnKey = new String(toEliminate.getName()+"_"+toEliminate.getHost());
			deleteRelationshipByID(relationship_id.get(allocatedOnKey));
			relationship_id.remove(allocatedOnKey);
			node_id.remove(toEliminate.getName());
		
		}
	}catch(WebApplicationException wae) {
		logger.log(Level.SEVERE,"BackendService deleteNode: backend service error received");
		throw wae;
	}catch(Exception e) {
		logger.log(Level.SEVERE,"BackendService deleteLink:backed service unexpected Exception");
		throw new InternalServerErrorException();
	}
	
}

/***********************************DELETE LINK *********************************************/
protected void deleteLink(NFFGtype nffg, LinkType toEliminate, boolean updateNffg) throws WebApplicationException,InternalServerErrorException {
	
	try {
		//eliminate from the NFFG
		if(updateNffg) {
			NFFGtype newNffg =serviceOf.createNFFGtype();
			newNffg.setName(nffg.getName());
			
			List<NodeType> nodeSet = nffg.getNode();
			
			for (NodeType nr: nodeSet) {
				
				NodeType node=serviceOf.createNodeType();
				
				node.setName(nr.getName());
				node.setNFFG(nffg.getName());
				node.setHost(nr.getHost());
				node.setFunctionalType(nr.getFunctionalType());
				
				
				List<LinkType> linkSet = nr.getLinks().getLink();
				
				LinksType links=serviceOf.createLinksType();
				
				for (LinkType lr: linkSet) {
					
					if(lr.getName().equals(toEliminate.getName()))
						continue;
					LinkType link=serviceOf.createLinkType();
					
					link.setName(lr.getSourceNode()+"_"+lr.getDestinationNode());
					
					link.setSourceNode(lr.getSourceNode());
					link.setDestinationNode(lr.getDestinationNode());
					link.setThroughput(lr.getThroughput());
					link.setLatency(new BigInteger(String.valueOf(lr.getLatency())));
					
					links.getLink().add(link);
					
				}
				
				if(links!=null && !links.getLink().isEmpty());
					node.setLinks(links);
					
			newNffg.getNode().add(node);
			}
			
			nffgsMap.put(nffg.getName(), newNffg);
		}
		
		//eliminate from NodeLinks
		List<LinkType> linksUnmodified = nodeLinks.get(toEliminate.getSourceNode());
		linksUnmodified.remove(toEliminate);
		if(!linksUnmodified.isEmpty()) {
			logger.log(Level.INFO, "backed service delete link: there are links ");
			nodeLinks.put(toEliminate.getSourceNode(), linksUnmodified);
		}else {
			logger.log(Level.INFO, "backed service delete link: no more links ");
			nodeLinks.remove(toEliminate.getSourceNode());
		}
		
		
		//eliminate from NEO4J and relationship id
		deleteRelationshipByID(relationship_id.get(toEliminate.getName()));
		relationship_id.remove(toEliminate.getName());
	}catch(WebApplicationException wae) {
		logger.log(Level.SEVERE,"BackendService deleteLink: backend service error received");
		throw wae;
	}catch(Exception e) {
		logger.log(Level.SEVERE,"BackendService deleteLink: unexpectedException");
		throw new InternalServerErrorException();
	}
	
}

protected void deleteRelationshipByID(String id)throws WebApplicationException {

Response response = webTarget.path("data").path("relationship").path(id)
		                   .request(MediaType.APPLICATION_XML_TYPE)
		                   .delete();
	
try {
	if(response.getStatus()!=204) {
		throw new WebApplicationException(response);
	}
}finally {
	response.close();
}
}


/*UTILS METHOD TO CREATE NEW NAMES*/

protected String newNffgName(Map<String,NFFGtype> nffgMap)
{
	String nffgName;
	
	do {
		nffgName = "Nffg" + BackendDB.getNextNffg();
	} while(nffgMap.get(nffgName) != null);
	
	return nffgName;
}

protected String newNodeName(Map<String, String> node_id, String vnfRef, String nffgName)
{
	String nodeName;
	long next = 0;
	
	do {
		nodeName = vnfRef + next + nffgName;
		next++;
	} while(node_id.get(nodeName) != null);
	
	return nodeName;
}


	

}
