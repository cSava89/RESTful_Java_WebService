package it.polito.dp2.NFV.sol3.service;


import javax.xml.bind.JAXBElement;

import it.polito.dp2.NFV.sol3.jaxb.CatalogueType;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;


public interface CatalogueInterface {
	
	
	public JAXBElement<CatalogueType> getCatalogue();
	
	
	public JAXBElement<VNFtype> getVNF(String vnf);

}
