package br.com.ericsson.smartnotification.abstractions.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import br.com.ericsson.smartnotification.domain.kafka.KafkaTopicObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicObjectQueue;
import br.com.ericsson.smartnotification.utils.Util;

public class KafkaTemplatePublisher extends KafkaTemplate<String, String> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaTemplatePublisher.class);

    public KafkaTemplatePublisher(ProducerFactory<String, String> producerFactory) {
        super(producerFactory);
    }

    public ListenableFuture<SendResult<String, String>> send(KafkaTopic topic, KafkaTopicObject data) {
        LOG.info("{} sendo enviado para a fila de {}: {}", data.getClass().getSimpleName(), topic, data);
        return this.send(topic.toString(), data);
    }

    public ListenableFuture<SendResult<String, String>> send(KafkaTopic topic, TopicPriority topicPriority,
            KafkaTopicObject data) {
        LOG.info("{} sendo enviado para a fila de {}{}: {}", data.getClass().getSimpleName(), topic, topicPriority,
                data);
        return this.send(topic.toString().concat(topicPriority.toString()), data);
    }

    public ListenableFuture<SendResult<String, String>> send(KafkaTopic topic, TopicPriority topicPriority,
            KafkaTopicObjectQueue data) {
        LOG.info("{} sendo enviado para a fila de {}{}: {}", data.getClass().getSimpleName(), topic, topicPriority,
                data);
        return this.send(topic.toString().concat(topicPriority.toString()), data);
    }

    protected ListenableFuture<SendResult<String, String>> send(String topic, KafkaTopicObjectQueue data) {
        data.setId(Util.gerateId());
        return super.send(topic, data.toString());
    }

}
