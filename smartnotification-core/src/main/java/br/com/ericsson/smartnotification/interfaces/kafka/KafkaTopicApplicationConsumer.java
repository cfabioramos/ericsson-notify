package br.com.ericsson.smartnotification.interfaces.kafka;

import java.util.Map;

import org.springframework.kafka.core.ProducerFactory;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;

public interface KafkaTopicApplicationConsumer {

    public Map<String, Object> producerNotificationConfigs();

    public ProducerFactory<String, String> producerNotificationFactory();

    public KafkaTemplatePublisher kafkaTemplatePublisher();
    
    public KafkaTopicConsumer receiver();
    
    public String getBootstrapServers();
    
    public Map<String, Object>  getConsumerConfigs();
}
