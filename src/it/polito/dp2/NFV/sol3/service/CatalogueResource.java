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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.polito.dp2.NFV.sol3.service.AllocationException;
import it.polito.dp2.NFV.sol3.jaxb.CatalogueType;

import it.polito.dp2.NFV.sol3.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;

@Path("/catalogue")
public class CatalogueResource implements CatalogueInterface {
	
	private BackendSERVICE backend;
	private Logger logger=Logger.getLogger(CatalogueResource.class.getName());
	private ObjectFactory serviceOf;
	private Map<String,VNFtype> vnfMap;
	
	public CatalogueResource() throws AllocationException,InternalServerErrorException {
		
		try {
			
			backend=BackendSERVICE.getInstance();
		
		}catch(BadRequestException | ServerErrorException e) {
			
			logger.log(Level.SEVERE,"CatalogueResource: something wrong in NfvDeployer backend initialization \n");
			throw new InternalServerErrorException();
		}catch(ClientErrorException c) {
			
			logger.log(Level.SEVERE,"CatalogueResource: nffg0 deploy leads to an allocation problem \n");
			throw new AllocationException();
			
		}
		
		serviceOf=new ObjectFactory();
		vnfMap=BackendDB.getVnfMAP();
	}
	@GET
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<CatalogueType> getCatalogue() {
		
		CatalogueType catalogue = null;
		
		try {
			catalogue=serviceOf.createCatalogueType();
			
			for(VNFtype vnf: vnfMap.values()) {
				catalogue.getVnf().add(vnf);
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE,"CatalogueResource, getCatalogue: unexpected exception");
			throw new InternalServerErrorException();
		}
		return serviceOf.createCatalogue(catalogue);
	}
	@Path("/{vnf}")
	@ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Override
	public JAXBElement<VNFtype> getVNF(@PathParam("vnf")String vnf) {
		VNFtype requestedVNF = null;
		try {
			requestedVNF=vnfMap.get(vnf);
			
			if(requestedVNF==null) {
				logger.log(Level.SEVERE,"CatalogueResource, getCatalogue: required vnf not found");
				throw new NotFoundException();
			}
		}catch(NotFoundException nf) {
			throw nf;
		}catch(Exception e) {
			logger.log(Level.SEVERE,"CatalogueResource, getVNF: unexpected exception");
			throw new InternalServerErrorException();
		}
		
		return serviceOf.createVnf(requestedVNF);
	}

}
