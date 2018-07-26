package it.polito.dp2.NFV.sol3.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.sol3.service.AllocationException;
import it.polito.dp2.NFV.sol3.service.ServiceException;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.LinksType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGsType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.ReachableHostsType;
import it.polito.dp2.NFV.sol3.neo4j.Node;
import it.polito.dp2.NFV.sol3.neo4j.Nodes;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/nffgs")
public class NffgsResource implements NffgsInterface {
	
	private BackendSERVICE backend;
	private Logger logger=Logger.getLogger(NffgsResource.class.getName());
	private ObjectFactory serviceOf;
	private Map<String,NFFGtype> nffgsMap;
	private Map<String,String> node_id; //to check existence of nodes to deploy
	private Map<String,String> rel_id; //to check existence of links to deploy
	private Map<String,Boolean> loadedNFFGs;
	private Map<String,List<NodeType>> nodesInNffg;
	private Map<String,HostType> hostMap;
	private Map<String, List<LinkType>> nodeLinks;
	
	public NffgsResource() throws AllocationException,InternalServerErrorException {
		
		try {
			
			backend=BackendSERVICE.getInstance();
		
		}catch(BadRequestException | ServerErrorException e) {
			
			logger.log(Level.SEVERE,"NffgsResource: something wrong in NfvDeployer backend initialization \n");
			throw new InternalServerErrorException();
		}catch(ClientErrorException c) {
			
			logger.log(Level.SEVERE,"NffgsResource: nffg0 deploy leads to an allocation problem \n");
			throw new AllocationException();
			
		}
		
		nffgsMap=BackendDB.getNffgsMap();
		node_id = BackendDB.getNode_id();
		rel_id = BackendDB.getRelationships_id();
		loadedNFFGs= BackendDB.getLoadedNffgs();
		nodesInNffg = BackendDB.getNodesInNffg();
		hostMap = BackendDB.getHostMAP();
		nodeLinks = BackendDB.getNodeLinks();
		serviceOf=new ObjectFactory();
	}
	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<NFFGsType> getNffgs(@QueryParam("from")String from) {
		NFFGsType nffgs=null;
		try {
			if(from==null) { //must retrieve all nffgs
				
				 nffgs=serviceOf.createNFFGsType();
				
				for(NFFGtype nffg:nffgsMap.values()) {
					
					NFFGtype newNffg=serviceOf.createNFFGtype();
					
					newNffg.setName(nffg.getName());
					newNffg.setDeployTime(nffg.getDeployTime());
					nffgs.getNffg().add(newNffg);
				}
			}else {
					nffgs=serviceOf.createNFFGsType();
				
					for(NFFGtype nffg:nffgsMap.values()) {
						
						XMLGregorianCalendar gc=nffg.getDeployTime();
						Date date=gc.toGregorianCalendar().getTime();
						Calendar c=Calendar.getInstance();
						c.setTime(date);
						
						logger.log(Level.INFO,"NFFG resource, get nffgs: checking deployed nffgs after"+from);
						
						Calendar toCheck = Calendar.getInstance();
						SimpleDateFormat sdfToCheck = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
						toCheck.setTime(sdfToCheck.parse(from));
						
						
						
						if(c.after(toCheck) || c.equals(toCheck)) {
					
						NFFGtype newNffg=serviceOf.createNFFGtype();
					
						newNffg.setName(nffg.getName());
						newNffg.setDeployTime(nffg.getDeployTime());
						nffgs.getNffg().add(newNffg);
						}
					}
			}
		}catch(ParseException pe) {
			logger.log(Level.SEVERE,"NFFG resource, get nffgs: parsing exception, query parameter from bad formed "+from);
			throw new BadRequestException();
		}
		catch(Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, get nffgs: unexpected exception \n");
			throw new InternalServerErrorException();
		}
		return serviceOf.createNffgs(nffgs);
	}
	@POST
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 400, message = "Bad Request"),
    		@ApiResponse(code = 409, message = "Allocation Exception"),
    		@ApiResponse(code = 502, message = "Bad Gateway"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<NFFGtype> postNffg(JAXBElement<NFFGtype> nffgToDeploy) {
		
		NFFGtype nffg= null ;
		
		try {
		
			if(nffgToDeploy == null || !(nffgToDeploy.getValue() instanceof NFFGtype)) {
				logger.log(Level.SEVERE,"NFFG resource, post nffg: the incoming nffg is null or not a NFFGType \n");
				throw new BadRequestException();
			}
			
				nffg= nffgToDeploy.getValue(); // a possible error can be the retrieving of the NffgType
				
				synchronized(nffgsMap) {
					//check the name of the NFFG to deploy
					String nffgName = nffg.getName();
					if(nffgName==null || nffgsMap.get(nffgName)!=null || nffgName.equals("dummyNffg")) { // if name is null or already present create a new Name
						nffgName = backend.newNffgName(nffgsMap);
						nffg.setName(nffgName);
					
					}
					//check node names
					HashSet<String> nodeNames = new HashSet<String>();
					Map<String,NodeType> oldname_newNode = new HashMap<String,NodeType>();
					List<NodeType> nffgToDeployNodes = nffg.getNode();
					for(NodeType node: nffgToDeployNodes) {
						String oldName = node.getName(); //in theory it cannot be null
						if(oldName == null) {
							logger.log(Level.SEVERE,"NFFG resource, post nffg: node to deploy has null name \n");
							throw new BadRequestException();
						}
						if(node_id.containsKey(node.getName())||nodeNames.contains(oldName))
								node.setName(backend.newNodeName(node_id, node.getFunctionalType(), nffgName));
						node.setNFFG(nffgName);
						oldname_newNode.put(oldName, node);
						nodeNames.add(node.getName());
						
					}
					
					//check link name and set correct src and destination
					HashSet<String> linkNames = new HashSet<String>();
					for(NodeType node:nffgToDeployNodes) {
						LinksType nodeLinks = node.getLinks();
						if(nodeLinks == null)
							continue;
						for(LinkType link:nodeLinks.getLink()) {//otherwise for each link in links
							if(link.getName() == null ||link.getDestinationNode() == null || link.getSourceNode()==null) {
								logger.log(Level.SEVERE,"NFFG resource, post nffg: link to deploy has null parameters \n");
								throw new BadRequestException();
							}
							String linkName=oldname_newNode.get(link.getSourceNode()).getName()+"_"+oldname_newNode.get(link.getDestinationNode()).getName();
							if(linkNames.contains(linkName)) {
								logger.log(Level.INFO,"Link already inserted "+linkName);
								continue;
								
							}
							logger.log(Level.INFO,"to be inserted "+linkName);
							linkNames.add(linkName);
							link.setName(linkName);
							link.setSourceNode(oldname_newNode.get(link.getSourceNode()).getName());	
							link.setDestinationNode(oldname_newNode.get(link.getDestinationNode()).getName());
							//the other parameters are set by default if null
						}
					}
				
					
					backend.CheckAllocationAndLoadGraph(nffg);
					nffgsMap.put(nffg.getName(), nffg);
					loadedNFFGs.put(nffg.getName(), true);
					
					
				 }
		}catch(BadRequestException bdr) {
			throw bdr;
		}catch(ClientErrorException cee) {
			throw cee;
		}catch(InternalServerErrorException isee) {
			throw isee;
		}catch(ServerErrorException see) {
			throw see;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, post nffg: unexpected exception \n");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createNffg(nffg);
	}
    @GET
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("/{nffg}")
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<NFFGtype> getNffg(@PathParam("nffg")String nffg) {
    	
	    	NFFGtype requiredNFFG=null;
	    		try {
		    		
				synchronized(nffgsMap) {
					if(nffgsMap.containsKey(nffg))
						requiredNFFG=nffgsMap.get(nffg);
						
				}
				if(requiredNFFG==null)
					throw new NotFoundException();
	    		}catch(NotFoundException nfe) {
	    			logger.log(Level.SEVERE,"NFFG resource, get nffg: nffg not found \n");
	    			throw nfe;
	    		}catch(Exception e) {
	    			logger.log(Level.SEVERE,"NFFG resource, get nffg: unexpected exception \n");
	    			throw new InternalServerErrorException();
	    			
	    		}
		return serviceOf.createNffg(requiredNFFG);
	}
	
	@DELETE
	@ApiResponses(value = {
    		@ApiResponse(code = 204, message = "No content"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("/{nffg}")
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public void deleteNffg(@PathParam("nffg")String nffg) {
		NFFGtype toEliminate = null;
		LinksType linksToEliminate = null;
		
		try {
			toEliminate = nffgsMap.get(nffg);
			if(toEliminate == null) {
				logger.log(Level.SEVERE,"Nffg resource, deleteNffg: provided node not found");
				throw new NotFoundException();
			}
			synchronized(toEliminate) {
				for(NodeType node:toEliminate.getNode()) {
					//eliminate all relationship for that node
					logger.log(Level.INFO, "Node under elaboration "+node.getName());
					linksToEliminate = node.getLinks();
					if(linksToEliminate!=null && !linksToEliminate.getLink().isEmpty()) {
						logger.log(Level.INFO, "Node under elaboration has links");
						for(LinkType link:linksToEliminate.getLink()) {
							backend.deleteLink(toEliminate, link, false);
							logger.log(Level.INFO, "Deleted link "+link.getName());
						}
					}
					//eliminate the node
					backend.deleteNode(toEliminate, node,false);
					logger.log(Level.INFO, "Deleted node"+node.getName());
					
				}
				//after that remove nffg from internal structure
				nffgsMap.remove(nffg);
				logger.log(Level.INFO, nffg+" removed from nffgMap");
				nodesInNffg.remove(nffg);
				logger.log(Level.INFO, nffg+" removed from nodesInNffg");
				loadedNFFGs.remove(nffg);
				logger.log(Level.INFO, nffg+" removed from loadedNffgs");
				if(nffgsMap.get(nffg)==null && nodesInNffg.get(nffg)==null
						&& loadedNFFGs.get(nffg)==null)
					logger.log(Level.INFO, "NFFG undeploy complete!");
			}
		}catch(NotFoundException nf) {
			logger.log(Level.SEVERE,"NffgResource deleteNFFG: some resources not found \n");
			throw nf;
			
		}catch(InternalServerErrorException e) {
			logger.log(Level.SEVERE,"NffgResource deleteNFFG: internal server error from backend service \n");
			throw e;
			
		}catch(WebApplicationException e) {
			logger.log(Level.SEVERE,"NffgResource, deleteNFFG: internal server error from neo4j backend service \n");
			throw e;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NffgResource, deleteNFFG: unexpected exception "+e.getMessage());
			throw new InternalServerErrorException();
		}
		//throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
	}
	@POST
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 400, message = "Bad Request"),
    		@ApiResponse(code = 409, message = "Allocation Exception"),
    		@ApiResponse(code = 502, message = "Bad Gateway"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("/{nffg}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<NodeType> postNode(@PathParam("nffg") String nffg, JAXBElement<NodeType> nodeToDeploy){
		
		NodeType node=null;
		
		try {
			if(!(nodeToDeploy.getValue() instanceof NodeType)) {
				logger.log(Level.SEVERE,"NFFG resource, post node: the node to deploy is not of NodeType \n");
				throw new BadRequestException();
			}
			
			node = nodeToDeploy.getValue();
			NFFGtype container = nffgsMap.get(nffg) ;
			if(container==null ) {
				logger.log(Level.SEVERE,"NFFG resource, post node: the belonging nffg is not loaded or null \n");
				throw new NotFoundException();
			}
			
			
			//TAKE THE NFFG WHICH THE NODE BELONGS TO AND SYNC ON IT AVOIDING TO UNDEPLOY THAT NFFG WHILE ADDING THE NODE
			synchronized(container) {
				
				//check the name. LINKS AND REACHABLE ARE EMPTY
				String oldName = node.getName(); //in theory it cannot be null
				if(oldName == null) {
					logger.log(Level.SEVERE,"NFFG resource, post node: node to deploy has null name \n");
					throw new BadRequestException();
				}
				if(node_id.containsKey(node.getName()))
						node.setName(backend.newNodeName(node_id, node.getFunctionalType(), nffg));
				backend.checkAllocationAndLoadNode(node);
			}
		}catch(BadRequestException bdr) {
			throw bdr;
		}catch(ClientErrorException cee) {
			throw cee;
		}catch(InternalServerErrorException isee) {
			throw isee;
		}catch(ServerErrorException see) {
			throw see;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, post node: unexpected exception \n");
			throw new InternalServerErrorException();
		}
		return serviceOf.createNode(node);
	}
	
	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("{nffg}/{node}")
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<NodeType> getNode(@PathParam("nffg")String nffg,@PathParam("node") String nodeName) {
		NodeType foundNode = null; 
		try {
				foundNode = serviceOf.createNodeType();
				//check nffg existence
				NFFGtype nffgProvided = nffgsMap.get(nffg);
				if(nffgProvided == null || nffg == null) {
					logger.log(Level.SEVERE, "NFFG resource, getNode: provided nffg not found \n");
					throw new NotFoundException();
				}
				synchronized(nffgProvided) {
					
					
					//check if node is loaded
					if(!node_id.containsKey(nodeName)){
						logger.log(Level.SEVERE, "NFFG resource, getNode: provided node not found \n");
						throw new NotFoundException();
					}
					//check if node belongs to provided nffg
					boolean found = false;
					List<NodeType> nodesInProvidedNffg = nodesInNffg.get(nffg);
					for(NodeType node:nodesInProvidedNffg) {
						if(node.getName().equals(nodeName)) {
							found = true;
							foundNode.setName(node.getName());
							foundNode.setNFFG(nffg);
							foundNode.setHost(node.getHost());
							foundNode.setFunctionalType(node.getFunctionalType());
							break;
						}
					}
					if(!found) {
						logger.log(Level.SEVERE,"NFFG resource, getNode: sourceNode not belongs to provided nffg  \n");
						throw new NotFoundException();
					}
			}
		}catch(NotFoundException nfe) {
			throw nfe;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, getNode: unexpected exception");
			throw new InternalServerErrorException();
		}
		return serviceOf.createNode(foundNode);
}
	@DELETE
	@ApiResponses(value = {
    		@ApiResponse(code = 204, message = "No content"),
    		@ApiResponse(code = 403, message = "Forbidden"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("{nffg}/{node}")
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public void deleteNode(@PathParam("nffg")String nffg,@PathParam("node") String node) {
		
		NFFGtype container=null;
		NodeType toEliminate=null;
		List<LinkType> inOutLinks=null;
		boolean found = false;
		try {
			container = nffgsMap.get(nffg);
			if(container==null)
			{
				logger.log(Level.SEVERE,"NFFG resource, deleteNode: provided nffg not found");
				throw new NotFoundException();
			}
			synchronized(container) {
				for(NodeType n:nodesInNffg.get(nffg)) {
					if(n.getName().equals(node)) {
						found=true;
						toEliminate=n;
						break;
					}
				}
				if(!found) {
					logger.log(Level.SEVERE,"NFFG resource, deleteNode: provided node not found");
					throw new NotFoundException();
				}
				//CHECK FOR INCOMING OR OUTGOING LINKS
				inOutLinks = nodeLinks.get(node);
				if(inOutLinks != null) {
					logger.log(Level.SEVERE,"NFFG resource, deleteNode: delete forbidden! there are in or out links!");
					throw new ForbiddenException();
				}
				//if I am here it is possible to eliminate the node
				backend.deleteNode(container,toEliminate,true);
				if(nodesInNffg.get(nffg).contains(toEliminate) || node_id.containsKey(node)) {
					logger.log(Level.SEVERE,"NffgResource deleteNode: delete failed \n");
					
				}
			}
		}catch(NotFoundException nf) {
			logger.log(Level.SEVERE,"NffgResource deleteNode: some resources not found \n");
			throw nf;
		}catch(ForbiddenException f) {
			logger.log(Level.SEVERE,"NffgResource deleteNode: forbidden! incoming or outgoing link present \n");
			throw f;
		}catch(InternalServerErrorException e) {
			logger.log(Level.SEVERE,"NffgResource, deleteNode: internal server error from backend service \n");
			throw e;
			
		}catch(WebApplicationException e) {
			logger.log(Level.SEVERE,"NffgResource, deleteNode: internal server error from neo4j backend service \n");
			throw e;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NffgResource, deleteNode: resource unexpected exception "+e.getMessage());
			throw new InternalServerErrorException();
		}

		//throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
	}

	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 502, message = "Bad Gateway"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("{nffg}/{node}/reachablehosts")
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<ReachableHostsType> getReachableHosts(@PathParam("nffg")String nffg,@PathParam("node") String sourceNode)  {
		
		ReachableHostsType reachableHosts = null;
		try {
			
			reachableHosts= serviceOf.createReachableHostsType();
			//check nffg existence
			NFFGtype nffgProvided = nffgsMap.get(nffg);
			if(nffgProvided == null || nffg == null) {
				logger.log(Level.SEVERE, "NFFG resource, getReachableHosts: provided nffg not found \n");
				throw new NotFoundException();
				
			}
			synchronized(nffgProvided) {
				
				
				//check if node is loaded
				if(!node_id.containsKey(sourceNode)){
					logger.log(Level.SEVERE, "NFFG resource, getReachableHosts: provided node not found \n");
					throw new NotFoundException();
					
				}
				//check if node belongs to provided nffg
				boolean found = false;
				List<NodeType> nodesInProvidedNffg = nodesInNffg.get(nffg);
				for(NodeType node:nodesInProvidedNffg) {
					if(node.getName().equals(sourceNode)) {
						found = true;
						break;
					}
				}
				if(!found) {
					logger.log(Level.SEVERE,"NFFG resource, getReachableHosts: sourceNode not belongs to provided nffg  \n");
					throw new NotFoundException();
					
				}
				
					Nodes response;
					response = backend.getReachableNodes(node_id.get(sourceNode));
					
					
					reachableHosts.setStartingNode(sourceNode);
					
					HostType foundHost=null;
					//boolean foundNode = false;
					String hostName = null;
					for(Node node: response.getNode()) {
						String nodeName=node.getProperties().getProperty().get(0).getValue(); //take node name
						for(NodeType nodeT:nodesInNffg.get(nffg)) { //for each node of the provided nffg
							if(nodeT.getName().equals(nodeName)) {
								HostType resultWithoutNode = serviceOf.createHostType();
								hostName = nodeT.getHost(); //take the name of the host on which response node is virtualized
								foundHost = hostMap.get(hostName);
								if(foundHost == null) {
									logger.log(Level.SEVERE,"NFFG resource, getReachableHosts: response node not found  \n");
									throw new InternalServerErrorException();
									
								}
								resultWithoutNode.setName(hostName);
								resultWithoutNode.setAmountOfMemory(foundHost.getAmountOfMemory());
								resultWithoutNode.setDiskStorage(foundHost.getDiskStorage());
								resultWithoutNode.setNumberOfVNFs(foundHost.getNumberOfVNFs());
								reachableHosts.getHost().add(resultWithoutNode);
								break;
							}
						}
					}
					
					//add reachable host from starting node
					for(NodeType nodeT:nodesInNffg.get(nffg)) { //for each node of the provided nffg
						if(nodeT.getName().equals(sourceNode)) {
							HostType resultWithoutNode = serviceOf.createHostType();
							hostName = nodeT.getHost(); //take the name of the host on which response node is virtualized
							foundHost = hostMap.get(hostName);
							if(foundHost == null) {
								logger.log(Level.SEVERE,"NFFG resource, getReachableHosts: response node not found  \n");
								throw new InternalServerErrorException();
								
							}
							resultWithoutNode.setName(hostName);
							resultWithoutNode.setAmountOfMemory(foundHost.getAmountOfMemory());
							resultWithoutNode.setDiskStorage(foundHost.getDiskStorage());
							resultWithoutNode.setNumberOfVNFs(foundHost.getNumberOfVNFs());
							reachableHosts.getHost().add(resultWithoutNode);
							break;
						}
					}
					
					
				
			}
		}catch (NotFoundException nfe) {
			throw nfe;
			
		}catch (InternalServerErrorException isee) {
			throw isee;
			
		}catch (ServiceException se) {
			logger.log(Level.SEVERE,"NFFG resource, getReachableHosts: not able to retrieve reachable nodes  \n");
			throw new ServerErrorException(Response.Status.BAD_GATEWAY);
			
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, getReachableHosts: unexpected exception  \n");
			throw new InternalServerErrorException();
			
		}
		
		return serviceOf.createReachablehosts(reachableHosts);
		
	}
	
	@POST
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 400, message = "Bad Request"),
    		@ApiResponse(code = 403, message = "Forbidden"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 502, message = "Bad Gateway"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("{nffg}/{node}/links")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<LinkType> postLink(@PathParam("nffg")String nffg,@PathParam("node") String sourceNode, JAXBElement<LinkType> linkToDeploy) {
		
		LinkType incomingLink = null;
		try {
			if(!(linkToDeploy.getValue() instanceof LinkType)) {
				logger.log(Level.SEVERE,"NFFG resource, post link: link to deploy is not instanceof LinkType \n");
				throw new BadRequestException();
			}
			NFFGtype container =nffgsMap.get(nffg);
			if(container == null ) {
				logger.log(Level.SEVERE,"NFFG resource, post link: the provided nffg is not loaded or null \n");
				throw new NotFoundException();
				
			}
			LinkType toOverwrite = serviceOf.createLinkType();
			
			synchronized(container) {
			
			
			 incomingLink= linkToDeploy.getValue();
			 
			 if(incomingLink.getSourceNode().equals(incomingLink.getDestinationNode())) {
				 logger.log(Level.SEVERE,"NFFG resource, post link: bad request! source and destination of link are equal \n");
					throw new BadRequestException();
			 }
			//check if source and dest node are valid
			
			
				
			if(!node_id.containsKey(incomingLink.getSourceNode())) {
				logger.log(Level.SEVERE,"NFFG resource, post link: sourceNode not loaded or null \n");
				throw new NotFoundException();
			}
			boolean found = false;
			List<NodeType> nodesInProvidedNffg = nodesInNffg.get(nffg);
			for(NodeType node:nodesInProvidedNffg) {
				if(node.getName().equals(incomingLink.getSourceNode())) {
					found = true;
					break;
				}
			}
			if(!found) {
				logger.log(Level.SEVERE,"NFFG resource, post link: sourceNode not belongs to provided nffg  \n");
				throw new NotFoundException();
			}
			if(!node_id.containsKey(incomingLink.getDestinationNode())) {
				logger.log(Level.SEVERE,"NFFG resource, post link: destinationNode not loaded or null \n");
				throw new NotFoundException();
			}
			
			found = false;
			
			for(NodeType node:nodesInProvidedNffg) {
				if(node.getName().equals(incomingLink.getDestinationNode())) {
					found = true;
					break;
				}
			}
			if(!found) {
				logger.log(Level.SEVERE,"NFFG resource, post link: destinationNode not belongs to provided nffg \n");
				throw new NotFoundException();
			}
			
			found = false;
			if(nodeLinks.get(incomingLink.getSourceNode())!=null) {
				for(LinkType l: nodeLinks.get(incomingLink.getSourceNode()) ) {
					if(l.getDestinationNode().equals(incomingLink.getDestinationNode())) {
						found = true;
						toOverwrite.setName(l.getName());
						toOverwrite.setSourceNode(l.getSourceNode());
						toOverwrite.setDestinationNode(l.getDestinationNode());
						toOverwrite.setLatency(l.getLatency());
						toOverwrite.setThroughput(l.getThroughput());
						//overwritten??
						break;
					}
				}
			}
			
			//if I am here the entries are valid. Now check existence of link
			
				if(found) {
					if(incomingLink.isOverwrite() != null && incomingLink.isOverwrite()) {
						logger.log(Level.INFO, "Try to override link:"+incomingLink.getName());
						backend.overwriteLink(incomingLink,nffg);
						
					}
					else {
						logger.log(Level.SEVERE,"NFFG resource, post link: forbidden! link already exists and overwrite false \n");
						throw new ForbiddenException();
					}
				}else if(!found) {
				logger.log(Level.INFO, "Try to create new link...:"+incomingLink.getName());
				backend.loadSingleLink(incomingLink,nffg);
				
				
				}
				
			}
			
		}catch(BadRequestException bre) {
			throw bre;
		}catch(NotFoundException nfe) {
			throw nfe;
		}catch(ForbiddenException fe) {
			throw fe;
		}catch(InternalServerErrorException ise) {
			throw ise;
		}catch(ServerErrorException see) {
			throw see;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NFFG resource, post link: unexpected exception \n");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createLink(incomingLink);
	}
	
	@DELETE
	@ApiResponses(value = {
    		@ApiResponse(code = 204, message = "No content"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Path("{nffg}/{node}/links/{link}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public void deleteLink(@PathParam("nffg")String nffg,@PathParam("node") String sourceNode,@PathParam("link") String link) {
		
		NFFGtype nffgContainer=null;
		boolean found = false;
		LinkType linkToEliminate = null;
		try {
			nffgContainer = nffgsMap.get(nffg);
			if(nffgContainer == null) {
				logger.log(Level.SEVERE,"NffgResource deleteLink: nffg provided not found \n");
				throw new NotFoundException();
			}
			synchronized(nffgContainer) {
				
				for(NodeType node:nodesInNffg.get(nffg))
					if(node.getName().equals(sourceNode)) {
						found=true;
						break;
					}
				if(!found) {
					logger.log(Level.SEVERE,"NffgResource deleteLink: node provided not in provided nffg \n");
					throw new NotFoundException();
				}
				found = false;
				if(nodeLinks.get(sourceNode)!=null) {
					for(LinkType l:nodeLinks.get(sourceNode))
						if(l.getName().equals(link)) {
							found=true;
							linkToEliminate=l;
							break;
						}
				}
				if(!found) {
					logger.log(Level.SEVERE,"NffgResource deleteLink: link provided not in provided node \n");
					throw new NotFoundException();
				}
				backend.deleteLink(nffgContainer,linkToEliminate,true);
				if(rel_id.containsKey(link)) {
					logger.log(Level.SEVERE,"NffgResource deleteLink: delete failed \n");
					
				}
			}
			
		}catch(NotFoundException nf) {
			logger.log(Level.SEVERE,"NffgResource deleteLink: some resources not found \n");
			throw nf;
		}catch(InternalServerErrorException e) {
			logger.log(Level.SEVERE,"NffgResource deleteLink: internal server error from backend service \n");
			throw e;
			
		}catch(WebApplicationException e) {
			logger.log(Level.SEVERE,"NffgResource, deleteLink: internal server error from neo4j backend service \n");
			throw e;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"NffgResource, deleteLink: unexpected exception \n");
			throw new InternalServerErrorException();
		}
		
		
		
		//throw new WebApplicationException(Response.Status.NOT_IMPLEMENTED);
	}



	

}
