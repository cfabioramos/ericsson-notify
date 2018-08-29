package br.com.ericsson.smartnotification.web.validation;

import br.com.ericsson.smartnotification.entities.Condition;
import br.com.ericsson.smartnotification.enums.ConditionEnum;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public final class EventDefinitionValidation {

    private EventDefinitionValidation() {
    }
    
    public static void validationConditionRule(Condition condition, FieldType type) throws ApplicationException {
        
        if(ConditionEnum.BETWEEN.equals(condition.getClause())) {
            if((type.equals(FieldType.DATE) || type.equals(FieldType.DATETIME)) && condition.getDateStart().after(condition.getDateEnd())) {
                throw new ApplicationException("Data Inicial não pode ser maior que a data final");
            } else if((type.equals(FieldType.DATE) || type.equals(FieldType.DATETIME)) && condition.getDateEnd().before(condition.getDateStart())) {
                throw new ApplicationException("Data final não pode ser maior que a data inicial");
            } else if((type.equals(FieldType.NUMBER)) && condition.getNumStart() > (condition.getNumEnd())) {
                throw new ApplicationException("O valor inicial não pode ser maior que o valor final");
            } else if((type.equals(FieldType.NUMBER)) && condition.getNumEnd() < (condition.getNumStart())) {
                throw new ApplicationException("O valor final não pode ser maior que o valor inicial");
            }
        }
        
    }

}
