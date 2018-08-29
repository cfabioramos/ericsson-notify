package br.com.ericsson.smartnotification.enums;

public enum FieldType {
    STRING, NUMBER, DATE, DATETIME;
    
    public static FieldType get(String type) {
        for(FieldType field : values()) {
            if(field.name().equals(type)) {
                return field;
            }
        }
        return null;
    }
}
