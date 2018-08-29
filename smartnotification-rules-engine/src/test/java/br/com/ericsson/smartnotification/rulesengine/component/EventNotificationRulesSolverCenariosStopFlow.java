package br.com.ericsson.smartnotification.rulesengine.component;

import static org.assertj.core.api.Assertions.assertThat;
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
import br.com.ericsson.smartnotification.entities.Condition;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.entities.Rule;
import br.com.ericsson.smartnotification.enums.ConditionEnum;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;
import br.com.ericsson.smartnotification.utils.DateUtil;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EventNotificationRulesSolverCenariosStopFlow {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopicBusiness;

    @InjectMocks
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;

    @Test
    public void evaluateTestNotificationsFor_RuleStopFlowUnicaAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "1-Regra é a Stop Flow, é a única da lista e é atendida");
        event.setId("3000000001");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleStopFlowPrimeiraAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate3, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "2-Regra é a Stop Flow, é a primeira da lista e é atendida");
        event.setId("3000000002");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleStopFlowPrimeiraNaoAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate3, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "3-Regra é a Stop Flow, é a primeira da lista e não é atendida");
        event.setId("3000000003");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 2).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate2).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId() == idTemplate3).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleStopFlowIntermediariaAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate3, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "4-Regra é a Stop Flow, está entre duas ou mais regras e é atendida");
        event.setId("3000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 2).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId() == idTemplate2).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleStopFlowIntermediariaNaoAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate3, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "5-Regra é a Stop Flow, está entre duas ou mais regras e não é atendida");
        event.setId("3000000005");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "12"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 2).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId() == idTemplate3).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleAnteriorStopFlowAtendida() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules = new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate3, DateUtil.parseToDate("2018-01-01"),
                DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "6-Regra não é Stop Flow, mas anterior é Stop Flow e anterior é atendida");
        event.setId("3000000006");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante",
                FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento",
                FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size() == 1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId() == idTemplate).isTrue();
    }
}
