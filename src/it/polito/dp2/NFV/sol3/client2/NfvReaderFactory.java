package it.polito.dp2.NFV.sol3.client2;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.sol3.client1.NfvReader.NfvReaderImpl;
import it.polito.dp2.NFV.sol3.jaxb.CatalogueType;
import it.polito.dp2.NFV.sol3.jaxb.ConnectionsType;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.HostsType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGsType;
import it.polito.dp2.NFV.sol3.jaxb.NFFGtype;
import it.polito.dp2.NFV.sol3.jaxb.NfvSystemType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory
{
	private String targetUrl;
	private ObjectFactory serviceOf;
	private Logger logger=Logger.getLogger(NfvReaderFactory.class.getName());
	private WebTarget target;
	private NfvSystemType nfvSystem;
	
	@Override
	public NfvReader newNfvReader() throws NfvReaderException
	{
		
		targetUrl=System.getProperty("it.polito.dp2.NFV.lab3.URL");
		
		if(targetUrl == null)
			targetUrl="http://localhost:8080/NfvDeployer/rest/";
		
		
		serviceOf=new ObjectFactory();
		
		target=ClientBuilder.newClient().target(targetUrl); //I use the garbage collector to close the client since there are several
		//interaction with service
		
		
		
		nfvSystem=serviceOf.createNfvSystemType();
		
			nfvSystem.setCatalogue(getCatalogue());
			nfvSystem.setConnections(getConnections());
			nfvSystem.setHosts(getHosts());
			nfvSystem.setNffgs(getNffgs());
			
			
			return new NfvReaderImpl(nfvSystem);
	}
	
	private CatalogueType getCatalogue() throws NfvReaderException
	{
		
		CatalogueType catalogue;
		
		try {
			catalogue = target.path("catalogue")
			                .request(MediaType.APPLICATION_XML)
			                .get(CatalogueType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client2: getCatalogue-> JAX-RS request processing error \n");
			throw new NfvReaderException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client2: getCatalogue-> service returned an error \n");
			throw new NfvReaderException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client2: getCatalogue-> unexpected exception \n");
			throw new NfvReaderException();
		}
		
		return catalogue;
	}
	
	
	private ConnectionsType getConnections() throws NfvReaderException
	{
		// Call NfvDeployer REST Web Service
		ConnectionsType connections;
		
		try {
			connections = target.path("connections")
			                    .request(MediaType.APPLICATION_XML)
			                    .get(ConnectionsType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client2: getConnections-> JAX-RS request processing error \n");
			throw new NfvReaderException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client2: getConnections-> DIOOO FAAAA \n");
			throw new NfvReaderException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client2: getConnections-> unexpected exception \n");
			throw new NfvReaderException();
		}
		
		return connections;
	}
	
private HostsType getHosts() throws NfvReaderException{
		
		HostsType hosts;
		
		try {
			hosts = target.path("hosts")
			              .request(MediaType.APPLICATION_XML)
			              .get(HostsType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client2: getHosts-> JAX-RS request processing error \n");
			throw new NfvReaderException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client2: getHosts-> service returned an error \n");
			throw new NfvReaderException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client2: getHosts-> unexpected exception \n");
			throw new NfvReaderException();
		}
		
		// Create a complete host set (including nodeRefs)
		HostsType completeHosts = serviceOf.createHostsType();
		
		for (HostType host: hosts.getHost())
		{
			HostType newHost = getHost( host.getName() );
			completeHosts.getHost().add(newHost);
		}
		
		return completeHosts;
	}
	
	private HostType getHost(String hostName) throws NfvReaderException{
		
		HostType host;
		
		try {
			host = target.path("hosts").path(hostName)
			             .request(MediaType.APPLICATION_XML)
			             .get(HostType.class);
		}
		catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client2: getHost-> JAX-RS request processing error \n");
			throw new NfvReaderException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client2: getHost-> server returned an exception \n");
			throw new NfvReaderException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client2: getHost-> unexpected exception \n");
			throw new NfvReaderException();
		}
		
		return host;
	}
	
	private NFFGsType getNffgs() throws NfvReaderException{
		
		NFFGsType nffgs;
		
		try {
			nffgs = target.path("nffgs").request(MediaType.APPLICATION_XML)
					.get(NFFGsType.class);
		}catch (ProcessingException pe) {
			logger.log(Level.SEVERE,"Client2: DeployedNffg getNffgs-> JAX-RS request processing error \n");
			throw new NfvReaderException();
		}
		catch (WebApplicationException wae) {
			logger.log(Level.SEVERE,"Client2: DeployedNffg getNffgs-> server returned an exception \n");
			throw new NfvReaderException();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE,"Client2: DeployedNffgs getNffgs-> unexpected exception \n");
			throw new NfvReaderException();
		}
		
		NFFGsType completeNffgs=serviceOf.createNFFGsType();
		
		for(NFFGtype nffg:nffgs.getNffg()) {
			
			NFFGtype newNffg=getNffg(nffg.getName());
			completeNffgs.getNffg().add(newNffg);			
		}
		
		return completeNffgs;
		
	}

	private NFFGtype getNffg(String name) throws NfvReaderException {
		
				NFFGtype nffg;
				
				try {
					nffg = target.path("nffgs").path(name)
					             .request(MediaType.APPLICATION_XML)
					             .get(NFFGtype.class);
				}
				catch (ProcessingException pe) {
					logger.log(Level.SEVERE,"Client2: DeployedNffg getNffg-> JAX-RS processing request error \n");
					throw new NfvReaderException();
				}
				catch (WebApplicationException wae) {
					logger.log(Level.SEVERE,"Client2: DeployedNffg getNffg-> service returned an error \n");
					throw new NfvReaderException();
				}
				catch (Exception e) {
					logger.log(Level.SEVERE,"Client2: DeployedNffg getNffg-> unexpected exception \n");
					throw new NfvReaderException();
				}
				
				return nffg;
			
	}
	


}
