package br.com.ericsson.smartnotification.web.dto;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.springframework.data.annotation.Transient;

import br.com.ericsson.smartnotification.entities.AbstractDocument;
import br.com.ericsson.smartnotification.web.dto.AbstractDto;

public abstract class DtoTest {
    
    @SuppressWarnings("rawtypes")
    protected void assertFieldsAllEquals(AbstractDto dto, AbstractDocument document) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = dto.getClass().getDeclaredFields();
        for(Field field : fields) {
            if(field.getDeclaredAnnotation(Transient.class) == null && !field.getName().contains("$") && !Modifier.isStatic(field.getModifiers())) {
                Object valueFielDto = dto.getClass().getMethod(AbstractDto.getMethodNameGet(field)).invoke(dto);
                Object valueFielDocument = document.getClass().getMethod(AbstractDto.getMethodNameGet(field)).invoke(document);
                assertEquals(valueFielDto, valueFielDocument);
            }
        }
    }
    

}
