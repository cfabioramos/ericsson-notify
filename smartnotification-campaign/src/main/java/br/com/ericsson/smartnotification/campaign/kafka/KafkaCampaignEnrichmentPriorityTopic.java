package br.com.ericsson.smartnotification.campaign.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.abstractions.kafka.KafkaTemplatePublisher;
import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicProducer;

@Service
public class KafkaCampaignEnrichmentPriorityTopic implements KafkaTopicProducer<CampaignEnrichmentObject> {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaCampaignEnrichmentPriorityTopic.class);

    @Autowired
    private KafkaTemplatePublisher publisher;

    @Override
    public void toTopic(CampaignEnrichmentObject object) {
        LOG.info("Template enviado para fila {}{} : {}", KafkaTopic.ENRICHMENT, object.getPriority(), object);
        publisher.send(KafkaTopic.ENRICHMENT, object.getPriority(), object);
    }

}
