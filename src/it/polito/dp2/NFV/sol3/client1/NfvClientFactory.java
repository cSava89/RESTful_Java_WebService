package it.polito.dp2.NFV.sol3.client1;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NfvClientException;

public class NfvClientFactory extends it.polito.dp2.NFV.lab3.NfvClientFactory{

	@Override
	public NfvClient newNfvClient() throws NfvClientException {
		
		NfvClient nfvClient=null;
		Logger logger=Logger.getLogger(NfvClientFactory.class.getName());
		
		
			try {
				nfvClient=new NfvClientImpl();
			} catch (URISyntaxException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE,"The hardcoded service URL is not RFC 2396 compliant\n");
				System.exit(1);
			}
		
		return nfvClient;
	}

}
