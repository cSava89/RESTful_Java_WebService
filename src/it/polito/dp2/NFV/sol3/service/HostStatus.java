package it.polito.dp2.NFV.sol3.service;



public class HostStatus {
	
	private String hostName;
	private int remainingVNF;
	private int	remainingMemory;
	private int  remainingStorage;
	
	protected HostStatus() {
		setRemainingVNF(0);
		setRemainingMemory(0);
		setRemainingStorage(0);
	}

	public int getRemainingVNF() {
		return remainingVNF;
	}

	public void setRemainingVNF(int remainingVNF) {
		this.remainingVNF = remainingVNF;
	}

	public int getRemainingMemory() {
		return remainingMemory;
	}

	public void setRemainingMemory(int remainingMemory) {
		this.remainingMemory = remainingMemory;
	}

	public int getRemainingStorage() {
		return remainingStorage;
	}

	public void setRemainingStorage(int remainingStorage) {
		this.remainingStorage = remainingStorage;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

}
