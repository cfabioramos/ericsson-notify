package br.com.ericsson.smartnotification.log.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.domain.log.LogObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Service
public class KafkaInterfaceLogTopic implements KafkaTopicProducer<LogObject> {

	@Autowired
	private KafkaTemplatePublisher publisher;

	@Override
	public void toTopic(LogObject element) {
		publisher.send(KafkaTopic.INTERFACE_LOG, element);
	}

}
