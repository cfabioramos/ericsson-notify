package br.com.ericsson.smartnotification.scheduler.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.scheduler.component.SchedulerValidationShippingRestrictionsComponent;
import br.com.ericsson.smartnotification.scheduler.kafka.KafkaSchedulerNotificationPriorityTopic;


@Service
public class SchedulerBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerBusiness.class);

    @Autowired
    private KafkaSchedulerNotificationPriorityTopic kafkaSchedulerNotificationPriorityTopic;

    @Autowired
    private SchedulerValidationShippingRestrictionsComponent schedulerValidationShippingRestrictionsComponent;

    public void scheduler(MessageEnrichedObject messageEnriched) {
        
        LOG.info("Mensagem Recebida pelo scheduler : {}", messageEnriched);

        if (schedulerValidationShippingRestrictionsComponent.isValid(messageEnriched)) {
            
            kafkaSchedulerNotificationPriorityTopic.toTopic(messageEnriched);
            
        }

    }
}
