package it.polito.dp2.NFV.sol3.client1.NfvReader;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.sol3.jaxb.PLPtype;



public class ConnectionPerformanceReaderImpl implements ConnectionPerformanceReader {

	private int latency;
	private float throughput;
	
	public ConnectionPerformanceReaderImpl(PLPtype plp) {
		this.latency=plp.getLatency().intValue();
		this.throughput=plp.getThroughput();
		
	}

	@Override
	public int getLatency() {
		
			return this.latency;
		
	}

	@Override
	public float getThroughput() {
		
			return this.throughput;
		
	}

}
