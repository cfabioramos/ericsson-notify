package br.com.ericsson.smartnotification.interfaces.kafka;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public interface KafkaTopicConsumer {
    
    public void toReceive(String object) throws ApplicationException;
    
}
