package br.com.ericsson.smartnotification.rulesengine.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
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
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.ConditionEnum;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;
import br.com.ericsson.smartnotification.utils.DateUtil;

@RunWith(MockitoJUnitRunner.class)
public class EventNotificationRulesSolverTest {

    @Mock
    private RuleRepository ruleRepository;
    
    @Mock
    private TemplateMessageRepository templateMessageRepo;

    @Mock
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopicBusiness;

    @InjectMocks
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;

    @Before
    public void init() {
    	for(int id = 12345; id < 12350; id++) {
    		when(templateMessageRepo.findById(String.valueOf(id))).thenReturn(geTemplateMessageActive(String.valueOf(id)));
    	}
    	
    }
    
    @Test
    public void evaluateTestNotificationsFor_RulesEmpty() throws ApplicationException {
        Event event = new Event(1, "evento 1");
        event.setId("4000000001");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);
        assertEquals(0,idTemplates.size());
    }

    @Test
    public void evaluateTestNotificationsFor_StringEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "2-Configurado Campo Texto igual");
        event.setId("4000000002");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_StringNotEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "3-Configurado Campo Texto diferente");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.setId("4000000003");

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "3-Configurado Campo Numérico igual");
        event.setId("4000000004");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.NUMBER, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberNotEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd <> 11", ConditionEnum.NOT_EQUALS, "11"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "5-Configurado Campo Numérico diferente");
        event.setId("4000000005");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.NUMBER, true, "12"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberGreater() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade > 18", ConditionEnum.GREATER_THAN, "18"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade > 20", ConditionEnum.GREATER_THAN, "20"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade >= 20", ConditionEnum.GREATER_AND, "20"));
        rules.add(rule);

        String idTemplate4 = "12348";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate4, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade >= 10", ConditionEnum.GREATER_AND, "10"));
        rules.add(rule);

        String idTemplate5 = "12349";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate5, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade >= 30", ConditionEnum.GREATER_AND, "30"));
        rules.add(rule);

        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "6-Configurado Campo Numérico maior que (testar também limiar) (testar também limiar)");
        event.setId("4000000006");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "20"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertTrue(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate));
        assertTrue(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate3));
        assertTrue(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate4));
    }

    @Test
    public void evaluateTestNotificationsFor_NumberLess() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade < 20", ConditionEnum.LESS_THAN, "20"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade < 16", ConditionEnum.LESS_THAN, "16"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade <= 16", ConditionEnum.LESS_AND, "16"));
        rules.add(rule);

        String idTemplate4 = "12348";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate4, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade <= 20", ConditionEnum.LESS_AND, "20"));
        rules.add(rule);

        String idTemplate5 = "12349";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate5, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade <= 10", ConditionEnum.LESS_AND, "10"));
        rules.add(rule);

        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "7-Configurado Campo Numérico menor que (testar também limiar)");
        event.setId("4000000007");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "16"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate3)).isTrue();
        assertThat(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate4)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberBetween() throws ApplicationException {
        Condition conditionNumberBetween = new Condition("1", true, "idade", "idades entre 18 e 65", ConditionEnum.BETWEEN, "");
        conditionNumberBetween.setNumStart(18);
        conditionNumberBetween.setNumEnd(65);
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetween);
        rules.add(rule);

        Condition conditionNumberBetweenStart = new Condition("1", true, "idade", "idades entre 25 e 26", ConditionEnum.BETWEEN, "");
        conditionNumberBetweenStart.setNumStart(25);
        conditionNumberBetweenStart.setNumEnd(26);
        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetweenStart);
        rules.add(rule);
        
        Condition conditionNumberBetweenEnd = new Condition("1", true, "idade", "idades entre 20 e 25", ConditionEnum.BETWEEN, "");
        conditionNumberBetweenEnd.setNumStart(20);
        conditionNumberBetweenEnd.setNumEnd(25);
        String idTemplate3 = "12347";
        rule = new Rule("3", true, 2, 0, "regra 3 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetweenEnd);
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "8-Configurado Campo Numérico valor entre (testar também limiares)");
        event.setId("4000000008");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "25"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate2)).isTrue();
        assertThat(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate3)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "aniversario", "aniversariantes = 2018-07-05", ConditionEnum.EQUALS, "2018-07-05"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "9-Configurado Campo Data igual");
        event.setId("4000000009");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("aniversario", "aniversario", FieldType.DATE, true, "2018-07-05"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateNotEquals() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <> 2018-07-05", ConditionEnum.NOT_EQUALS, "2018-07-05"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "10-Configurado Campo Data diferente");
        event.setId("4000000010");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "Vencimento", FieldType.DATE, true, "2018-07-06"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateGreater() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento > 2018-07-01", ConditionEnum.GREATER_THAN, "2018-07-01"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento > 2018-07-06", ConditionEnum.GREATER_THAN, "2018-07-06"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento >= 2018-07-06", ConditionEnum.GREATER_AND, "2018-07-06"));
        rules.add(rule);

        String idTemplate4 = "12348";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate4, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento >= 2018-07-01", ConditionEnum.GREATER_AND, "2018-07-01"));
        rules.add(rule);

        String idTemplate5 = "12349";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate5, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento >= 2018-07-20", ConditionEnum.GREATER_AND, "2018-07-20"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "11-Configurado Campo Data maior que (testar também limiar) (testar também limiar)");
        event.setId("4000000011");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-07-06"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate3)).isTrue();
        assertThat(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate4)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateLess() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento < 2018-07-06", ConditionEnum.LESS_THAN, "2018-07-06"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento < 2018-07-01", ConditionEnum.LESS_THAN, "2018-07-01"));
        rules.add(rule);

        String idTemplate3 = "12347";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <= 2018-07-01", ConditionEnum.LESS_AND, "2018-07-01"));
        rules.add(rule);

        String idTemplate4 = "12348";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate4, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <= 2018-07-07", ConditionEnum.LESS_AND, "2018-07-07"));
        rules.add(rule);
        
        String idTemplate5 = "12348";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate5, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <= 2018-01-01", ConditionEnum.LESS_AND, "2018-01-01"));
        rules.add(rule);

        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "12-Configurado Campo Data menor que (testar também limiar)");
        event.setId("4000000012");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-07-01"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate3)).isTrue();
        assertThat(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate4)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateBetween() throws ApplicationException {
        Condition conditionDateBetween = new Condition("1", true, "vencimento", "vencimento entre 2018-07-05 e 2018-07-10", ConditionEnum.BETWEEN, "");
        conditionDateBetween.setDateStart(DateUtil.parseToDate("2018-07-05"));
        conditionDateBetween.setDateEnd(DateUtil.parseToDate("2018-07-10"));
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetween);
        rules.add(rule);

        Condition conditionDateBetweenStart = new Condition("1", true, "vencimento", "vencimento entre 2018-07-06 e 2018-07-10", ConditionEnum.BETWEEN, "");
        conditionDateBetweenStart.setDateStart(DateUtil.parseToDate("2018-07-06"));
        conditionDateBetweenStart.setDateEnd(DateUtil.parseToDate("2018-07-10"));
        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetweenStart);
        rules.add(rule);

        Condition conditionDateBetweenEnd = new Condition("1", true, "vencimento", "vencimento entre 2018-07-05 e 2018-07-06", ConditionEnum.BETWEEN, "");
        conditionDateBetweenEnd.setDateStart(DateUtil.parseToDate("2018-07-05"));
        conditionDateBetweenEnd.setDateEnd(DateUtil.parseToDate("2018-07-06"));
        String idTemplate3 = "12347";
        rule = new Rule("3", true, 2, 0, "regra 3 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetweenEnd);
        rules.add(rule);

        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "13-Configurado Campo Data valor entre (testar também limiares)");
        event.setId("4000000013");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-07-06"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==3).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
        assertThat(idTemplates.get(1).getTemplateMessage().getId().equals(idTemplate2)).isTrue();
        assertThat(idTemplates.get(2).getTemplateMessage().getId().equals(idTemplate3)).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_StringEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "14-Configurado Campo Texto igual");
        event.setId("4000000014");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_StringNotEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "nome", "nome <> JOSE", ConditionEnum.NOT_EQUALS, "JOSE"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "15-Configurado Campo Texto diferente");
        event.setId("4000000015");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd = 11", ConditionEnum.EQUALS, "11"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "16-Configurado Campo Numérico igual");
        event.setId("4000000016");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.NUMBER, true, "12"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberNotEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "ddd", "ddd <> 11", ConditionEnum.NOT_EQUALS, "11"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "17-Configurado Campo Numérico diferente");
        event.setId("4000000017");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("ddd", "ddd", FieldType.NUMBER, true, "11"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberGreaterNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade > 18", ConditionEnum.GREATER_THAN, "18"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade >= 20", ConditionEnum.GREATER_THAN, "20"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "18-Configurado Campo Numérico maior que (testar também limiar) (testar também limiar)");
        event.setId("4000000018");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "16"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberLessNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade < 20", ConditionEnum.LESS_THAN, "20"));
        rules.add(rule);

        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "idade", "idade <= 16", ConditionEnum.LESS_THAN, "16"));
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "19-Configurado Campo Numérico menor que (testar também limiar)");
        event.setId("4000000019");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "26"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_NumberBetweenNotEvent() throws ApplicationException {
        Condition conditionNumberBetween = new Condition("1", true, "idade", "idades entre 18 e 65", ConditionEnum.BETWEEN, "");
        conditionNumberBetween.setNumStart(18);
        conditionNumberBetween.setNumEnd(65);
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetween);
        rules.add(rule);

        Condition conditionNumberBetweenStart = new Condition("1", true, "idade", "idades entre 25 e 26", ConditionEnum.BETWEEN, "");
        conditionNumberBetweenStart.setNumStart(25);
        conditionNumberBetweenStart.setNumEnd(26);
        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetweenStart);
        rules.add(rule);
        
        Condition conditionNumberBetweenEnd = new Condition("1", true, "idade", "idades entre 20 e 25", ConditionEnum.BETWEEN, "");
        conditionNumberBetweenEnd.setNumStart(20);
        conditionNumberBetweenEnd.setNumEnd(25);
        String idTemplate3 = "12347";
        rule = new Rule("3", true, 2, 0, "regra 3 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionNumberBetweenEnd);
        rules.add(rule);
        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "20-Configurado Campo Numérico valor entre (testar também limiares)");
        event.setId("4000000020");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("idade", "idade", FieldType.NUMBER, true, "15"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "aniversario", "aniversariantes = 2018-07-05", ConditionEnum.EQUALS, "2018-07-05"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "21-Configurado Campo Data igual");
        event.setId("4000000021");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("aniversario", "aniversario", FieldType.DATE, true, "2018-07-06"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateNotEqualsNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), true);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <> 2018-07-05", ConditionEnum.NOT_EQUALS, "2018-07-05"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "22-Configurado Campo Data diferente");
        event.setId("4000000022");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "Vencimento", FieldType.DATE, true, "2018-07-05"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateGreaterNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento > 2018-07-01", ConditionEnum.GREATER_THAN, "2018-07-01"));
        rules.add(rule);

        String idTemplate2 = "12345";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento >= 2018-07-06", ConditionEnum.GREATER_THAN, "2018-07-06"));
        rules.add(rule);
        
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "23-Configurado Campo Data maior que (testar também limiar) (testar também limiar)");
        event.setId("4000000023");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-06-10"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateLessNotEvent() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento < 2018-07-06", ConditionEnum.LESS_THAN, "2018-07-06"));
        rules.add(rule);

        String idTemplate2 = "12345";
        rule = new Rule("2", true, 1, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "vencimento", "vencimento <= 2018-07-01", ConditionEnum.LESS_THAN, "2018-07-01"));
        rules.add(rule);
        
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "12-Configurado Campo Data menor que (testar também limiar)");
        event.setId("4000000024");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-07-07"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_DateBetweenNotEvent() throws ApplicationException {
        Condition conditionDateBetween = new Condition("1", true, "vencimento", "vencimento entre 2018-07-05 e 2018-07-10", ConditionEnum.BETWEEN, "");
        conditionDateBetween.setDateStart(DateUtil.parseToDate("2018-07-05"));
        conditionDateBetween.setDateEnd(DateUtil.parseToDate("2018-07-10"));
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 2, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetween);
        rules.add(rule);

        Condition conditionDateBetweenStart = new Condition("1", true, "vencimento", "vencimento entre 2018-07-06 e 2018-07-10", ConditionEnum.BETWEEN, "");
        conditionDateBetweenStart.setDateStart(DateUtil.parseToDate("2018-07-06"));
        conditionDateBetweenStart.setDateEnd(DateUtil.parseToDate("2018-07-10"));
        String idTemplate2 = "12346";
        rule = new Rule("2", true, 2, 0, "regra 2 order 0", idTemplate2, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetweenStart);
        rules.add(rule);

        Condition conditionDateBetweenEnd = new Condition("1", true, "vencimento", "vencimento entre 2018-07-05 e 2018-07-06", ConditionEnum.BETWEEN, "");
        conditionDateBetweenEnd.setDateStart(DateUtil.parseToDate("2018-07-05"));
        conditionDateBetweenEnd.setDateEnd(DateUtil.parseToDate("2018-07-06"));
        String idTemplate3 = "12347";
        rule = new Rule("3", true, 2, 0, "regra 3 order 0", idTemplate3, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(conditionDateBetweenEnd);
        rules.add(rule);

        when(ruleRepository.findByEventType(2)).thenReturn(rules);

        Event event = new Event(2, "13-Configurado Campo Data valor entre (testar também limiares)");
        event.setId("4000000025");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("vencimento", "vencimento", FieldType.DATE, true, "2018-07-01"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }
    
    @Test
    public void evaluateTestNotificationsFor_Rule2Conditions1True2False() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "26-Configurado dois campos só o primeiro satisfaz");
        event.setId("4000000026");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "S"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule2Conditions1False2True() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "27-Configurado dois campos só o primeiro satisfaz");
        event.setId("4000000027");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "C"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1True2False3False() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "28-Configurado três campos só o primeiro satisfaz  ");
        event.setId("4000000028");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "S"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "B"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1False2True3False() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "29-Configurado três campos só o segundo satisfaz ");
        event.setId("4000000029");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "C"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "B"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }


    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1False2False3True() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "30-Configurado três campos só o terceiro satisfaz ");
        event.setId("4000000030");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "S"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1True2True3False() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "31-Configurado três campos só primeiro e segundo satisfazem");
        event.setId("4000000031");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "C"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "B"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }


    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1True2False3True() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "32-Configurado três campos só primeiro e terceiro satisfazem");
        event.setId("4000000032");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "S"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1False2True3True() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "33-Configurado três campos só segundo e terceiro satisfazem");
        event.setId("4000000033");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "C"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1False2False3False() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "34-Configurado três campos nenhum satisfaz");
        event.setId("4000000034");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "MARIA"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "S"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "B"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==0).isTrue();
    }

    @Test
    public void evaluateTestNotificationsFor_Rule3Conditions1True2True3True() throws ApplicationException {
        String idTemplate = "12345";
        List<Rule> rules= new ArrayList<Rule>();
        Rule rule = new Rule("1", true, 1, 0, "regra 1 order 0", idTemplate, DateUtil.parseToDate("2018-01-01"), DateUtil.parseToDate("2018-12-31"), false);
        rule.getConditions().add(new Condition("1", true, "nome", "nome = JOSE", ConditionEnum.EQUALS, "JOSE"));
        rule.getConditions().add(new Condition("2", true, "estadocivil", "estadocivil = C", ConditionEnum.EQUALS, "C"));
        rule.getConditions().add(new Condition("3", true, "plano", "plano = A", ConditionEnum.EQUALS, "A"));
        rules.add(rule);
        when(ruleRepository.findByEventType(1)).thenReturn(rules);

        Event event = new Event(1, "35-Configurado três campos nenhum satisfaz");
        event.setId("4000000035");
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_MSISDN.getName(), "numero assinante", FieldType.STRING, true, "5511993399966"));
        event.getFields().add(new EventField(EventDefinitionField.EVENT_FIELD_NTYPE.getName(), "tipo do evento", FieldType.NUMBER, true, "1"));
        event.getFields().add(new EventField("nome", "assinante", FieldType.STRING, true, "JOSE"));
        event.getFields().add(new EventField("estadocivil", "Estado Civil", FieldType.STRING, true, "C"));
        event.getFields().add(new EventField("plano", "plano", FieldType.STRING, true, "A"));

        List<RuleTemplateMessage> idTemplates = eventNotificationRulesSolver.notificationsFor(event);

        assertThat(idTemplates.size()==1).isTrue();
        assertThat(idTemplates.get(0).getTemplateMessage().getId().equals(idTemplate)).isTrue();
    }
    
    private Optional<TemplateMessage> geTemplateMessageActive(String id) {
    	return Optional.of(new TemplateMessage(id, true, TopicPriority.MEDIUM));
    }
    
}
