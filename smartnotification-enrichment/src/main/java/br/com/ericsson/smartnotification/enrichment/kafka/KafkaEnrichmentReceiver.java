package br.com.ericsson.smartnotification.enrichment.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.enrichment.business.EnrichmentBusiness;
import br.com.ericsson.smartnotification.enums.KafkaTopic;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicPriorityConsumer;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Component
public class KafkaEnrichmentReceiver implements KafkaTopicPriorityConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaEnrichmentReceiver.class);

    @Autowired
    private EnrichmentBusiness business;

    public void toReceive(String object) throws ApplicationException {
    	EnrichmentObject enrichmentTopicObject = this.getEnrichmentObject(object);
        LOG.info("Evento recebido pelo pela fila {}{} : {}", KafkaTopic.ENRICHMENT,enrichmentTopicObject.getPriority(), enrichmentTopicObject);
        business.enrich(enrichmentTopicObject);
    }
    
    private EnrichmentObject getEnrichmentObject(String object) {
    	return object.contains(EventEnrichmentObject.class.getSimpleName()) ? JsonUtil.getGson().fromJson(object, EventEnrichmentObject.class) : JsonUtil.getGson().fromJson(object, CampaignEnrichmentObject.class);
    }
    @Override
    @KafkaListener(topics = "${kafka.topic.enrichment_high}")
    public void toReceiveHigh(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.enrichment_medium}")
    public void toReceiveMedium(String object) throws ApplicationException {
        this.toReceive(object);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.enrichment_low}")
    public void toReceiveLow(String object) throws ApplicationException {
        this.toReceive(object);
    }
}
