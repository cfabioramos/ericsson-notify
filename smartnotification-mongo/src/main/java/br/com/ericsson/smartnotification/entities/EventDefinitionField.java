package br.com.ericsson.smartnotification.entities;

import br.com.ericsson.smartnotification.enums.FieldType;

public class EventDefinitionField extends AgregationDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final EventDefinitionField EVENT_FIELD_NTYPE = new EventDefinitionField("nType", "Tipo de evento",
            FieldType.NUMBER, true, true);
    public static final EventDefinitionField EVENT_FIELD_MSISDN = new EventDefinitionField("msisdn",
            "Número do assinante", FieldType.NUMBER, true, true);

    private String name;

    private String description;

    private FieldType type;

    private boolean required;

    private Boolean fixed;

    public EventDefinitionField() {
        super();
    }

    public EventDefinitionField(String name, String description, FieldType type, boolean required) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
    }

    public EventDefinitionField(String name, String description, FieldType type, boolean required, boolean fixed) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
        this.fixed = fixed;
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isString() {
        return FieldType.STRING.equals(this.type);
    }

    public boolean isDate() {
        return FieldType.DATE.equals(this.type);
    }

    public boolean isDateTime() {
        return FieldType.DATETIME.equals(this.type);
    }

    public boolean isNumber() {
        return FieldType.NUMBER.equals(this.type);
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
}
