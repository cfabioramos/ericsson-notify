package br.com.ericsson.smartnotification.web.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.annotation.Transient;

import br.com.ericsson.smartnotification.entities.AbstractDocument;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

@SuppressWarnings("rawtypes")
public abstract class AbstractDto<D extends AbstractDocument, T extends AbstractDto> {
    
    private String id;
    
    private Boolean active;
    

    public AbstractDto(String id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public Boolean isActive() {
        return active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public D setValuesFromDocument(D document) throws ApplicationException {
        List<Field> fields = new ArrayList<>(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));
        for(Field field : fields) {
            if(field.getDeclaredAnnotation(Transient.class) == null && !field.getName().contains("$") && !Modifier.isStatic(field.getModifiers())) {
                try {
                    Object valueFielFrom = this.getClass().getMethod(getMethodNameGet(field)).invoke(this);
                    if(valueFielFrom instanceof List && ((List)valueFielFrom).isEmpty()) {
                        continue;
                    }
                    Method methodSetTo = document.getClass().getMethod(getMethodNameSet(field), field.getType());
                    methodSetTo.invoke(document, valueFielFrom);
                }catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                        | SecurityException e) {
                    throw new ApplicationException(String.format("Erro ao obter valor do campo %s do DTO %s : %s", field.getName(),this.getClass().getSimpleName(), e.getMessage()));
                }
                
            }
        }
        return document;
    }
    
    @SuppressWarnings("unchecked")
    public T setValuesFromDto(D document) throws ApplicationException {
        List<Field> fields = new ArrayList<>(Arrays.asList(this.getClass().getSuperclass().getDeclaredFields()));
        fields.addAll(Arrays.asList(this.getClass().getDeclaredFields()));
        for(Field field : fields) {
            if(field.getDeclaredAnnotation(Transient.class) == null && !field.getName().contains("$") && !Modifier.isStatic(field.getModifiers())) {
                try {
                    Object valueFielFrom = document.getClass().getMethod(getMethodNameGet(field)).invoke(document);
                    Method methodSetTo = this.getClass().getMethod(getMethodNameSet(field), field.getType());
                    methodSetTo.invoke(this, valueFielFrom);
                }catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                        | SecurityException e) {
                    throw new ApplicationException(String.format("Erro ao obter valor do campo %s do Document %s : %s", field.getName(), getDocumentName() , e.getMessage()));
                }
            }
        }
        return (T) this;
    }
    
    @SuppressWarnings("unchecked")
    public List<T> setValuesFromDtos(List<? extends AbstractDocument> documents, Class<? extends AbstractDto> dtoClas) throws ApplicationException {
        List<T> listDtos = new ArrayList<>(); 
        for(AbstractDocument document : documents) {
            try {
                listDtos.add((T) dtoClas.newInstance().setValuesFromDto(document));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ApplicationException(String.format("Erro ao instanciar %s : %s", dtoClas.getSimpleName(), e.getMessage()));
            }

        }
        return listDtos;
    }
    
    public static List<AbstractDocument> setValuesFromDocuments(List<? extends AbstractDto> dtos) throws ApplicationException {
        List<AbstractDocument> listDocuments = new ArrayList<>(); 
        for(AbstractDto dto : dtos) {
            listDocuments.add(dto.getNewDocument());
        }
        return listDocuments;
    }
    
    public D getNewDocument() throws ApplicationException{
        D document = getDocument();
        return this.setValuesFromDocument(document);
    }
    
    static String getMethodNameSet(Field field) {
        return getMethodName(field, "set");
    }
    
    static String getMethodNameGet(Field field) {
        String typeMethod = Boolean.class.getSimpleName().equalsIgnoreCase(field.getType().getSimpleName()) ? "is" : "get"; 
        return getMethodName(field, typeMethod);
    }
    
    private static String getMethodName(Field field, String typeMethod) {
        StringBuilder method = new StringBuilder(typeMethod);
        method.append(field.getName().substring(0, 1).toUpperCase());
        method.append(field.getName().substring(1));
        return method.toString();        
    }
    
    protected abstract D getDocument();
    
    public String getDocumentName() {
        return getDocument().getClass().getSimpleName();
    }

}
