package br.com.ericsson.smartnotification.web.domain;

import java.util.ArrayList;
import java.util.List;

public class FilterView {

	private List<FilterOptionView> options;

	public FilterView() {
		this.options = new ArrayList<>();
	}

	public FilterView(List<FilterOptionView> options) {
		super();
		this.options = options;
	}

	public List<FilterOptionView> getOptions() {
		return options;
	}

	public void setOptions(List<FilterOptionView> options) {
		this.options = options;
	}

}
