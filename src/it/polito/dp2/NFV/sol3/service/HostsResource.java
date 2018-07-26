package it.polito.dp2.NFV.sol3.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import it.polito.dp2.NFV.sol3.service.AllocationException;
import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.HostsType;
import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.VirtualNodeType;
import it.polito.dp2.NFV.sol3.jaxb.VirtualizedNodesType;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/hosts")
public class HostsResource implements HostsInterface {
	
	private BackendSERVICE backend;
	private Logger logger=Logger.getLogger(HostsResource.class.getName());
	private Map<String,HostType> hostMap;
	private Map<String,List<VirtualNodeType>>  nodes;
	private ObjectFactory serviceOf;
	
	public HostsResource() throws AllocationException,InternalServerErrorException {
		
		try {
			
			backend=BackendSERVICE.getInstance();
		
		}catch(BadRequestException | ServerErrorException e) {
			
			logger.log(Level.SEVERE,"HostsResource: something wrong in NfvDeployer backend initialization \n");
			throw new InternalServerErrorException();
		}catch(ClientErrorException c) {
			
			logger.log(Level.SEVERE,"HostsResource: nffg0 deploy leads to an allocation problem \n");
			throw new AllocationException();
			
		}
		
		hostMap=BackendDB.getHostMAP();
		nodes=BackendDB.getVirtualizedNodesOnHostMAP();
		serviceOf=new ObjectFactory();
	}
	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<HostsType> getHosts() {
		
		HostsType hosts = null;
		
		try {
			hosts=serviceOf.createHostsType();
			
			for(HostType h:hostMap.values()) {
				hosts.getHost().add(h);
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE,"HostsResource, get hosts: unexpected exception");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createHosts(hosts);
	}
	@Path("/{host}")
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<HostType> getHost(@PathParam("host")String hostName) {
		
		HostType requiredHost=null;
		try {
			requiredHost=hostMap.get(hostName);
			
			if(requiredHost==null) {
				logger.log(Level.SEVERE,"HostsResource, get host: provided host not found");
				throw new NotFoundException();
				
			}
			List<VirtualNodeType> vNodes=nodes.get(hostName);
			
			
			if(!vNodes.isEmpty()) {
				
				VirtualizedNodesType vn=serviceOf.createVirtualizedNodesType();
				
				for( VirtualNodeType vNode: vNodes) {
					vn.getVirtualizednode().add(vNode);
				}
				
				requiredHost.setVirtualizednodes(vn);
			}
		}catch(NotFoundException nf) {
			throw nf;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"HostsResource, get host: unexpected exception");
			throw new InternalServerErrorException();
		}
		
		
		
		return serviceOf.createHost(requiredHost);
	}

}
