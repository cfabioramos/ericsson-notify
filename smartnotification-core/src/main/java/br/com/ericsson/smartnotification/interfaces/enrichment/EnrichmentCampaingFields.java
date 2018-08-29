package br.com.ericsson.smartnotification.interfaces.enrichment;

import java.util.Map;

import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public interface EnrichmentCampaingFields {

    public Map<String, String> enrich(String[] fields, CampaignEnrichmentObject campaignEnrichmentObject) throws ApplicationException;

}
