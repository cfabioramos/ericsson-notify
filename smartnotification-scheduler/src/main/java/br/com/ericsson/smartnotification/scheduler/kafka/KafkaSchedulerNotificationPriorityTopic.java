package br.com.ericsson.smartnotification.scheduler.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Service
public class KafkaSchedulerNotificationPriorityTopic implements KafkaTopicProducer<MessageEnrichedObject> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaSchedulerNotificationPriorityTopic.class);

    @Autowired
    private KafkaTemplatePublisher publisher;

    @Override
    public void toTopic(MessageEnrichedObject messageEnriched) {

        LOG.info("Enviado mensagem para a fila de route: {}", messageEnriched);

        publisher.send(KafkaTopic.NOTIFICATION, messageEnriched.geEnrichmentObject().getPriority(), messageEnriched);
    }

}
