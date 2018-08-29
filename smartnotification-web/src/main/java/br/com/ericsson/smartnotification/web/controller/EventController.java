package br.com.ericsson.smartnotification.web.controller;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.web.busines.EventDefinitionBusines;
import br.com.ericsson.smartnotification.web.busines.OriginBusiness;
import br.com.ericsson.smartnotification.web.domain.FilterOptionView;
import br.com.ericsson.smartnotification.web.domain.FilterView;
import br.com.ericsson.smartnotification.web.domain.PageableView;
import br.com.ericsson.smartnotification.web.dto.EventDefinitionDto;
import br.com.ericsson.smartnotification.web.util.ConstantsWeb;

@Controller
@RequestMapping("/modulo/event")
public class EventController extends AbstractController {

	@Autowired
	private EventDefinitionBusines busines;
	
	@Autowired 
	private OriginBusiness originBusiness;

	public EventController() {
		super("/modulo/event");
	}

	@Override
	public String getPage(Map<String, Object> model) throws ApplicationException {
		this.getPageableView().setTotalItens((int)busines.total());
		model.put(ConstantsWeb.EVENTS_DEFINITIONS, busines.list(this.getFilterView(), getPageableView().getCurrent() - 1, this.getPageableView().getTotalItemsPerPage()));
		model.put(ConstantsWeb.ORIGINS, originBusiness.list());
		return getUrlBaseView().concat("/index");
	}
	
	@Override
	public String changeFilter(@RequestBody FilterView filterView, BindingResult result, Locale currentLocale, Map<String, Object> model) throws ApplicationException {
		this.setFilterView(filterView);
		getPageIndex(model);
		return this.getListView();
	}
	
	@Override
	public String changePage(@RequestBody PageableView pageableView, BindingResult result, Locale currentLocale, Map<String, Object> model) throws ApplicationException {
		if(getPageableView().getTotalItens() > (getPageableView().getTotalItemsPerPage() * getPageableView().getCurrent()) || getPageableView().getCurrent() == 1) {
			this.setPageableView(pageableView);
		}else {
			pageableView.setCurrent(pageableView.getCurrent() -1);
			this.setPageableView(pageableView);
		}
		getPageIndex(model);
		return this.getListView();
	}
	
	@Override
	public FilterView buildFilter() {
		FilterView filter  = new FilterView();
		FilterOptionView status = new FilterOptionView("Status", "active", false);
		status.getOptions().add(new FilterOptionView("Ativo", status.getField(), false));
		status.getOptions().add(new FilterOptionView("Inativo", status.getField(), false));
		filter.getOptions().add(status);
		return filter;
	}
	
	@Override
	public PageableView buildPageable() {
		return new PageableView();
	}
	
	@PostMapping(value = "/{eventId}")
	public String getPartialEvent(@PathVariable("eventId") String eventId, Map<String, Object> model) throws ApplicationException {
		model.put(EventDefinitionDto.NAME_IN_VIEW, busines.getDto(eventId));
		model.put("colapsedOpen", true);
		return this.getUrlBaseView().concat("/partials/form_event_in_list");
	}
	
	@PostMapping(value = "/save", produces = { MediaType.APPLICATION_JSON_VALUE })
	public String getPageSave(@RequestBody EventDefinitionDto dto, BindingResult result, Locale currentLocale, Map<String, Object> model)
			throws ApplicationException {
		
		dto.getFields().add(EventDefinitionField.EVENT_FIELD_MSISDN);
		busines.save(dto);
		getPageIndex(model);
		model.put("showDialogEventSave", true);
		return this.getListView();
	}
	
	@PostMapping(value = "/update", produces = { MediaType.APPLICATION_JSON_VALUE })
	public String getPageUpdate(@RequestBody EventDefinitionDto dto, BindingResult result, Locale currentLocale, Map<String, Object> model)
			throws ApplicationException {
		busines.save(dto);
		model.put("event", dto);
		model.put("showDialogUpdateEvent", true);
		model.put("colapsedOpen", true);
		return this.getUrlBaseView().concat("/partials/form_event_in_list");
	}

	@PostMapping(value = "/new")
	public String getPageNew(Map<String, Object> model) throws ApplicationException {
		return getPageEdit(null, model);
	}

	@PostMapping(value = "/edit/{eventId}")
	public String getPageEdit(@PathVariable("eventId") String eventType, Map<String, Object> model)
			throws ApplicationException {
		if (StringUtils.isEmpty(eventType)) {
			model.put(EventDefinitionDto.NAME_IN_VIEW, new EventDefinitionDto());
		} else {
			model.put(EventDefinitionDto.NAME_IN_VIEW, busines.getDto(eventType));
		}
		return this.getUrlBaseView().concat("/modal/edit_event_form");
	}

	@PostMapping(value = "/field/new")
	public String getPageEdit(Map<String, Object> model) {
		model.put("field", new EventDefinitionField());
		return this.getUrlBaseView().concat("/partials/edit_field_event");
	}


	private String getListView() {
		return getUrlBaseView().concat("/partials/list-events");
	}



}
