package br.com.ericsson.smartnotification.rulesengine.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;
import br.com.ericsson.smartnotification.rulesengine.component.EventNotificationRulesSolverComponent;
import br.com.ericsson.smartnotification.rulesengine.component.OptOutComponent;
import br.com.ericsson.smartnotification.rulesengine.component.RulesEngineKafkaEnrichmentTopicComponent;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;

@RunWith(MockitoJUnitRunner.class)
public class RulesEngineBusinessTest {

    @Mock
    private TemplateMessageRepository templateMessageRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private OptOutComponent optOutComponent;

    @Mock
    private KafkaTopicLogProducer logQueue;

    @Mock
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;

    @Mock
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopicBusiness;

    @InjectMocks
    private RulesEngineBusiness rulesEngineBusines;

    @Test
    public void optOutTrueTest() throws ApplicationException {
        Event event = getEventOptOutTrue();

        when(eventNotificationRulesSolver.notificationsFor(event)).thenReturn(getRuleTemplates());
        when(optOutComponent.isOptOut(event)).thenReturn(true);

        rulesEngineBusines.publish(event);

        verify(optOutComponent).isOptOut(event);
        verifyZeroInteractions(ruleRepository);
        verifyZeroInteractions(rulesEngineKafkaEnrichmentTopicBusiness);
    }

    private Event getEventOptOutTrue() {
        Event event = new Event(1, "Teste");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "", FieldType.STRING,
                true, "1111111111111"));
        event.getFields()
                .add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "", FieldType.NUMBER, true, "1"));
        event.setId("1");
        return event;
    }

    private List<RuleTemplateMessage> getRuleTemplates() {
        List<RuleTemplateMessage> ruleTemplates = new ArrayList<>();
        ruleTemplates.add(new RuleTemplateMessage("1", new TemplateMessage("teste", true, TopicPriority.MEDIUM)));
        return ruleTemplates;
    }

    @Test
    public void optOutFalseTest() throws ApplicationException {
        Event event = getEventOptOutTrue();
        
        
        List<RuleTemplateMessage> ruleTemplates = getRuleTemplates();

        when(eventNotificationRulesSolver.notificationsFor(event)).thenReturn(ruleTemplates);
        when(optOutComponent.isOptOut(event)).thenReturn(false);

        rulesEngineBusines.publish(event);

        verify(rulesEngineKafkaEnrichmentTopicBusiness).send(event, ruleTemplates);

    }

}
