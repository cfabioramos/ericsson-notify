package br.com.ericsson.smartnotification.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Origin extends AbstractDocument{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    @Indexed(unique=true)
	private String name;


    public Origin() {
	}
    
	public Origin(String name) {
		super();
		this.name = name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	

}
