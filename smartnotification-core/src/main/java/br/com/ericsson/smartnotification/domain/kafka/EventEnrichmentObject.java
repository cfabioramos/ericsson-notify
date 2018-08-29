package br.com.ericsson.smartnotification.domain.kafka;

import java.time.LocalDateTime;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.enums.TopicPriority;

public class EventEnrichmentObject extends EnrichmentObject {

    private static final long serialVersionUID = 1L;

    private Event event;

    public EventEnrichmentObject() {
        super(null, null, null);
    }

    public EventEnrichmentObject(TopicPriority priority, Event event, String templateId) {
        super(priority, null, templateId);
        this.event = event;
    }

    public EventEnrichmentObject(TopicPriority priority, Event event, String ruleId, String templateId) {
        super(priority, ruleId, templateId);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public LocalDateTime getDateTimeReceived() {
        return event.getDateTimeReceived();
    }

}
