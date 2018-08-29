package br.com.ericsson.smartnotification.rulesengine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTopicApplication;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;
import br.com.ericsson.smartnotification.rulesengine.kafka.KafkaRulesEngineReceiver;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
public class RulesEngineApplication extends KafkaTopicApplication{

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static void main(String[] args) {
        SpringApplication.run(RulesEngineApplication.class, args);
    }

    @Override
    public KafkaTopicConsumer receiver() {
        return new KafkaRulesEngineReceiver();
    }

    @Override
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    @Override
    public Map<String, Object> getConsumerConfigs() {
        return this.consumerConfigs(KafkaTopic.EVENT);
    }

}
