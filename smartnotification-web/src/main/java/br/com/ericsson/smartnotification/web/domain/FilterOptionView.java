package br.com.ericsson.smartnotification.web.domain;

import java.util.ArrayList;
import java.util.List;

public class FilterOptionView {
    
	private String name;

	private String field;
	
	private boolean checked;

	private List<FilterOptionView> options;
	
	public FilterOptionView() {
		this.options = new ArrayList<>();
		
	}
	
	public FilterOptionView(String name, String field, boolean checked, List<FilterOptionView> options) {
		super();
		this.name = name;
		this.field = field;
		this.checked = checked;
		this.options = options;
	}
	
	public FilterOptionView(String name, String field, boolean checked) {
		super();
		this.name = name;
		this.field = field;
		this.checked = checked;
		this.options = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<FilterOptionView> getOptions() {
		return options;
	}

	public void setOptions(List<FilterOptionView> options) {
		this.options = options;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
    
}
