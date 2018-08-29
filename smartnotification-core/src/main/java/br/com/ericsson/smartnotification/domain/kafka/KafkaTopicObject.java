package br.com.ericsson.smartnotification.domain.kafka;

import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicObjectQueue;
import br.com.ericsson.smartnotification.utils.JsonUtil;

public class KafkaTopicObject implements KafkaTopicObjectQueue {

    private static final long serialVersionUID = 1L;

    private String id;

    @Override
    public String toString() {
        return JsonUtil.parseToJsonString(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String idGeratedKafka) {
        this.id = idGeratedKafka;
    }

}
