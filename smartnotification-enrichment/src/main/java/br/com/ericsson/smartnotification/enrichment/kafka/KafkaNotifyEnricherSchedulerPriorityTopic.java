package br.com.ericsson.smartnotification.enrichment.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Service
public class KafkaNotifyEnricherSchedulerPriorityTopic implements KafkaTopicProducer<MessageEnrichedObject> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaNotifyEnricherSchedulerPriorityTopic.class);

    @Autowired
    private KafkaTemplatePublisher sender;

    @Override
    public void toTopic(MessageEnrichedObject object) {
        LOG.info("Enviando Mensagens Enriquecida para Scheduler: {}", object);
        sender.send(KafkaTopic.SCHEDULER, object.geEnrichmentObject().getPriority(), object);
    }

}
