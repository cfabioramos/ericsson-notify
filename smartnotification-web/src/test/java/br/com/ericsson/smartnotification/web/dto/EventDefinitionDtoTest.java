package br.com.ericsson.smartnotification.web.dto;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.Test;

import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.web.dto.EventDefinitionDto;

public class EventDefinitionDtoTest extends DtoTest{
    
    @Test
    public void parserDtoToDocumentSucessTest() throws ApplicationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        EventDefinitionDto dto = new EventDefinitionDto("1", true, 1,  "name", "desc", "", new ArrayList<>());
        dto.getFields().add(new EventDefinitionField("teste", "teste", FieldType.STRING, true));
        dto.getFields().add(new EventDefinitionField("teste", "teste", FieldType.NUMBER, true));
        EventDefinition document = dto.getNewDocument();
        
        assertFieldsAllEquals(dto, document);
    }
    
    @Test
    public void parserDocumentToDtoSucessTest() throws ApplicationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        EventDefinition document = new EventDefinition("1", "name", 1, "teste", true);
        
        EventDefinitionDto dto = new EventDefinitionDto().setValuesFromDto(document);
        
        assertFieldsAllEquals(dto, document);
    }
    

}
