package br.com.ericsson.smartnotification.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.web.busines.OriginBusiness;
import br.com.ericsson.smartnotification.web.dto.OriginDto;

@RestController
@RequestMapping("/origin")
public class OriginController {

	@Autowired 
	private OriginBusiness originBusiness;
	
	@GetMapping(value = "/search")
	public @ResponseBody List<String> search(@RequestParam String term) throws ApplicationException {
		List<OriginDto> dtos = this.originBusiness.search(term);
		List<String> origins = new ArrayList<>();
		
		for(OriginDto dto : dtos) {
			origins.add(dto.getName());
		}
		
		return origins;
	}

}
