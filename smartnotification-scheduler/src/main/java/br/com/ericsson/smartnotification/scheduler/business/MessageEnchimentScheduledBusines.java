package br.com.ericsson.smartnotification.scheduler.business;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.entities.EventMessageEnrichedScheduled;
import br.com.ericsson.smartnotification.repository.EventMessageEnrichedScheduledRepository;
import br.com.ericsson.smartnotification.utils.DateUtil;

@Service
public class MessageEnchimentScheduledBusines {

    private static final Logger LOG = LoggerFactory.getLogger(MessageEnchimentScheduledBusines.class);

    @Autowired
    private EventMessageEnrichedScheduledRepository scheduledRepository;

    @Autowired
    private SchedulerBusiness schedulerBusiness;

    public void publishMessagesEnrichedsScheduleds(Date date) {
        int total = 0;
        LOG.info("Buscando Mensagens enriquecidas agendadas pelo scheduler {}", date);
        int page = 0;
        List<EventMessageEnrichedScheduled> messageEnrichedScheduleds = this.getMessagesEnrichedsScheduleds(date, page);
        while (!messageEnrichedScheduleds.isEmpty()) {
            for (EventMessageEnrichedScheduled eventMessageEnrichedScheduled : messageEnrichedScheduleds) {
                this.deleteMessage(eventMessageEnrichedScheduled);
                schedulerBusiness.scheduler(eventMessageEnrichedScheduled.getEventMessageEnriched());
                total++;
            }
            page++;
            messageEnrichedScheduleds = this.getMessagesEnrichedsScheduleds(date, page);
        }
        LOG.info("{} mensagen(s) processada(s) pelo agendador", total);
    }

    private List<EventMessageEnrichedScheduled> getMessagesEnrichedsScheduleds(Date date, int page) {
        
        DayOfWeek dayOfWeek = DateUtil.getDayOfWeek(date);
        Pageable pageable = scheduledRepository.createPageRequest(page);
        LocalTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
        
        return scheduledRepository
                .findByEventMessageEnrichedShippingRestrictionDateStartBeforeAndEventMessageEnrichedShippingRestrictionDaysOfWeekContainingAndEventMessageEnrichedShippingRestrictionTimeStartLessThan(
                        date, 
                        dayOfWeek,
                        time,
                        pageable);
    }

    private void deleteMessage(EventMessageEnrichedScheduled eventMessageEnrichedScheduled) {
        LOG.info("Removendo mensagem do agendamento- {}", eventMessageEnrichedScheduled);
        scheduledRepository.delete(eventMessageEnrichedScheduled);
    }

}
