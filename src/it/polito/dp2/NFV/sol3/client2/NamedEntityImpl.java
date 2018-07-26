package it.polito.dp2.NFV.sol3.client2;

import it.polito.dp2.NFV.NamedEntityReader;


public class NamedEntityImpl implements NamedEntityReader{

	
	protected String name;

    public NamedEntityImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
