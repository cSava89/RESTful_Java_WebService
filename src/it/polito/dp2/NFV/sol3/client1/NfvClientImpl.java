package it.polito.dp2.NFV.sol3.client1;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkDescriptor;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.NodeDescriptor;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;

import it.polito.dp2.NFV.sol3.jaxb.CatalogueType;
import it.polito.dp2.NFV.sol3.jaxb.ConnectionsType;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.HostsType;
import it.polito.dp2.NFV.sol3.jaxb.LinkType;
import it.polito.dp2.NFV.sol3.jaxb.LinksType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NfvSystemType;
import it.polito.dp2.NFV.sol3.jaxb.NodeType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;

public class NfvClientImpl implements NfvClient {
	
	private static long NFFG=1;
	private URI targetUrl;
	private ObjectFactory serviceOf;
	private Logger logger=Logger.getLogger(NfvClientImpl.class.getName());
	private WebTarget target;
	private NfvSystemType nfvSystem;
	
	
	public NfvClientImpl() throws URISyntaxException, NfvClientException {
		
		
		try {
			
			targetUrl=new URI(System.getProperty("it.polito.dp2.NFV.lab3.URL"));
		
		}catch(URISyntaxException | NullPointerException n) {
			
			targetUrl=new URI("http://localhost:8080/NfvDeployer/rest/");
		}
		
		serviceOf=new ObjectFactory();
		
		target=ClientBuilder.newClient().target(targetUrl); //I use the garbage collector to close the client since there are several
		//interaction with service
		
		
		
		nfvSystem=serviceOf.createNfvSystemType();
		nfvSystem.setConnections(getConnections());
		nfvSystem.setCatalogue(getCatalogue());
		
	}
	
	private CatalogueType getCatalogue() throws NfvClientException
	{
		
		CatalogueType catalogue;
		
		try {
			catalogue = target.path("catalogue")
			                .request(MediaType.APPLICATION_XML)
			                .get(CatalogueType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: getCatalogue-> JAX-RS request processing error \n");
			throw new NfvClientException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: getCatalogue-> service returned an error \n");
			throw new NfvClientException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: getCatalogue-> unexpected exception \n");
			throw new NfvClientException();
		}
		
		return catalogue;
	}
	
	
	private ConnectionsType getConnections() throws NfvClientException
	{
		
		ConnectionsType connections;
		
		try {
			connections = target.path("connections")
			                    .request(MediaType.APPLICATION_XML)
			                    .get(ConnectionsType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: getConnections-> JAX-RS request processing error \n");
			throw new NfvClientException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: getConnections-> server returned an exception \n");
			throw new NfvClientException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: getConnections-> unexpected exception \n");
			throw new NfvClientException();
		}
		
		return connections;
	}

	
	/**
	 * Deploys a new NF-FG with the features described by an NffgDescriptor and returns an object that can be used to interact with the deployed NF-FG.
	 * @param nffg	an object that describes the NF-FG to be deployed
	 * @throws AllocationException if the NF-FG could not be deployed because allocation was not possible.
	 * @throws ServiceException	if any other error occurred when trying to deploy the NF-FG.
	 * returns an object that implements the DeployedNffg interface and that allows interaction with the deployed NF-FG
	 */

	@Override
	public DeployedNffg deployNffg(NffgDescriptor nffg) throws AllocationException, ServiceException {
		
		NFFGtype deployed;
		NFFGtype nffgToDeploy;
		long nodeName=1;
	
		try {	
			Map<NodeDescriptor,String> descriptor_name=new HashMap<NodeDescriptor,String>(); //used for link correct managing
			
			for(NodeDescriptor node:nffg.getNodes()) {
				descriptor_name.put(node, node.getFuncType().getName()+nodeName+"Nffg"+NFFG); //TO CORRECTLY CONSTRUCT THE LINKS SAVE NODEDESC AND NAME
				nodeName++;
			}
			
			
			
			nffgToDeploy=serviceOf.createNFFGtype();
			
			for(NodeDescriptor node:nffg.getNodes()) {
				
				NodeType nodeT=serviceOf.createNodeType();
				
				nodeT.setName(descriptor_name.get(node));
				nodeT.setHost(node.getHostName());
				nodeT.setNFFG("Nffg"+NFFG);
				nodeT.setFunctionalType(node.getFuncType().getName());
				//nodeT.setReachablehosts(serviceOf.createReachableHostsType()); //set an empty reachable hosts wrapper
				
				LinksType linksT=serviceOf.createLinksType();
				
				
				Set <LinkDescriptor> links = node.getLinks();
				
				if(!links.isEmpty()) {
				
					for(LinkDescriptor link:links) {
						
						LinkType linkT=serviceOf.createLinkType();
					
						linkT.setName(descriptor_name.get(link.getSourceNode())+"_"+descriptor_name.get(link.getDestinationNode()));
						linkT.setSourceNode(descriptor_name.get(link.getSourceNode()));
						linkT.setDestinationNode(descriptor_name.get(link.getDestinationNode()));
						linkT.setLatency(new BigInteger(String.valueOf(link.getLatency())));
						linkT.setThroughput(link.getThroughput());
						//linkT.setOverwrite(false);
						
						linksT.getLink().add(linkT);
						
						
					}
				}
				nodeT.setLinks(linksT); // empty or fulfilled object
				
				nffgToDeploy.getNode().add(nodeT);
			}
	}catch(Exception e) {
		logger.log(Level.SEVERE, "Client1: a problem has occured setting up the nffg to deploy \n");
		throw new ServiceException(e.getMessage());
	}
		
		Response response=target.path("nffgs").
				request(MediaType.APPLICATION_XML).post(Entity.entity(serviceOf.createNffg(nffgToDeploy), MediaType.APPLICATION_XML));
		try {
			if(response.getStatus()!=200) { //I need a response body in order to create the deployed nffg object
				
				if(response.getStatus()==409) {
					logger.log(Level.SEVERE,"Client1: Allocation exception deploying a nffg \n");
					throw new AllocationException();
				}
				else {
					logger.log(Level.SEVERE, "Client1: a problem has occured deploying an nffg \n");
					throw new ServiceException();
				}
			}else {
				logger.log(Level.INFO, "Client 1: the nffg is correctly deployed! \n");
				deployed=response.readEntity(NFFGtype.class);
				
			}
		}finally {
			response.close();
		}
		
		DeployedNffg depNffg=new DeployedNffgImpl(nfvSystem,target,serviceOf,deployed.getName());
		NFFG++;
		return depNffg;
	}
	
	/**
	 * Looks for an already deployed NF-FG by name and returns an object that can be used to interact with it
	 * @param	name the name of the NF-FG we are looking for
	 * @return	an object that implements the DeployedNffg interface and that allows interaction with the deployed NF-FG
	 * @throws UnknownEntityException	if the name passed as argument does not correspond to a deployed NF-FG
	 * @throws ServiceException if any other error occurred when trying to access the service
	 */

	@Override
	public DeployedNffg getDeployedNffg(String name) throws UnknownEntityException, ServiceException {
	
		NFFGtype nffg = null;
		
		try {
			nffg = target.path("nffgs").path(name)
			             .request(MediaType.APPLICATION_XML)
			             .get(NFFGtype.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client1: getDeployedNffg-> JAX-RS processing request error \n");
			throw new ServiceException();
		}
		catch (NotFoundException nf) {
			logger.log(Level.SEVERE,"Client1: getDeployedNffg-> service returned a NotFoundException \n");
			throw new UnknownEntityException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client1: getDeployedNffg-> service returned an error \n");
			throw new ServiceException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client1: DeployedNffg getNffg-> unexpected exception \n");
			throw new ServiceException();
		}
		
		
		return new DeployedNffgImpl(nfvSystem,target,serviceOf,name);
	}


}
