package br.com.ericsson.smartnotification.log.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.log.LogObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicConsumer;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Service
public class KafkaLogReceiver implements KafkaTopicConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaLogReceiver.class);

    
//    TODO 
//    @KafkaListener(topics = "${kafka.topic.notification_log}")
    public void toReceive(String logElement) throws ApplicationException {
        LogObject logObject = JsonUtil.getGson().fromJson(logElement, LogObject.class);
        LOG.info("Log recebido da fila notification_log: {}", logObject);
    }

}
