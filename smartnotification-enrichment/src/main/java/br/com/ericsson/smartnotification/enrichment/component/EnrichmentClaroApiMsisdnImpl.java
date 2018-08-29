package br.com.ericsson.smartnotification.enrichment.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentGenericApi;

@Component
public class EnrichmentClaroApiMsisdnImpl implements EnrichmentGenericApi{

    @Override
    public Map<String, String> enrich(String... msisdn) throws ApplicationException {
        Map<String, String> map = new HashMap<>();
        map.put("claro_api.name", "Ricard");
        map.put("claro_api.data_adesao", "2018-03-03");
        map.put("claro_api.url_info", "https://www.claro.com.br");
        return map;
    }

}
