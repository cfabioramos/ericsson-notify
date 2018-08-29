package br.com.ericsson.smartnotification.gateway.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Component(value = "eventTopic")
public class KafkaGatewayEventTopic implements KafkaTopicProducer<Event> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaGatewayEventTopic.class);

    @Autowired
    private KafkaTemplatePublisher publisher;

    @Override
    public void toTopic(Event event) {
        LOG.info("Evento validado, sendo enviado para a fila de triagem: {}", event);
        publisher.send(KafkaTopic.EVENT, event);
    }
}

