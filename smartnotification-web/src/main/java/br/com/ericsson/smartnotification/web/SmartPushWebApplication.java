package br.com.ericsson.smartnotification.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("br.com.ericsson.smartnotification")
@Configuration
public class SmartPushWebApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SmartPushWebApplication.class, args);
    }
    
}
