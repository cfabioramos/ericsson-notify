package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.ericsson.smartnotification.enums.TopicPriority;

@Document
public class TemplateMessage extends AbstractDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private TopicPriority priority;

    private List<Message> messages;

    private ShippingRestriction shippingRestriction;

    public TemplateMessage() {
        super();
    }

    public TemplateMessage(String id, boolean active, TopicPriority priority) {
        super(id, active);
        this.messages = new ArrayList<>();
        this.priority = priority;
    }

    public TemplateMessage(TopicPriority priority, List<Message> messages) {
        super();
        this.priority = priority;
        this.messages = messages;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public TopicPriority getPriority() {
        return priority;
    }

    public void setPriority(TopicPriority priority) {
        this.priority = priority;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ShippingRestriction getShippingRestriction() {
        return shippingRestriction;
    }

    public void setShippingRestriction(ShippingRestriction shippingRestriction) {
        this.shippingRestriction = shippingRestriction;
    }
    
}
