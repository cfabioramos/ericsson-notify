package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EventDefinition extends AbstractDocument{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Indexed(unique=true)
    private Integer eventType;
    
    private String name;
    
    private String description;

    private List<String> origins;
    
    private List<EventDefinitionField> fields;
       
    public EventDefinition() {
        super();
        this.fields = new ArrayList<>();
        this.origins = new ArrayList<>();
    }
    
    public EventDefinition(String id, String name, Integer eventType, String description, boolean active) {
        super(id, active);
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.origins = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    public void addField(EventDefinitionField eventField) {
        fields.add(eventField);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    public List<EventDefinitionField> getFields() {
        return fields;
    }

    public void setFields(List<EventDefinitionField> fields) {
        this.fields = fields;
    }

    public EventDefinitionField getField(String name) {
        for(EventDefinitionField event : this.getFields()) {
            if(name.equals(event.getName())) {
                return event;
            }
        }
        return null;
    }
    

    public List<String> getOrigins() {
		return origins;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}

	@Transient
    public List<EventDefinitionField> getFieldsRequired() {
        List<EventDefinitionField> eventFieldRequired = new ArrayList<>();
        for(EventDefinitionField event : this.getFields()) {
            if(event.isRequired()) {
                eventFieldRequired.add(event);
            }
        }
        return eventFieldRequired;
    }
    
    public static final EventDefinitionField[] getEventFieldRequiredDefault() {
        return  new EventDefinitionField[]{
            EventDefinitionField.EVENT_FIELD_NTYPE,
            EventDefinitionField.EVENT_FIELD_MSISDN
        };
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
    
}