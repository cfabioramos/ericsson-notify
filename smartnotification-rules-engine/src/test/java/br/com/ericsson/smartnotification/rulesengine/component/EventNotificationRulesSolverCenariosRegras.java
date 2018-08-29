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
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;
import br.com.ericsson.smartnotification.utils.DateUtil;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EventNotificationRulesSolverCenariosRegras {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopicBusiness;
    
    @Mock
    private TemplateMessageRepository templateMessageRepository;

    @InjectMocks
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;
    

    @Test
    public void evaluateTestNotificationsFor_RulesEmpty() throws ApplicationException {
        Event event = new Event(1, "123-Nenhuma regra configurada para o evento 1");
        event.setId("100000000123");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);
        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleVigenteAtiva() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "4-Unica regra configurada - Vigente - Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleVigenteAtivaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(3, "5-Unica regra configurada - Vigente - Ativa - Outro Evento");
        event.setId("1000000005");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleVigenteInativaEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(1, "6-Unica regra configurada - Vigente - Não Ativa - Mesmo Evento");
        event.setId("1000000006");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleVigenteInativaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(3, "7-Unica regra configurada - Vigente - Não Ativa - Outro Evento");
        event.setId("1000000007");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleNaoVigenteAtiva() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "8-Unica regra configurada - Não Vigente - Ativa - Mesmo Evento");
        event.setId("1000000008");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleNaoVigenteAtivaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(3, "9-Unica regra configurada - Não Vigente - Ativa - Outro Evento");
        event.setId("1000000009");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleNaoVigenteInativaEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(1, "10-Unica regra configurada - Não Vigente - Não Ativa - Mesmo Evento");
        event.setId("1000000010");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleNaoVigenteInativaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(3, "11-Unica regra configurada - Não Vigente - Não Ativa - Outro Evento");
        event.setId("1000000011");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesNaoVigenteAtiva() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "12-Mais que uma regra configurada - Nenhuma Vigente");
        event.setId("1000000012");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesUmaVigenteAtiva() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "F"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "13-Mais de uma regra configurada - Uma Vigente - Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000013");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesUmaVigenteAtivaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(2, "14-Mais de uma regra configurada - Uma Vigente - Ativa - Outro Evento");
        event.setId("1000000014");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "F"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesUmaVigenteInativa() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", false, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "15-Mais de uma regra configurada - Uma Vigente - Não Ativa - Mesmo Evento");
        event.setId("1000000015");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "F"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesUmaVigenteInativaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", false, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(2, "16-Mais de uma regra configurada - Uma Vigente - Não Ativa - Outro Evento");
        event.setId("1000000016");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "F"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesInativa() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", false, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", false, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "17-Mais de uma regra configurada - Mais de Uma Vigente - Não Ativa - Mesmo Evento");
        event.setId("1000000017");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "F"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesUmaAtivaMesmoEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", false, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "18-Mais de uma regra configurada - Mais de Uma Vigente - Uma Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000018");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesUmaAtivaOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", false, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(2, "19-Mais de uma regra configurada - Mais de Uma Vigente - Uma Ativa - Outro Evento");
        event.setId("1000000019");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesAtivasOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(2, "20-Mais de uma regra configurada - Mais de Uma Vigente - Mais de Uma Ativa - Outro Evento");
        event.setId("1000000020");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesAtivasUmaMesmoEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("3", true, 1, 0, "regra 3 order 2", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "21-Mais de uma regra configurada - Mais de Uma Vigente - Mais de Uma Ativa - Uma aplicavel Evento");
        event.setId("1000000021");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "B"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "12"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "F"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesAtivasOutroEventos() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "22-Mais de uma regra configurada - Mais de Uma Vigente - Mais de Uma Ativa - Outro Evento");
        event.setId("1000000022");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==2).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate3).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId()==idTemplate2).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigentesAtivasMesmoEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("3", true, 2, 2, "regra 3 order 2", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "23-Mais de uma regra configurada - Uma Vigente - Mais de Uma Ativa - Mesmo Evento");
        event.setId("1000000023");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate3).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RulesVigenteAtivasOutroEvento() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("3", true, 1, 2, "regra 3 order 2", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 1, 1, "regra 2 order 1", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-01-01"), true);
        rule.getConditions().add(new Condition("1", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "sexo", "sexo = M", ConditionEnum.EQUALS, "M"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(2, "24-Mais de uma regra configurada - Uma Vigente - Mais de Uma Ativa - Mesmo Evento");
        event.setId("1000000024");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("sexo", "sexo", FieldType.STRING, true, "M"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.STRING, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates).isEmpty();
    }
    
    @Test
    public void evaluateTestNotificationsFor_RuleVigenteAtivaValidityStartNull() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, null, DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "4-Unica regra configurada - Vigente - Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_RuleVigenteAtivaValidityEndNull() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), null, false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "4-Unica regra configurada - Vigente - Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }
    
    @Test
    public void evaluateTestNotificationsFor_RuleVigenteAtivaValidityStartNullValidityEndNull() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, null, null, false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "4-Unica regra configurada - Vigente - Ativa - Mesmo Evento => gera 1 template");
        event.setId("1000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId()==idTemplate).isTrue();
    }

}
