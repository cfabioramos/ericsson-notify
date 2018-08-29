package br.com.ericsson.smartnotification.interfaces.kafka;

import java.util.Map;

import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import br.com.ericsson.smartnotification.enums.KafkaTopic;

public interface KafkaTopicApplicationProducer {

    public Map<String, Object> consumerConfigs(KafkaTopic kafkaTopic);

    public ConsumerFactory<String, String> consumerFactory();

    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory();

    public KafkaTopicConsumer receiver();

}
