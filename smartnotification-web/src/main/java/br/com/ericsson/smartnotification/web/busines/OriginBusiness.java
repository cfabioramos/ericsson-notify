package br.com.ericsson.smartnotification.web.busines;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.OriginRepository;
import br.com.ericsson.smartnotification.web.domain.FilterView;
import br.com.ericsson.smartnotification.web.dto.OriginDto;

@Service
public class OriginBusiness extends Busines<OriginDto>{

	@Autowired
	private OriginRepository repository;
	
	@Override
	public List<OriginDto> list(FilterView filterView, int startPage, int maxItens) throws ApplicationException {
		//TODO 
		throw new ApplicationException(""); 
	}
	
	public List<OriginDto> search(String name) throws ApplicationException {
		return this.getDto().setValuesFromDtos(this.repository.findByNameLike(name), OriginDto.class) ;
	}

	@Override
	protected OriginDto getDto() {
		return new OriginDto();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MongoRepository getRepository() {
		return this.repository;
	}

}
