package br.com.ericsson.smartnotification.interfaces.kafka;

import br.com.ericsson.smartnotification.domain.log.LogProperties;

public interface KafkaTopicLogProducer {
    
    public void publishInterface(LogProperties logProperties);
    
    public void publishNotification(LogProperties logProperties);
    
}
