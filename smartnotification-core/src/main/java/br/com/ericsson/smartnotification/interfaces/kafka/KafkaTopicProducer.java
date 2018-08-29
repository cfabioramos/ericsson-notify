package br.com.ericsson.smartnotification.interfaces.kafka;


public interface KafkaTopicProducer<T extends KafkaTopicObjectQueue> {
    
    public void toTopic(T object);

}
