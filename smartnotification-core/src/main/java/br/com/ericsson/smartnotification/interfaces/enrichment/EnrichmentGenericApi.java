package br.com.ericsson.smartnotification.interfaces.enrichment;

import java.util.Map;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public interface EnrichmentGenericApi {

    public Map<String, String> enrich(String... param) throws ApplicationException;

}
