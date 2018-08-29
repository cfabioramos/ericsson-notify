package br.com.ericsson.smartnotification.scheduler.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicPriorityConsumer;
import br.com.ericsson.smartnotification.scheduler.business.SchedulerBusiness;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Service
public class KafkaSchedulerReceiver implements KafkaTopicPriorityConsumer{

    private static final Logger LOG = LoggerFactory.getLogger(KafkaSchedulerReceiver.class);
    
    @Autowired
    private SchedulerBusiness business;

    @Override
    public void toReceive(String object) throws ApplicationException {
        MessageEnrichedObject eventMessageEnriched = JsonUtil.getGson().fromJson(object, MessageEnrichedObject.class);
        LOG.info("Mensagem Enriquecida recebida pela fila {}{} : {}", KafkaTopic.SCHEDULER,
        		eventMessageEnriched.geEnrichmentObject().getPriority(), eventMessageEnriched);
        business.scheduler(eventMessageEnriched);        
    }
    
    @Override
    @KafkaListener(topics = "${kafka.topic.scheduler_high}")
    public void toReceiveHigh(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.scheduler_medium}")
    public void toReceiveMedium(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.scheduler_low}")
    public void toReceiveLow(String object) throws ApplicationException {
        this.toReceive(object);
    }


}
