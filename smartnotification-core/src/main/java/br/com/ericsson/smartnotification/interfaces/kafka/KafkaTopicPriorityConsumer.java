package br.com.ericsson.smartnotification.interfaces.kafka;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public interface KafkaTopicPriorityConsumer extends KafkaTopicConsumer{
    
    public void toReceiveHigh(String object) throws ApplicationException;
    
    public void toReceiveMedium(String object) throws ApplicationException;
    
    public void toReceiveLow(String object) throws ApplicationException;
    
}
