package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CampaignRecipient extends AbstractDocument {

    private static final long serialVersionUID = 1L;

    private String campaignId;

    private String msisdn;

    private List<String> enrichmentValues;

    public CampaignRecipient() {
        super(null, null);
        this.enrichmentValues = new ArrayList<>();
    }
            
    public CampaignRecipient(String campaignId, String msisdn) {
        super(null, null);
        this.campaignId = campaignId;
        this.msisdn = msisdn;
        this.enrichmentValues = new ArrayList<>();
    }

    public CampaignRecipient(String campaignId, String msisdn, List<String> enrichmentValues) {
        super();
        this.campaignId = campaignId;
        this.msisdn = msisdn;
        this.enrichmentValues = enrichmentValues;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public List<String> getEnrichmentValues() {
        return enrichmentValues;
    }

    public void setEnrichmentValues(List<String> enrichmentValues) {
        this.enrichmentValues = enrichmentValues;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
