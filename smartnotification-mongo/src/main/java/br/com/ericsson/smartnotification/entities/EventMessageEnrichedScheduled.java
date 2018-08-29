package br.com.ericsson.smartnotification.entities;

import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class EventMessageEnrichedScheduled extends AbstractDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private MessageEnrichedObject eventMessageEnriched;

    public EventMessageEnrichedScheduled() {
        super();
    }

    public EventMessageEnrichedScheduled(MessageEnrichedObject eventMessageEnriched) {
        super();
        this.eventMessageEnriched = eventMessageEnriched;
    }

    public MessageEnrichedObject getEventMessageEnriched() {
        return eventMessageEnriched;
    }

    public void setEventMessageEnriched(MessageEnrichedObject eventMessageEnriched) {
        this.eventMessageEnriched = eventMessageEnriched;
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
