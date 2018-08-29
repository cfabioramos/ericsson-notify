package br.com.ericsson.smartnotification.enrichment.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.domain.log.LogProperties;
import br.com.ericsson.smartnotification.enrichment.component.EnrichComponent;
import br.com.ericsson.smartnotification.enrichment.kafka.KafkaNotifyEnricherSchedulerPriorityTopic;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.enums.ErrorType;
import br.com.ericsson.smartnotification.enums.Project;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;

@Service
public class EnrichmentBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(EnrichmentBusiness.class);

    @Autowired
    private EnrichComponent enrichComponent;
    
    @Autowired
    private KafkaNotifyEnricherSchedulerPriorityTopic schedulerTopic;
    
    @Autowired
    private KafkaTopicLogProducer logQueue;
    
    public void enrich(EnrichmentObject enrichmentObject) {
        LOG.info("Iniciando enriquecimento com origem em Evento ou Campanha: {}", enrichmentObject);
        try {
            MessageEnrichedObject messageEnriched = enrichComponent.enrich(enrichmentObject);
            schedulerTopic.toTopic(messageEnriched);
        } catch (ApplicationException e) {
            LOG.info("Erro ao obter enriquecimento : {}", e.getMessage());
            if(enrichmentObject instanceof EventEnrichmentObject){
                logQueue.publishNotification(new LogProperties(EnrichmentObject.class, "enrich", Project.ENRICHMENT, ((EventEnrichmentObject)enrichmentObject).getEvent(), ErrorType.ENRICHEMENT));
            }else {
                logQueue.publishNotification(new LogProperties(EnrichmentObject.class, "enrich", Project.ENRICHMENT, ErrorType.ENRICHEMENT, ((CampaignEnrichmentObject)enrichmentObject).getCampaignId(), ((CampaignEnrichmentObject)enrichmentObject).getMsisdn()));
            }
        }
    }
    
}
