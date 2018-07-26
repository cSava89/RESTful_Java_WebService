package it.polito.dp2.NFV.sol3.client2;


import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.jaxb.VNFtype;



public class VNFTypeReaderImpl extends NamedEntityImpl implements VNFTypeReader {

	private VNFtype vnf;
	
	public VNFTypeReaderImpl(VNFtype vnf)  {
		super(vnf.getName());
		
		this.vnf=vnf;
		
		
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public  FunctionalType getFunctionalType() {
		
		return FunctionalType.valueOf(this.vnf.getType().name());
	}

	@Override
	public int getRequiredMemory() {
		
			return this.vnf.getAmountOfMemory().intValue();
		
	}

	@Override
	public int getRequiredStorage() {
		
			return vnf.getDiskStorage().intValue();
		
	}

}
