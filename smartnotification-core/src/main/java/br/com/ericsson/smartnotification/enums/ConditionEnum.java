package br.com.ericsson.smartnotification.enums;

import java.util.ArrayList;
import java.util.List;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public enum ConditionEnum {
    EQUALS("Igual a", FieldType.values()),
    NOT_EQUALS("Diferente de", FieldType.values()),
    GREATER_THAN("maior que", new FieldType[]{FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE}),
    LESS_THAN("menor que", new FieldType[]{FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE}),
    BETWEEN("entre", new FieldType[]{FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE}),
    GREATER_AND("maior e igual", new FieldType[]{FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE}),
    LESS_AND("menor e igual", new FieldType[]{FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE}),
    ;
    
    private String name;
    
    private FieldType[] fieldTypes;

    private ConditionEnum(String name, FieldType[] fieldTypes) {
        this.name= name;
        this.fieldTypes = fieldTypes;
    }
    
    public String getName() {
        return name;
    }

    public FieldType[] getFieldTypes() {
        return fieldTypes;
    }
    
    public static List<ConditionEnum> getConditionEnum(FieldType fieldType) {
        List<ConditionEnum> conditionEnums = new ArrayList<>();
        for(ConditionEnum c : values()) {
            for(FieldType type : c.fieldTypes) {
                if(type.equals(fieldType)) {
                    conditionEnums.add(c);
                }
            }
        }
        return conditionEnums;
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static ConditionEnum getConditionByString(String condition) throws ApplicationException {
        for(ConditionEnum conditionEnum : values()) {
            if(conditionEnum.name().equalsIgnoreCase(condition) || conditionEnum.getName().equalsIgnoreCase(condition)) {
                return conditionEnum;
            }
        }
        throw new ApplicationException(String.format("Condição não encontrada : %s.", condition));
    }
    
}
