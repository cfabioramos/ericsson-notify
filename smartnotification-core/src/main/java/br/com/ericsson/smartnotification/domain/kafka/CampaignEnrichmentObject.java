package br.com.ericsson.smartnotification.domain.kafka;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.ericsson.smartnotification.enums.TopicPriority;

public class CampaignEnrichmentObject extends EnrichmentObject {

    private static final long serialVersionUID = 1L;

    private String msisdn;

    private String campaignId;

    private LocalDateTime dateTimeProcessCampaign;

    private List<String> enrichmentValues;

    public CampaignEnrichmentObject(String campaignId, TopicPriority priority, String msisdn, String template,
            LocalDateTime dateTimeProcessCampaign, List<String> enrichmentValues) {
        super(priority, template);
        this.campaignId = campaignId;
        this.msisdn = msisdn;
        this.dateTimeProcessCampaign = dateTimeProcessCampaign;
        this.enrichmentValues = enrichmentValues;
    }

    public CampaignEnrichmentObject() {
        super(null, null);
        this.enrichmentValues = new ArrayList<>();
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public LocalDateTime getDateTimeProcessCampaign() {
        return dateTimeProcessCampaign;
    }

    public void setDateTimeProcessCampaign(LocalDateTime dateTimeProcessCampaign) {
        this.dateTimeProcessCampaign = dateTimeProcessCampaign;
    }

    @Override
    public LocalDateTime getDateTimeReceived() {
        return this.dateTimeProcessCampaign;
    }

    public List<String> getEnrichmentValues() {
        return enrichmentValues;
    }

    public void setEnrichmentValues(List<String> enrichmentValues) {
        this.enrichmentValues = enrichmentValues;
    }

}
