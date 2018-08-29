package br.com.ericsson.smartnotification.route.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicPriorityConsumer;
import br.com.ericsson.smartnotification.route.business.RouteBusiness;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Service
public class KafkaRouteReceiver implements KafkaTopicPriorityConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaRouteReceiver.class);

    @Autowired
    private RouteBusiness business;

    @Override
    public void toReceive(String object) throws ApplicationException {
        MessageEnrichedObject eventMessageEnriched = JsonUtil.getGson().fromJson(object, MessageEnrichedObject.class);
        LOG.info("Mensagem Enriquecida recebida pela fila {}{} : {}", KafkaTopic.NOTIFICATION,
                eventMessageEnriched.geEnrichmentObject().getPriority(), eventMessageEnriched);
        business.route(eventMessageEnriched);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.notification_high}")
    public void toReceiveHigh(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.notification_medium}")
    public void toReceiveMedium(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.notification_low}")
    public void toReceiveLow(String object) throws ApplicationException {
        this.toReceive(object);
    }

}
