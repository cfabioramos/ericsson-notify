package br.com.ericsson.smartnotification.rulesengine.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;
import br.com.ericsson.smartnotification.rulesengine.business.RulesEngineBusiness;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Service
public class KafkaRulesEngineReceiver implements KafkaTopicConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaRulesEngineReceiver.class);

    
    @Autowired
    private RulesEngineBusiness rulesEngineBusiness;
    
    @KafkaListener(topics = "${kafka.topic.event}")
    public void toReceive(String eventDefinitionJson) {
        LOG.info("Evento recebido pelo Rules Engine: {}", eventDefinitionJson);
        Event event = JsonUtil.getGson().fromJson(eventDefinitionJson, Event.class);
        this.rulesEngineBusiness.publish(event);
    }

}
