package br.com.ericsson.smartnotification.route.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Service
public class KafkaRouteSchedulerPriorityTopic implements KafkaTopicProducer<MessageEnrichedObject> {

    @Autowired
    private KafkaTemplatePublisher publisher;

    @Override
    public void toTopic(MessageEnrichedObject object) {
        publisher.send(KafkaTopic.SCHEDULER, object.geEnrichmentObject().getPriority(), object);
    }

}
