package br.com.ericsson.smartnotification.gateway.business;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceNotFoundException;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceUnprocessableException;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;
import br.com.ericsson.smartnotification.repository.EventDefinitionRepository;
import br.com.ericsson.smartnotification.utils.DateUtil;

@Service
public class EventBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(EventBusiness.class);

    @Autowired
    private EventDefinitionRepository eventDefinitionRepository;

    @Autowired
    private KafkaTopicProducer<Event> eventTopicProducer;

    public void publishEvent(Event event) throws ApplicationException {

        LOG.info("Evento recebido pelo Gateway: {}", event);

        this.checkRequiredFields(event);

        this.eventTopicProducer.toTopic(event);
    }

    void checkRequiredFields(Event event) throws ApplicationException {
        checkRequiredFieldsDefault(event);
        Optional<EventDefinition> eventDefinitionOpt = eventDefinitionRepository.findByEventType(Integer
                .parseInt(event.getField(EventDefinitionField.EVENT_FIELD_NTYPE.getName()).getValue().toString()));
        if (eventDefinitionOpt.isPresent() && !eventDefinitionOpt.get().isExcluded()) {
            EventDefinition eventDefinition = eventDefinitionOpt.get();
            if (!eventDefinition.isActive()) {
                throw new ResourceUnprocessableException("Evento",
                        event.getField(EventDefinitionField.EVENT_FIELD_NTYPE.getName()).getValue().toString());
            }
            checkFieldsInvalids(event, eventDefinition);
            checkRequiredFields(event, eventDefinition.getFields());
            completeInfoEvent(event, eventDefinition);
        } else {
            throw new ResourceNotFoundException("Evento",
                    event.getField(EventDefinitionField.EVENT_FIELD_NTYPE.getName()).getValue().toString());
        }

    }

    private void completeInfoEvent(Event event, EventDefinition eventDefinition) {
        event.setDescription(eventDefinition.getDescription());
        event.setEventType(Integer
                .parseInt(event.getField(EventDefinitionField.EVENT_FIELD_NTYPE.getName()).getValue().toString()));
        for (EventDefinitionField eventDefinitionField : eventDefinition.getFields()) {
            if (event.getField(eventDefinitionField.getName()) != null) {
                event.getField(eventDefinitionField.getName()).setDescription(eventDefinitionField.getDescription());
                event.getField(eventDefinitionField.getName()).setType(eventDefinitionField.getType());
            }
        }
    }

    private void checkRequiredFieldsDefault(Event event) throws ApplicationException {
        if (!event.getFields().isEmpty()) {
            checkRequiredFields(event, Arrays.asList(EventDefinition.getEventFieldRequiredDefault()));
        } else {
            throw new ValidationException();
        }
    }

    private void checkRequiredFields(Event event, List<EventDefinitionField> eventDefinitionFields)
            throws ApplicationException {
        for (EventDefinitionField eventDefinitionField : eventDefinitionFields) {
            if (eventDefinitionField.isRequired() && (event.getField(eventDefinitionField.getName()) == null
                    || event.getField(eventDefinitionField.getName()).getValue() == null
                    || event.getField(eventDefinitionField.getName()).getValue().toString().isEmpty())) {
                throw new ValidationException(eventDefinitionField);
            }
            if (event.getField(eventDefinitionField.getName()) != null) {
                validadeValueField(event.getField(eventDefinitionField.getName()), eventDefinitionField.getType());
            }
        }
    }

    private void checkFieldsInvalids(Event event, EventDefinition eventDefinition) throws ApplicationException {
        for (EventField field : event.getFields()) {
            if (eventDefinition.getField(field.getName()) == null) {
                throw new ValidationException(
                        String.format("Campo %s não especificado na definição do evento", field.getName()));
            }
        }
    }

    private void validadeValueField(EventField eventField, FieldType fieldType) throws ApplicationException {
        try {
            if (FieldType.DATE.equals(fieldType)) {
                validadeFieldDate(eventField);
            } else if (FieldType.DATETIME.equals(fieldType)) {
                validadeFieldDateTime(eventField);
            } else if (FieldType.NUMBER.equals(fieldType)) {
                validadeFieldNumber(eventField);
            }
        } catch (ApplicationException e) {
            throw new ValidationException(eventField, fieldType);
        }
    }

    private void validadeFieldDate(EventField eventField) throws ApplicationException {
        DateUtil.parseToDate(eventField.getValue().toString());
    }

    private void validadeFieldDateTime(EventField eventField) throws ApplicationException {
        DateUtil.parseToDateTime(eventField.getValue().toString());
    }

    private void validadeFieldNumber(EventField eventField) throws ApplicationException {
        try {
            Long.parseLong(eventField.getValue().toString());
        } catch (NumberFormatException e) {
            throw new ValidationException(eventField, FieldType.NUMBER);
        }
    }

}
