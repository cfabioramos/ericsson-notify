package br.com.ericsson.smartnotification.campaign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
@EnableScheduling
public class CampaignApplication{

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
	
    public static void main(String[] args) {
        SpringApplication.run(CampaignApplication.class, args);
    }

}
