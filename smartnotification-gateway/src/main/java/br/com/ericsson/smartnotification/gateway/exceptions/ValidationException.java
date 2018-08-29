package br.com.ericsson.smartnotification.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

@ResponseStatus( value = HttpStatus.BAD_REQUEST)
public class ValidationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ValidationException() {
        super("Requisição inválida");
    }

    public ValidationException(String cause) {
        super(String.format("Requisição inválida : %s", cause));
    }

    public ValidationException(EventDefinitionField eventDefinitionField) {
        super(String.format("o campo %s é obrigatório.", eventDefinitionField.getName()));
    }
    
    public ValidationException(EventField eventField, FieldType fieldType) {
        super(String.format("Campo %s do tipo %s está com valor inválido.", eventField.getName(), fieldType.name()));
    }
    
}
