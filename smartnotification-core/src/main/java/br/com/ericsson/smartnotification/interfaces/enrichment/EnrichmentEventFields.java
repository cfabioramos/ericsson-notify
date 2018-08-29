package br.com.ericsson.smartnotification.interfaces.enrichment;

import java.util.Map;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public interface EnrichmentEventFields {

    public Map<String, String> enrich(String[] fields, Event event) throws ApplicationException;

}
