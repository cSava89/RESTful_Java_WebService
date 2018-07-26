package it.polito.dp2.NFV.sol3.service;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import it.polito.dp2.NFV.sol3.service.AllocationException;
import it.polito.dp2.NFV.sol3.jaxb.ConnectionsType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.PLPtype;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/connections")
public class ConnectionsResource implements ConnectionsInterface {
	
	private BackendSERVICE backend;
	private Logger logger=Logger.getLogger(ConnectionsResource.class.getName());
	private ObjectFactory serviceOf;
	Map	<String,PLPtype> connectionsMap;
	
	public ConnectionsResource() throws AllocationException, InternalServerErrorException {
					
					try {
							
							backend=BackendSERVICE.getInstance();
						
						}catch(BadRequestException | ServerErrorException e) {
							
							logger.log(Level.SEVERE,"ConnectionsResource: something wrong in NfvDeployer backend initialization \n");
							throw new InternalServerErrorException();
						}catch(ClientErrorException c) {
							
							logger.log(Level.SEVERE,"ConnectionsResource: nffg0 deploy leads to an allocation problem \n");
							throw new AllocationException();
							
						}
						serviceOf=new ObjectFactory();
						connectionsMap=BackendDB.getConnectionsMAP();
	}
	
	
	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<ConnectionsType> getConnections() {
		ConnectionsType connections = null;
		try {
			connections=serviceOf.createConnectionsType();
			
			for(PLPtype plink:connectionsMap.values()) {
				connections.getPlink().add(plink);
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE,"ConnectionsResource, get connections: unexpected exception");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createConnections(connections);
	}
	@Path("/connection")
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 400, message = "Bad Request"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<PLPtype> getConnection(@QueryParam("host1")String host1,@QueryParam("host2") String host2) {
		
		PLPtype requestedPlink = null;
		try {
			if(host1==null || host2==null) {
				logger.log(Level.SEVERE,"ConnectionsResource, get connection: host1 or host2 or both are null");
				throw new BadRequestException();
			}
			
			String connectionKey=new String(host1+"_"+host2);
			
			requestedPlink = connectionsMap.get(connectionKey);
			
			if(requestedPlink==null) { 
				logger.log(Level.SEVERE,"ConnectionsResource, get connection: requested connection not found");
				throw new NotFoundException();
				
			}
		}catch(NotFoundException nfe) {
			throw nfe;
		}catch(BadRequestException bre) {
			throw bre;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"ConnectionsResource, get connection: unexpected exception");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createPlink(requestedPlink);
	}

}
