package br.com.ericsson.smartnotification.web.busines;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.Origin;
import br.com.ericsson.smartnotification.entities.Rule;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.EventDefinitionRepository;
import br.com.ericsson.smartnotification.repository.OriginRepository;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.web.domain.FilterOptionView;
import br.com.ericsson.smartnotification.web.domain.FilterView;
import br.com.ericsson.smartnotification.web.dto.EventDefinitionDto;

@Service
public class EventDefinitionBusines  extends Busines<EventDefinitionDto>{

    @Autowired
    private EventDefinitionRepository repository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private OriginRepository originRepository;
    
    @Override
    protected EventDefinitionDto getDto() {
        return new EventDefinitionDto();
    }
    
    @Override
    public void save(EventDefinitionDto dto) throws ApplicationException {
    	
    	String[] origins = dto.getOrigem().trim().split(",");
    	
    	for(String originName : origins) {
    		if(!StringUtils.isEmpty(originName)) {
    			Origin origin = originRepository.findByName(originName.trim());
    			if(origin == null) {
    				originRepository.save(new Origin(originName.trim()));
    			}
    		}
    	}
    	
    	super.save(dto);
    }
    
    public EventDefinitionDto getDto(Integer eventType) throws ApplicationException {
        Optional<EventDefinition> optional = repository.findByEventType(eventType);
        if(optional.isPresent()) {
            return new EventDefinitionDto().setValuesFromDto(optional.get());
        }
        throw new ApplicationException(String.format("EventDefinition s% com o type s% não existe no repositório", getDto().getDocumentName(), eventType));
    }
    
    
    public List<EventDefinitionDto> getListEventDefinitionWhithRulesNumber() throws ApplicationException{
        List<EventDefinitionDto> definitionDtos = this.list();
        for(EventDefinitionDto dto : definitionDtos) {
            List<Rule> rules = ruleRepository.findByEventType(dto.getEventType());
            dto.setNumberOfRules(rules.size());
        }
        return definitionDtos;
    }

    @Override
    public List<EventDefinitionDto> list(FilterView filterView, int start, int maxItens) throws ApplicationException{
    	Boolean activeField = null;
    	if(filterView != null) {
    		
    		for(FilterOptionView optionView : filterView.getOptions()) {
    			if(optionView.getField().equals("active") && optionView.isChecked() && !optionView.getOptions().isEmpty()) {
    				boolean active = optionView.getOptions().get(0).isChecked();
    				boolean notActive = optionView.getOptions().get(1).isChecked();
    				if((active && notActive) 
    						|| (!active && !notActive)) {
    					activeField = null;
    				}else {
    					activeField = optionView.getOptions().get(0).isChecked();
    				}
    				break;
    			}
    		}
    	}
    	
    	List<EventDefinition> definitions = this.getRepository().findByActiveEqualsOrActiveNotNull(activeField, repository.createPageRequest(start, maxItens));
    	
        return this.getDto().setValuesFromDtos(definitions, EventDefinitionDto.class);
    }
    
    @Override
    protected EventDefinitionRepository getRepository() {
        return repository;
    }

}
