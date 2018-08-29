package br.com.ericsson.smartnotification.scheduler.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.scheduler.business.MessageEnchimentScheduledBusines;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private MessageEnchimentScheduledBusines scheduledBusiness;

    @Scheduled(fixedRate = 10000)
    public void loadMessageEnchimentScheduled() {
        Date date = Calendar.getInstance().getTime();
        log.info("Iniciando Agendador {}", date);
        scheduledBusiness.publishMessagesEnrichedsScheduleds(date);
    }
}
