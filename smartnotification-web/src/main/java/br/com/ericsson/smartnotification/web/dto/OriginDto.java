package br.com.ericsson.smartnotification.web.dto;

import br.com.ericsson.smartnotification.entities.Origin;

public class OriginDto extends AbstractDto<Origin, OriginDto>{

	private String name;
	
	public OriginDto() {
		super(null, true);
	}

	@Override
	protected Origin getDocument() {
		return new Origin();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
