package br.com.ericsson.smartnotification.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.ericsson.smartnotification.domain.kafka.KafkaTopicObject;


public class Event extends KafkaTopicObject{

    private static final long serialVersionUID = 1L;

    private Integer eventType;
    
    private String description;
    
    private LocalDateTime dateTimeReceived;

    private List<EventField> fields;

    public Event(Integer eventType, String description, LocalDateTime dateTimeReceived) {
        this.eventType = eventType;
        this.description = description;
        this.dateTimeReceived = dateTimeReceived;
        this.fields = new ArrayList<>();
    }

    public Event(Integer eventType, String description) {
        this.eventType = eventType;
        this.description = description;
        this.dateTimeReceived = LocalDateTime.now();
        this.fields = new ArrayList<>();
    }

    public Event() {
        this.fields = new ArrayList<>();
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getDateTimeReceived() {
        return dateTimeReceived;
    }

    public void setDateTimeReceived(LocalDateTime dateTimeReceived) {
        this.dateTimeReceived = dateTimeReceived;
    }

    public List<EventField> getFields() {
        return fields;
    }

    public void setFields(List<EventField> fields) {
        this.fields = fields;
    }
    
    public EventField getField(String name) {
        for(EventField eventField : this.fields) {
            if(name.equals(eventField.getName())) {
                return eventField;
            }
        }
        return null;
    }

    public static Event buildEvent( Map<String, String> parameters) {
        Event event = new Event();
        for(Entry<String, String> entry : parameters.entrySet()) {
            event.getFields().add(new EventField(entry.getKey(), entry.getValue()));
        }
        return event;
    }
    
}
