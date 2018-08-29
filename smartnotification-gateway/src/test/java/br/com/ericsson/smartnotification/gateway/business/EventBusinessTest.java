package br.com.ericsson.smartnotification.gateway.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceNotFoundException;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.gateway.kafka.KafkaGatewayEventTopic;
import br.com.ericsson.smartnotification.repository.EventDefinitionRepository;


@RunWith(MockitoJUnitRunner.class)
public class EventBusinessTest {

    @Mock
    private EventDefinitionRepository eventDefinitionRepository;
    
    @Mock
    private KafkaGatewayEventTopic eventTopicGateway;
    
    @InjectMocks
    private EventBusiness business;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void validEventShouldBePublishedToEventTopic() throws ApplicationException {
        when(eventDefinitionRepository.findByEventType(1)).thenReturn(this.getEventDefinition());
        
        Event event = validEvent();
        business.publishEvent(event);
        
        verify(eventTopicGateway).toTopic(event);
    }
    
    @Test
    public void invalidEventsShoudNotBePublishedToTopic() {
        when(eventDefinitionRepository.findByEventType(1)).thenReturn(this.getEventDefinition());
        
        try {
            business.publishEvent(invalidEventMissingRequiredField());
        } catch(Exception e) {}
        
        verifyZeroInteractions(eventTopicGateway);
    }

    @Test
    public void validEventShouldNotThrowException() throws ApplicationException {
        when(eventDefinitionRepository.findByEventType(1)).thenReturn(this.getEventDefinition());
        
        business.checkRequiredFields(this.validEvent());
    }
    
    @Test
    public void checkRequiredFieldsFailTest() throws ApplicationException {
        when(eventDefinitionRepository.findByEventType(1)).thenReturn(this.getEventDefinition());
        
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("o campo teste é obrigatório.");
        
        business.checkRequiredFields(this.invalidEventMissingRequiredField());
    }
    
    @Test
    public void checkRequiredFieldsNoEventDefinitionTest() throws ApplicationException {
        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage("Evento não encontrado para identificador 1");
        
        business.checkRequiredFields(this.validEvent());
    }
    
    @Test
    public void checkRequiredFieldsInvalidTest() throws ApplicationException {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("Requisição inválida");
        
        business.checkRequiredFields(new Event(1, "teste"));
    }

    private Optional<EventDefinition> getEventDefinition() {
        EventDefinition eventDefinition = new EventDefinition("1", "AS",1, "Aviso de Assinatura", true);
        eventDefinition.addField(new EventDefinitionField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(),
                "Id da promo contratada", FieldType.NUMBER, true));
        eventDefinition.addField(new EventDefinitionField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(),
                "Nome da promo contratada", FieldType.STRING, true));
        eventDefinition.addField(new EventDefinitionField("teste", "teste", FieldType.STRING, true));
        return Optional.of(eventDefinition);
    }

    private Event validEvent() {
        Event event = new Event();
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "1"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "1111111111111"));
        event.getFields().add(new EventField("teste", "teste"));
        return event;
    }

    
    private Event invalidEventMissingRequiredField() {
        Event event = new Event();
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "1"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "1111111111111"));
        return event;
    }
 
}
