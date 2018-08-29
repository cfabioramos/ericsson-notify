package br.com.ericsson.smartnotification.enrichment.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentEventFields;

@Component
public class EnrichmentEventFieldsImpl implements EnrichmentEventFields{

    @Override
    public Map<String, String> enrich(String[] fields, Event event) throws ApplicationException {
        Map<String, String> map = new HashMap<>();
        
        for(EventField field : event.getFields()) {
            map.put(field.getName(), field.getValue().toString());
        }
        
        return map;
    }

}
