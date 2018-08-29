package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.List;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicObjectQueue;

public class MessageEnrichedObject extends AgregationDocument implements KafkaTopicObjectQueue {

    private static final long serialVersionUID = 1L;
    
    private List<MessageEnriched> messagesEnriched;

    private ShippingRestriction shippingRestriction;

    private EventEnrichmentObject eventEnrichmentObject;

    private CampaignEnrichmentObject campaignEnrichmentObject;
    
    private int indexOfTheLastEnrichedMessageSent;
    
    private int numberOfMessageSubmissions;

    public MessageEnrichedObject() {
        super();
    }

    public MessageEnrichedObject(List<MessageEnriched> messagesEnriched, ShippingRestriction shippingRestriction,
            EventEnrichmentObject enrichmentTopicObject) {
        super();
        this.messagesEnriched = messagesEnriched;
        this.shippingRestriction = shippingRestriction;
        this.eventEnrichmentObject = enrichmentTopicObject;
    }

    public MessageEnrichedObject(List<MessageEnriched> messagesEnriched, ShippingRestriction shippingRestriction,
            CampaignEnrichmentObject enrichmentTopicObject) {
        super();
        this.messagesEnriched = messagesEnriched;
        this.shippingRestriction = shippingRestriction;
        this.campaignEnrichmentObject = enrichmentTopicObject;
    }

    public MessageEnrichedObject(List<MessageEnriched> messagesEnriched, ShippingRestriction shippingRestriction,
            EnrichmentObject enrichmentTopicObject) {
        super();
        this.messagesEnriched = messagesEnriched;
        this.shippingRestriction = shippingRestriction;
        this.campaignEnrichmentObject = enrichmentTopicObject instanceof CampaignEnrichmentObject
                ? (CampaignEnrichmentObject) enrichmentTopicObject
                : null;
        this.eventEnrichmentObject = enrichmentTopicObject instanceof EventEnrichmentObject
                ? (EventEnrichmentObject) enrichmentTopicObject
                : null;
    }

    public MessageEnrichedObject(ShippingRestriction shippingRestriction) {
        super();
        this.shippingRestriction = shippingRestriction;
        this.messagesEnriched = new ArrayList<>();
    }

    public List<MessageEnriched> getMessagesEnriched() {
        return messagesEnriched;
    }

    public void setMessagesEnriched(List<MessageEnriched> messagesEnriched) {
        this.messagesEnriched = messagesEnriched;
    }

    public ShippingRestriction getShippingRestriction() {
        return shippingRestriction;
    }

    public void setShippingRestriction(ShippingRestriction shippingRestriction) {
        this.shippingRestriction = shippingRestriction;
    }

    public EventEnrichmentObject getEventEnrichmentObject() {
        return eventEnrichmentObject;
    }

    public void setEventEnrichmentObject(EventEnrichmentObject eventEnrichmentObject) {
        this.eventEnrichmentObject = eventEnrichmentObject;
    }

    public CampaignEnrichmentObject getCampaignEnrichmentObject() {
        return campaignEnrichmentObject;
    }

    public void setCampaignEnrichmentObject(CampaignEnrichmentObject campaignEnrichmentObject) {
        this.campaignEnrichmentObject = campaignEnrichmentObject;
    }

    public EnrichmentObject geEnrichmentObject() {
        return this.eventEnrichmentObject != null ? this.eventEnrichmentObject : this.campaignEnrichmentObject;
    }
    
    public int getIndexOfTheLastEnrichedMessageSent() {
        return indexOfTheLastEnrichedMessageSent;
    }

    public void setIndexOfTheLastEnrichedMessageSent(int indexOfTheLastEnrichedMessageSent) {
        this.indexOfTheLastEnrichedMessageSent = indexOfTheLastEnrichedMessageSent;
    }
    
    public int getNumberOfMessageSubmissions() {
        return numberOfMessageSubmissions;
    }

    public void setNumberOfMessageSubmissions(int numberOfMessageSubmissions) {
        this.numberOfMessageSubmissions = numberOfMessageSubmissions;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
    
    public String getMsisdn() {
        return this.eventEnrichmentObject != null ? this.eventEnrichmentObject.getEvent().getField(Constants.MSISDN).getValue()
                .toString() : this.campaignEnrichmentObject.getMsisdn();
    }
}
