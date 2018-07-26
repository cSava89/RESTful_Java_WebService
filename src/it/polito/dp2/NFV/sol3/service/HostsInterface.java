package it.polito.dp2.NFV.sol3.service;


import javax.xml.bind.JAXBElement;

import it.polito.dp2.NFV.sol3.jaxb.HostType;
import it.polito.dp2.NFV.sol3.jaxb.HostsType;


public interface HostsInterface {
	
	
	public JAXBElement<HostsType> getHosts();
	
	
	public JAXBElement<HostType> getHost( String hostName);
	

}
