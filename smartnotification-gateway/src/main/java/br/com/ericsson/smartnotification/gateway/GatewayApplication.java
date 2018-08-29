package br.com.ericsson.smartnotification.gateway;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTopicApplication;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
public class GatewayApplication extends KafkaTopicApplication{

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Override
    public KafkaTopicConsumer receiver() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    @Bean
    @Override
    public Map<String, Object> getConsumerConfigs() {
        return this.consumerConfigs(KafkaTopic.EVENT);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
    
}
