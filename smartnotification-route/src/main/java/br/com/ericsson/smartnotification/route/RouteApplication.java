package br.com.ericsson.smartnotification.route;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTopicApplication;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;
import br.com.ericsson.smartnotification.route.kafka.KafkaRouteReceiver;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
@EnableScheduling
public class RouteApplication extends KafkaTopicApplication {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
    }

    @Override
    public KafkaTopicConsumer receiver() {
        return new KafkaRouteReceiver();
    }

    @Override
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    @Override
    public Map<String, Object> getConsumerConfigs() {
        return this.consumerConfigs(KafkaTopic.ROUTE);
    }

}
