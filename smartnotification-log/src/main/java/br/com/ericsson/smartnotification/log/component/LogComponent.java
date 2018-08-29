package br.com.ericsson.smartnotification.log.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.log.LogObject;
import br.com.ericsson.smartnotification.domain.log.LogProperties;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.log.kafka.KafkaInterfaceLogTopic;
import br.com.ericsson.smartnotification.log.kafka.KafkaNotificationLogTopic;

@Service
public class LogComponent implements KafkaTopicLogProducer{

    @Autowired
    private KafkaNotificationLogTopic kafkaNotificationLogTopic;

    @Autowired
    private KafkaInterfaceLogTopic kafkaInterfaceLogTopic;
    
	@Override
	public void publishInterface(LogProperties logProperties) {
		LogObject logObject = new LogObject(logProperties);
		kafkaInterfaceLogTopic.toTopic(logObject);
	}

	@Override
	public void publishNotification(LogProperties logProperties) {
		LogObject logObject = new LogObject(logProperties);
		kafkaNotificationLogTopic.toTopic(logObject);
	}

}
