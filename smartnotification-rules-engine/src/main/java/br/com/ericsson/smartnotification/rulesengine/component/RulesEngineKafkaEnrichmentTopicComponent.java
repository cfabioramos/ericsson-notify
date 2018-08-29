package br.com.ericsson.smartnotification.rulesengine.component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;
import br.com.ericsson.smartnotification.rulesengine.kafka.KafkaRulesEngineEnrichmentPriorityTopic;

@Service
public class RulesEngineKafkaEnrichmentTopicComponent {

    private static final Logger LOG = LoggerFactory.getLogger(EventNotificationRulesSolverComponent.class);

    @Autowired
    private KafkaRulesEngineEnrichmentPriorityTopic engineEnrichmentPriorityTopic;

    public void send(Event event, List<RuleTemplateMessage> ruleTemplates) {
        for (RuleTemplateMessage ruleTemplate : ruleTemplates) {

            LOG.info("IdEvento {} - Recuperando prioridade do template: {}", event.getId(),
                    ruleTemplate.getTemplateMessage().getId());

            LOG.info("IdEvento {} - Enviando evento da fila de enriquecimento prioridade ({}): evento: {}",
                    event.getId(), ruleTemplate.getTemplateMessage().getPriority(), event);

            EventEnrichmentObject enrichmentTopicObject = new EventEnrichmentObject(
                    ruleTemplate.getTemplateMessage().getPriority(), event, ruleTemplate.getIdRule(),
                    ruleTemplate.getTemplateMessage().getId());

            engineEnrichmentPriorityTopic.toTopic(enrichmentTopicObject);
        }
    }

}
