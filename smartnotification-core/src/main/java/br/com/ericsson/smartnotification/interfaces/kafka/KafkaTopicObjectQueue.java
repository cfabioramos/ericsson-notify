package br.com.ericsson.smartnotification.interfaces.kafka;

import java.io.Serializable;

public interface KafkaTopicObjectQueue extends Serializable {

    public String getId();

    public void setId(String idGeratedKafka);

}
