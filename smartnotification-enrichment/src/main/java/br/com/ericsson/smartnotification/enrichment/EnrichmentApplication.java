package br.com.ericsson.smartnotification.enrichment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTopicApplication;
import br.com.ericsson.smartnotification.enrichment.kafka.KafkaEnrichmentReceiver;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
public class EnrichmentApplication extends KafkaTopicApplication {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static void main(String[] args) {
        SpringApplication.run(EnrichmentApplication.class, args);
    }

    @Bean
    @Override
    public KafkaTopicConsumer receiver() {
        return new KafkaEnrichmentReceiver();
    }

    @Override
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    @Bean
    @Override
    public Map<String, Object> getConsumerConfigs() {
        return this.consumerConfigs(KafkaTopic.ENRICHMENT);
    }

}
