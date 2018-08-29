package br.com.ericsson.smartnotification.rulesengine.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;
import br.com.ericsson.smartnotification.rulesengine.business.RulesEngineBusiness;

@Component
public class OptOutComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEngineBusiness.class);
    
    @Autowired
    private OptOutRepository optOutRepository;
    
    public boolean isOptOut(Event event) {
        EventField msFieldField = event.getField(EventDefinitionField.EVENT_FIELD_MSISDN.getName());
        return this.isOptOut(msFieldField.getValue().toString());
    }
    
    public boolean isOptOut(String msisdn) {
        LOG.info("Verificando OptOut para msisdn {}", msisdn);
        return this.optOutRepository.hasOptedOut(msisdn);
    }
    
}
