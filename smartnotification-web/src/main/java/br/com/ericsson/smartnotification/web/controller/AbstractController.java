package br.com.ericsson.smartnotification.web.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.web.domain.FilterView;
import br.com.ericsson.smartnotification.web.domain.PageableView;

public abstract class AbstractController {
    
	private PageableView pageableView;
	
	private FilterView filterView;
	
	private String urlBaseController;
	
	public AbstractController(String urlBaseController) {
		this.pageableView = buildPageable();
		this.filterView = buildFilter();
		this.urlBaseController = urlBaseController;
	}
	
	@RequestMapping("/index")
	public String getPageIndex(Map<String, Object> model) throws ApplicationException {
		model.put("urlController", this.getUrlBaseController());
		model.put(PageableView.class.getSimpleName(), this.getPageableView());
		model.put(FilterView.class.getSimpleName(), this.getFilterView());
		return this.getPage(model);
	}
	
	@PostMapping(value = "/filter", produces = { MediaType.APPLICATION_JSON_VALUE })
	public abstract String changeFilter(@RequestBody FilterView filterView, BindingResult result, Locale currentLocale, Map<String, Object> model) throws ApplicationException;
	
	@PostMapping(value = "/changePage", produces = { MediaType.APPLICATION_JSON_VALUE })
	public abstract String changePage(@RequestBody PageableView pageableView, BindingResult result, Locale currentLocale, Map<String, Object> model) throws ApplicationException;
	
	public abstract String getPage(Map<String, Object> model) throws ApplicationException ;
	
	public abstract FilterView buildFilter();

	public abstract PageableView buildPageable();
	
	public PageableView getPageableView() {
		return pageableView;
	}
	
	public FilterView getFilterView() {
		return filterView;
	}

	public String getUrlBaseController() {
		return urlBaseController;
	}

	public String getUrlBaseView() {
		return "../page" + urlBaseController;
	}
	
	public void setPageableView(PageableView pageableView) {
		this.pageableView = pageableView;
	}

	public void setFilterView(FilterView filterView) {
		this.filterView = filterView;
	}

}
