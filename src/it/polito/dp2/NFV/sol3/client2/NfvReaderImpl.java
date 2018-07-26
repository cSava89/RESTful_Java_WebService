package it.polito.dp2.NFV.sol3.client2;


import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.jaxb.*;

public class NfvReaderImpl implements NfvReader {
	
	private static NfvSystemType nfvSystemType;
	
	
	private Set<NffgReader> nffgs;
	private Set<HostReader> hosts;
	private  Map<String,ConnectionPerformanceReader> performances;
	private Set<VNFTypeReader> catalogue;
	
	public NfvReaderImpl(NfvSystemType nfvSystemType)  {
		NfvReaderImpl.nfvSystemType=nfvSystemType;
		this.nffgs=new HashSet<>();
		this.hosts=new HashSet<>();
		this.performances=new HashMap<>();
		this.catalogue=new HashSet<>();
		
		
		for(NFFGtype nf:nfvSystemType.getNffgs().getNffg()) {
			
			NffgReader nffgReader=new NffgReaderImpl(nf);
			nffgs.add(nffgReader);
			}
		
		
		for(HostType h:nfvSystemType.getHosts().getHost()) {
			
			HostReader hostReader=new HostReaderImpl(h);
			hosts.add( hostReader);
		}
		
		String performanceKey=null;
		
		Set<PLPtype> Typeperformances=new HashSet<>(nfvSystemType.getConnections().getPlink());
		if(!Typeperformances.isEmpty()) {//possible null
			for(PLPtype plp:Typeperformances) {
				performanceKey=new String(plp.getHost1()+"_"+plp.getHost2());
				ConnectionPerformanceReader cpr=new ConnectionPerformanceReaderImpl(plp);
				performances.put(performanceKey, cpr);
			}
		}
	
		for(VNFtype vnf:nfvSystemType.getCatalogue().getVnf()) {
	
			VNFTypeReader vnfReader=new VNFTypeReaderImpl(vnf);
			catalogue.add(vnfReader);
			
		}
		
		
		
		
		
		
	}
	
	
	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader h1, HostReader h2) {
		String performanceKey=null;
		performanceKey=new String(h1.getName()+"_"+h2.getName());
		
		if(this.performances.containsKey(performanceKey)) {
				
			return this.performances.get(performanceKey);
			
			
		}
			
		return null;
	}
	@Override
	public HostReader getHost(String host) {
		for(HostReader h:hosts) {
			if(h.getName().equals(host))
				return h;
		}
		return null;
	}
	@Override
	public Set<HostReader> getHosts() {
		
			return hosts;
		
	}
	@Override
	public NffgReader getNffg(String nffg) {
		for(NffgReader n:nffgs) {
			if(n.getName().equals(nffg))
				return n;
		}
		return null;
	}
	@Override
	public Set<NffgReader> getNffgs(Calendar date) {
		if(date!=null) {
			HashSet<NffgReader> nffgSince=new HashSet<>();
			for(NffgReader n:nffgs) {
				if(n.getDeployTime().after(date) || n.getDeployTime().equals(date))
					nffgSince.add(n);
				
				} 
			if(!nffgSince.isEmpty())
				return nffgSince;
			}
			else if(date==null && this.nffgs!=null){
				 return nffgs;
				
			}
			
			
			return null;
	}
	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return catalogue;
	}
	
	protected static NFFGtype getNffgByName(String nffgName) {
		for(NFFGtype nffg:nfvSystemType.getNffgs().getNffg()) {
			if(nffgName.equals(nffg.getName()))
				return  nffg;
		}
		return null;
			
		
		
	}

	protected static HostType getHostByName(String host) {
		for(HostType h:nfvSystemType.getHosts().getHost()) {
			if(host.equals(h.getName()))
				return  h;
		}
		return null;
	}

	protected static VNFtype getVNFbyName(String functionalType) {
		for(VNFtype vnf:nfvSystemType.getCatalogue().getVnf()) {
			if(functionalType.equals(vnf.getName()))
				return  vnf;
		}
		return null;
	}

	

	

	

}
