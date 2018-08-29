package br.com.ericsson.smartnotification.web.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public class EventDefinitionDto extends AbstractDto<EventDefinition, EventDefinitionDto> {

	public static final String NAME_IN_VIEW = "event";
	
    private Integer eventType;
    
    private String name;
    
    private String description;
    
    @Transient
    private String origem;

    private List<String> origins;
    
    private List<EventDefinitionField> fields;

    @Transient
    private int numberOfRules;
    
    public EventDefinitionDto(boolean isDefault) {
        super(null, true);
        this.fields = new ArrayList<>();
        this.origins = new ArrayList<>();
        if(isDefault) {
            this.fields.add(EventDefinitionField.EVENT_FIELD_NTYPE);
            this.fields.add(EventDefinitionField.EVENT_FIELD_MSISDN);
        }
    }
    
    public EventDefinitionDto() {
        super(null, true);
        this.fields= new ArrayList<>(); 
        this.origins = new ArrayList<>();
    }

    public EventDefinitionDto(String id, Boolean active, Integer eventType, String name, String description, String origem, List<EventDefinitionField> fields) {
        super(id, active);
        this.eventType = eventType;
        this.name = name;
        this.description = description;
        this.fields = fields;
        this.origem = origem;
        this.origins = new ArrayList<>();
    }


    @Override
    protected EventDefinition getDocument() {
        return new EventDefinition();
    }


    public Integer getEventType() {
        return eventType;
    }


    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
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

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
    
    public List<String> getOrigins() {
		return origins;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}

	public EventDefinitionField getEventDefinitionField(String name) {
        for(EventDefinitionField field : this.fields) {
            if(name.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }
    
    
    public void setEventDefinitionField(EventDefinitionField eventField) {
        for(int x = 0 ; x < this.fields.size(); x++) {
            if(this.fields.get(x).equals(eventField)) {
                this.fields.remove(x);
                this.fields.add(x, eventField);
                return;
            }
        }
        this.fields.add(eventField);
    }

    public int getNumberOfRules() {
        return numberOfRules;
    }

    public void setNumberOfRules(int numberOfRules) {
        this.numberOfRules = numberOfRules;
    }  
    
    public String getNumberOfRulesFormated() {
        return numberOfRules < 10 ? "0"+numberOfRules : String.valueOf(numberOfRules);
    }
    
    @Override
    public EventDefinitionDto setValuesFromDto(EventDefinition document) throws ApplicationException {
    	EventDefinitionDto dto = super.setValuesFromDto(document);
    	
    	StringBuilder builder = new StringBuilder();
    	if(document.getOrigins() != null) {
    		for(int x =0 ; x < document.getOrigins().size(); x++) {
    			builder.append(document.getOrigins().get(x));
    			if(x < document.getOrigins().size() -1) {
    				builder.append(", ");
    			}
    		}
    	}
    	dto.setOrigem(builder.toString());
    	return dto;
    }
    
    
    @Override
    public EventDefinition setValuesFromDocument(EventDefinition document) throws ApplicationException {
    	EventDefinition eventDefinition = super.setValuesFromDocument(document);
    	eventDefinition.getOrigins().clear();
    	for(String origin : this.getOrigem().trim().split(",")) {
    		if(!origin.isEmpty()) {
    			eventDefinition.getOrigins().add(origin.trim());
    		}
    	}
    	return eventDefinition;
    }

}
