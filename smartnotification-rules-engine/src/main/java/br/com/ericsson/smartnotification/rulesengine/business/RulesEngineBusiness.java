package br.com.ericsson.smartnotification.rulesengine.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.log.LogProperties;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.enums.ErrorType;
import br.com.ericsson.smartnotification.enums.Project;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.rulesengine.component.EventNotificationRulesSolverComponent;
import br.com.ericsson.smartnotification.rulesengine.component.OptOutComponent;
import br.com.ericsson.smartnotification.rulesengine.component.RulesEngineKafkaEnrichmentTopicComponent;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;

@Service
public class RulesEngineBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEngineBusiness.class);

    @Autowired
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopicBusiness;

    @Autowired
    private OptOutComponent optOutComponent;

    @Autowired
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;

    @Autowired
    private KafkaTopicLogProducer logQueue;

    public void publish(Event event) {
        try {
            List<RuleTemplateMessage> ruleTemplatesActive = eventNotificationRulesSolver.notificationsFor(event);

            if (!ruleTemplatesActive.isEmpty()) {

                if (!this.optOutComponent.isOptOut(event)) {
                    LOG.info("IdEvento {} - MSISDN {} sem OptOut", event.getId(),
                            event.getField(EventDefinitionField.EVENT_FIELD_MSISDN.getName()).getValue());

                    rulesEngineKafkaEnrichmentTopicBusiness.send(event, ruleTemplatesActive);

                } else {
                    LOG.info("IdEvento {} - Descartando evento para MSISDN {} por OptOut", event.getId(),
                            event.getField(EventDefinitionField.EVENT_FIELD_MSISDN.getName()).getValue());
                    this.logQueue.publishNotification(new LogProperties(this.getClass(), "", Project.RULES_ENGINE, event, ErrorType.OPTOUT));
                }

            } else {
                LOG.info("IdEvento {} - Descartando evento {} devido nenhuma regra aplicada", event.getId(),
                        event.getField(EventDefinitionField.EVENT_FIELD_MSISDN.getName()).getValue());
                this.logQueue.publishNotification(new LogProperties(this.getClass(), "", Project.RULES_ENGINE, event, ErrorType.OPTOUT));
            }
        } catch (ApplicationException e) {
            LOG.info("IdEvento {} - Descartando evento - {}", event.getId(), e.getMessage());
            this.logQueue.publishNotification(new LogProperties(this.getClass(), "", Project.RULES_ENGINE, event, ErrorType.OPTOUT));
        }

    }

}
