package it.polito.dp2.NFV.sol3.service;


import javax.xml.bind.JAXBElement;



import it.polito.dp2.NFV.sol3.jaxb.ConnectionsType;
import it.polito.dp2.NFV.sol3.jaxb.PLPtype;


public interface ConnectionsInterface {
	
	
	public JAXBElement<ConnectionsType> getConnections();
	
	
	public JAXBElement<PLPtype> getConnection(String host1,String host2);

}
