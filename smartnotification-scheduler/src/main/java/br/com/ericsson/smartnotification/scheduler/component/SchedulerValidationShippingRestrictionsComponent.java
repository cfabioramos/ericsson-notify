package br.com.ericsson.smartnotification.scheduler.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.EventMessageEnrichedScheduled;
import br.com.ericsson.smartnotification.repository.EventMessageEnrichedScheduledRepository;

@Component
public class SchedulerValidationShippingRestrictionsComponent {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerValidationShippingRestrictionsComponent.class);
    
    @Autowired
    private EventMessageEnrichedScheduledRepository enrichedScheduledRepository;

    public boolean isValid(MessageEnrichedObject eventMessageEnriched) {
        LOG.info("Validando Mensagem Eriquecida- {}", eventMessageEnriched);
        if (eventMessageEnriched.getShippingRestriction() != null) {
            LOG.info("Verificando restrição de envio - {}", eventMessageEnriched.getShippingRestriction());
            
            LocalTime restrictTimeStartToSend = (eventMessageEnriched.getShippingRestriction().getTimeStart() == null)
                    ? LocalTime.MIN
                    : eventMessageEnriched.getShippingRestriction().getTimeStart();

            LocalDateTime dateSendMessage = LocalDateTime.now();
            LocalTime timeSendMessage = LocalTime.now();

            LOG.info("Verificando se é possivel enviar a mensagem - {}" , eventMessageEnriched.getMessagesEnriched());
            
            if ((!isValidDate(dateSendMessage.toLocalDate(), eventMessageEnriched) || 
                 (isValidDate(dateSendMessage.toLocalDate(), eventMessageEnriched)
                    && (eventMessageEnriched.getShippingRestriction().getTimeEnd() != null
                            && timeSendMessage.isAfter(eventMessageEnriched.getShippingRestriction().getTimeEnd()))))) {
                
                LOG.info("Não possível enviar mensagem em {} - {} - Verificando nova data de envio...", dateSendMessage.toLocalDate(), dateSendMessage.toLocalTime());
                
                do {
                    dateSendMessage = dateSendMessage.plusDays(1);
                } while (!isValidDate(dateSendMessage.toLocalDate(), eventMessageEnriched));

                LOG.info("Nova data e horario de envio - {} - {}", dateSendMessage.toLocalDate(), eventMessageEnriched.getShippingRestriction().getTimeStart());
                
                scheduleSendEventMessageEnriched(dateSendMessage.toLocalDate(), restrictTimeStartToSend, eventMessageEnriched);

                return false;

            } else {
                if (timeSendMessage.isBefore(restrictTimeStartToSend)) {

                    LOG.info("Não possível enviar mensagem em {} - Verificando nova data de envio...", dateSendMessage);
                    
                    LOG.info("Nova data e horario de envio - {} - {}", dateSendMessage.toLocalDate(), restrictTimeStartToSend);
                	
                    scheduleSendEventMessageEnriched(dateSendMessage.toLocalDate(), restrictTimeStartToSend, eventMessageEnriched);

                    return false;
                }
            }
        }

        return true;

    }

    private boolean isValidDate(LocalDate dateSendMessage, MessageEnrichedObject eventMessageEnriched) {
        return (eventMessageEnriched.getShippingRestriction().getDaysOfWeek().contains(dateSendMessage.getDayOfWeek()));
    }

    private void scheduleSendEventMessageEnriched(LocalDate dateSendMessage, LocalTime restrictTimeStartToSend,
            MessageEnrichedObject eventMessageEnriched) {
        eventMessageEnriched.getShippingRestriction()
                .setDateStart(Date.from(dateSendMessage.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eventMessageEnriched.getShippingRestriction().setTimeStart(restrictTimeStartToSend);

        EventMessageEnrichedScheduled enrichedScheduled = new EventMessageEnrichedScheduled(eventMessageEnriched);
        LOG.info("Agendando Mensagem Enriquecida - {}", enrichedScheduled);
        enrichedScheduledRepository.save(enrichedScheduled);
    }

}
