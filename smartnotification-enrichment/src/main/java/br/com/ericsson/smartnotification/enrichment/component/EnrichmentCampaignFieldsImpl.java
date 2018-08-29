package br.com.ericsson.smartnotification.enrichment.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentCampaingFields;

@Component
public class EnrichmentCampaignFieldsImpl implements EnrichmentCampaingFields {

    @Override
    public Map<String, String> enrich(String[] fields, CampaignEnrichmentObject campaignEnrichmentObject)
            throws ApplicationException {
        try {
            Map<String, String> map = new HashMap<>();
            for (int x = 0; x < fields.length; x++) {
                map.put(fields[x], campaignEnrichmentObject.getEnrichmentValues().get(x));
            }
            return map;

        } catch (IndexOutOfBoundsException e) {
            throw new ApplicationException(String.format(
                    "Existem campos no template %s que não foram definidos para enriquecimento na campanha %s - campos do template %s : campos informados no enriquecimento da campanha %s",
                    campaignEnrichmentObject.getTemplateId(), campaignEnrichmentObject.getCampaignId(),
                    Arrays.asList(fields), campaignEnrichmentObject.getEnrichmentValues()));
        }
    }

}
