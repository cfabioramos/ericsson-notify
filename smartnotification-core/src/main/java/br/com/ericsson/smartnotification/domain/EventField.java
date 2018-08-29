package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;

import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.utils.JsonUtil;

public class EventField implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private FieldType type;

    private Boolean required;

    private String value;

    public EventField() {
    }

    public EventField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public EventField(String name, String description, FieldType type, boolean required, String value) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.value = value;
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

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public Boolean isRequired() {
        return required != null ? required : Boolean.FALSE;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JsonUtil.parseToJsonString(this);
    }

}
