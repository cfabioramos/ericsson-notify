package br.com.ericsson.smartnotification.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTopicApplication;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;
import br.com.ericsson.smartnotification.log.kafka.KafkaLogReceiver;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
public class LogApplication extends KafkaTopicApplication{

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;
    
    @Override
    public KafkaTopicConsumer receiver() {
        return new KafkaLogReceiver();
    }

    @Override
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    @Override
    public Map<String, Object> getConsumerConfigs() {
        return this.consumerConfigs(KafkaTopic.NOTIFICATION_LOG);
    }

//
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaTopic.NOTIFICATION_LOG.toString());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        return props;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//    }
//
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//    
//    @Bean
//    public KafkaLogReceiver kafkaRulesEngineReceiver() {
//        return new KafkaLogReceiver();
//    }
//
//    /****** Producer notification **/
//    @Bean
//    public Map<String, Object> producerNotificationConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    @Bean
//    public ProducerFactory<String, String> producerNotificationFactory() {
//        return new DefaultKafkaProducerFactory<>(producerNotificationConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerNotificationFactory());
//    }
    
}
