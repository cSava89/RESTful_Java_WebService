package it.polito.dp2.NFV.sol3.client1;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.NoNodeException;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.client1.NfvReader.NfvReaderImpl;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.HostsType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGsType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;

import it.polito.dp2.NFV.sol3.jaxb.NfvSystemType;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;

public class DeployedNffgImpl implements DeployedNffg {
	
	private NfvSystemType nfv;
	private WebTarget target;
	private ObjectFactory of;
	private String NFFGname;
	private Logger logger=Logger.getLogger(DeployedNffgImpl.class.getName());
	
	public DeployedNffgImpl(NfvSystemType nfv,WebTarget target,ObjectFactory of,String NFFGname) {
		this.nfv=nfv;
		this.target=target;
		this.of=of;
		this.NFFGname=NFFGname;
	
	}
	
	/**
	 * Adds a new node to this already deployed NF-FG. The node is added without any link.
	 * A node name is chosen by the implementation
	 * @param type	the type of VNF the node to be added has to implement
	 * @param hostName	the name of the host requested for allocation or null if no specific host is requested
	 * @throws AllocationException if the node could not be added because allocation was not possible.
	 * @throws ServiceException	if any other error occurred when trying to deploy the NF-FG.
	 * returns	an interface for reading information about the added node.
	 */

	@Override
	public NodeReader addNode(VNFTypeReader type, String hostName) throws AllocationException, ServiceException {
		
		NodeType nodeDeployed = null;
		NodeType nodeToDeploy;
		NodeReader deployedNodeReader = null;
		
		try {
			 nodeToDeploy = of.createNodeType();
			
			//populate the node
			
			if(hostName != null)
				nodeToDeploy.setHost(hostName);
			
			nodeToDeploy.setFunctionalType(type.getName());
			
			nodeToDeploy.setNFFG(NFFGname);
			
			nodeToDeploy.setName(type.getName()+0+NFFGname);
		}catch(Exception e) {
			logger.log(Level.SEVERE,"Client1, addNode: unespected exception setting up node to deploy ");
			throw new ServiceException(e.getMessage());
		}
		
		
		try {
			nodeDeployed = target.path("nffgs").path(NFFGname)
					.request(MediaType.APPLICATION_XML).
					post(Entity.entity(of.createNode(nodeToDeploy), MediaType.APPLICATION_XML),NodeType.class);
					
		}catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: addNode-> JAX-RS request processing error \n");
			throw new ServiceException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: addNode-> service returned an allocation error \n");
			throw new AllocationException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: addNode-> unexpected exception \n");
			throw new ServiceException();
		}
	
		try {
			logger.log(Level.INFO, "Client1: added node "+nodeDeployed.getName()+" wait for reader...");
			if(getReader().getNode(nodeDeployed.getName())!=null)
				logger.log(Level.INFO, "Client1: reader returned correctly");
			deployedNodeReader = getReader().getNode(nodeDeployed.getName()); //create an up to date NfvReader and get the node deployed reader
		}catch(Exception e) {
			logger.log(Level.SEVERE,"Client1, addNode: unespected exception getting the reader of node deployed ");
			throw new ServiceException(e.getMessage());
		}
		return deployedNodeReader;
	}

	/**
	 * Adds a new link to this already deployed NF-FG without specification of throughput and latency requirements.
	 * A link name is chosen by the implementation
	 * @param source	the source node of the link
	 * @param dest	the destination node of the link
	 * @param overwrite	true if the link information has to be overwritten if the link was already present
	 * @throws NoNodeException if any of the nodes passed as arguments does not exist in the deployed NF-FG.
	 * @throws LinkAlreadyPresentException	if a link connecting the specified nodes was already present and overwrite is false.
	 * @throws ServiceException	if any other error occurred when trying to add the link.
	 * returns	an interface for reading information about the added link.
	 */
	
	@Override
	public LinkReader addLink(NodeReader source, NodeReader dest, boolean overwrite)
			throws NoNodeException, LinkAlreadyPresentException, ServiceException {
	
		LinkType deployedLink;
		LinkType linkToDeploy;
		String sourceNode;
		try {	
			linkToDeploy = of.createLinkType();
			deployedLink = null;
			sourceNode = source.getName();
			
			//linkToDeploy.setName(sourceNode+"_"+dest.getName());
			linkToDeploy.setSourceNode(sourceNode);
			linkToDeploy.setDestinationNode(dest.getName());
			linkToDeploy.setOverwrite(overwrite);
		}catch(Exception e) {
			logger.log(Level.SEVERE,"Client1, addLink: unespected exception setting up link to deploy ");
			throw new ServiceException(e.getMessage());
		}
		
		/******************************* CHECK THE RETURNED EXCEPTION FOR DISTINGUISH NoNodeException FROM LinkAlreadyPresent 
		 * 
		 */
		
		try {
			deployedLink = target.path("nffgs").path(NFFGname).path(sourceNode).path("links")
					.request(MediaType.APPLICATION_XML).
					post(Entity.entity(of.createLink(linkToDeploy), MediaType.APPLICATION_XML),LinkType.class);
					
		}catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: addLink-> JAX-RS request processing error \n");
			throw new ServiceException();
		}
		catch (NotFoundException nfe) {
			logger.log(Level.SEVERE,"Client1: addLink-> src or dest node not found error \n");
			throw new NoNodeException();
		}
		catch(ForbiddenException fex) {
			logger.log(Level.SEVERE,"Client1: addLink-> a link connecting the specified nodes was already present and overwrite is false. \n");
			throw new LinkAlreadyPresentException();
			
		}
		
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: addLink-> unexpected exception \n");
			throw new ServiceException();
		}
		
		
		
		
		LinkReader deployedLinkReader = null;
		
		//create an up to date NfvReader and get the link deployed reader
		try {
			Set<LinkReader> deployedLinks = getReader().getNode(source.getName()).getLinks();
			
			for(LinkReader link: deployedLinks) {
				
					if(link.getName().equals(deployedLink.getName())) {
						deployedLinkReader = link;
						break;
					}
			}
		}catch(Exception e) {
				logger.log(Level.SEVERE,"Client1, addLink: unespected exception getting the reader of link deployed ");
				throw new ServiceException(e.getMessage());
		}
		
		return deployedLinkReader; 
	}

	@Override
	public NffgReader getReader() throws ServiceException {
		nfv.setHosts(getHosts());
		nfv.setNffgs(getNffgs());
		NfvReader nfv_reader=new NfvReaderImpl(nfv);
		return nfv_reader.getNffg(NFFGname);
	}
	
	
	
	private NFFGsType getNffgs() throws ServiceException{
		
		NFFGsType nffgs;
		
		try {
			nffgs = target.path("nffgs").request(MediaType.APPLICATION_XML)
					.get(NFFGsType.class);
		}catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: DeployedNffg getNffgs-> JAX-RS request processing error \n");
			throw new ServiceException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: DeployedNffg getNffgs-> server returned an exception \n");
			throw new ServiceException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: DeployedNffgs getNffgs-> unexpected exception \n");
			throw new ServiceException();
		}
		
		NFFGsType completeNffgs=of.createNFFGsType();
		
		for(NFFGtype nffg:nffgs.getNffg()) {
			
			NFFGtype newNffg=getNffg(nffg.getName());
			completeNffgs.getNffg().add(newNffg);			
		}
		
		return completeNffgs;
		
	}

	private NFFGtype getNffg(String name) throws ServiceException {
		
				NFFGtype nffg;
				
				try {
					nffg = target.path("nffgs").path(name)
					             .request(MediaType.APPLICATION_XML)
					             .get(NFFGtype.class);
				}
				catch (ProcessingException pe) {
					logger.log(Level.SEVERE,"Client1: DeployedNffg getNffg-> JAX-RS processing request error \n");
					throw new ServiceException();
				}
				catch (WebApplicationException wae) {
					logger.log(Level.SEVERE,"Client1: DeployedNffg getNffg-> service returned a BadRequestException \n");
					throw new ServiceException();
				}
				catch (Exception e) {
					logger.log(Level.SEVERE,"Client1: DeployedNffg getNffg-> unexpected exception \n");
					throw new ServiceException();
				}
				
				return nffg;
			
	}
private HostsType getHosts()throws ServiceException {
		
		HostsType hosts;
		
		try {
			hosts = target.path("hosts")
			              .request(MediaType.APPLICATION_XML)
			              .get(HostsType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: getHosts-> JAX-RS request processing error \n");
			throw new ServiceException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: getHosts-> service returned an error \n");
			throw new ServiceException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: getHosts-> unexpected exception \n");
			throw new ServiceException();
		}
		
		// Create a complete host set (including nodeRefs)
		HostsType completeHosts = of.createHostsType();
		
		for (HostType host: hosts.getHost())
		{
			HostType newHost = getHost( host.getName() );
			completeHosts.getHost().add(newHost);
		}
		
		return completeHosts;
	}
	
	private HostType getHost(String hostName) throws ServiceException{
		
		HostType host;
		
		try {
			host = target.path("hosts").path(hostName)
			             .request(MediaType.APPLICATION_XML)
			             .get(HostType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: getHost-> JAX-RS request processing error \n");
			throw new ServiceException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: getHost-> server returned an exception \n");
			throw new ServiceException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: getHost-> unexpected exception \n");
			throw new ServiceException();
		}
		
		return host;
	}

}
