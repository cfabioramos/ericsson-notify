package br.com.ericsson.smartnotification.rulesengine.component;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.Condition;
import br.com.ericsson.smartnotification.enums.ConditionEnum;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.utils.DateUtil;

@RunWith(MockitoJUnitRunner.class)
public class ConditionsTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private RulesEngineKafkaEnrichmentTopicComponent rulesEngineKafkaEnrichmentTopic;

    @InjectMocks
    private EventNotificationRulesSolverComponent eventNotificationRulesSolver;

    @Test
    public void evaluateTestCondition() throws ApplicationException {    

        Condition conditionStringNomeEqualsJose = new Condition("1", true, "nome", "string nome = JOSÉ", ConditionEnum.EQUALS, "JOSE");
        Condition conditionStringNomeNotEqualsJose = new Condition("1", true, "nome", "string nome = JOSÉ", ConditionEnum.NOT_EQUALS, "JOSE");
        
        EventField eventFieldStringNomeJose = new EventField("nome", "Nome do cliente", FieldType.STRING, true, "JOSE");
        EventField eventFieldStringNomeMaria = new EventField("nome", "Nome do cliente", FieldType.STRING, true, "MARIA");

        Condition conditionNumberDddEquals11 = new Condition("1", true, "ddd", "somente para o ddd 11", ConditionEnum.EQUALS, "11");
        Condition conditionNumberDddNotEquals11 = new Condition("1", true, "ddd", "somente para o ddd 11", ConditionEnum.NOT_EQUALS, "11");
        
        EventField eventFieldNumberDdd11 = new EventField("ddd", "DDD do cliente", FieldType.NUMBER, true, "11");
        EventField eventFieldNumberDdd12 = new EventField("ddd", "DDD do cliente", FieldType.NUMBER, true, "12");

        Condition conditionNumberIdadeGreater18 = new Condition("1", true, "idade", "somente para o idade > 18", ConditionEnum.GREATER_THAN, "18");
        Condition conditionNumberIdadeLess20 = new Condition("1", true, "idade", "somente para o idade < 20", ConditionEnum.LESS_THAN, "20");
        
        EventField eventFieldNumberIdade20 = new EventField("idade", "Idade do cliente", FieldType.NUMBER, true, "20");
        EventField eventFieldNumberIdade16 = new EventField("idade", "Idade do cliente", FieldType.NUMBER, true, "16");

        EventField eventFieldNumericIdade25 = new EventField("idade", "Idade do cliente", FieldType.NUMBER, true, "25");

        Condition conditionNumberIdadeBetween18e65 = new Condition("1", true, "idade", "somente idades entre 18 e 65", ConditionEnum.BETWEEN, "");
        conditionNumberIdadeBetween18e65.setNumEnd(18);
        conditionNumberIdadeBetween18e65.setNumEnd(65);

        EventField eventFieldDateVencimento2018_07_05 = new EventField("vencimento", "Data de vencimento", FieldType.DATE, true, "2018-07-05");
        EventField eventFieldDateVencimento2018_07_06 = new EventField("vencimento", "Data de vencimento", FieldType.DATE, true, "2018-07-06");
        Condition conditionDateEquals2018_07_05 = new Condition("1", true, "vencimento", "somente vencimento = 2018-07-05", ConditionEnum.EQUALS, "2018-07-05");
        Condition conditionDateNotEquals2018_07_05 = new Condition("1", true, "vencimento", "somente vencimento = 2018-07-05", ConditionEnum.NOT_EQUALS, "2018-07-05");
        Condition conditionDateGreaterThan2018_07_05 = new Condition("1", true, "vencimento", "somente vencimento > 2018-07-05", ConditionEnum.GREATER_THAN, "2018-07-05");
        Condition conditionDateLessThan2018_07_06 = new Condition("1", true, "vencimento", "somente vencimento < 2018-07-06", ConditionEnum.LESS_THAN, "2018-07-06");

        Condition conditionDateGreaterAnd2018_07_06 = new Condition("1", true, "vencimento", "somente vencimento >= 2018-07-06", ConditionEnum.GREATER_AND, "2018-07-06");
        Condition conditionDateLessAnd2018_07_06 = new Condition("1", true, "vencimento", "somente vencimento <= 2018-07-06", ConditionEnum.LESS_AND, "2018-07-06");

        Condition conditionDateVencimentoBetween2018_07_05e2018_07_10 = new Condition("1", true, "vencimento", "somente vencimento entre 2018-07-05 e 2018-07-10", ConditionEnum.BETWEEN, "");
        conditionDateVencimentoBetween2018_07_05e2018_07_10.setDateStart(DateUtil.parseToDate("2018-07-05"));
        conditionDateVencimentoBetween2018_07_05e2018_07_10.setDateEnd(DateUtil.parseToDate("2018-07-10"));

        assertThat(eventNotificationRulesSolver.testCondition(conditionStringNomeEqualsJose, eventFieldStringNomeJose)).isTrue();               // Test #2
        assertThat(eventNotificationRulesSolver.testCondition(conditionStringNomeNotEqualsJose, eventFieldStringNomeMaria)).isTrue();           // Test #3
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberDddEquals11, eventFieldNumberDdd11)).isTrue();                     // Test #4
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberDddNotEquals11, eventFieldNumberDdd12)).isTrue();                  // Test #5
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberIdadeGreater18, eventFieldNumberIdade20)).isTrue();                // Test #6
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberIdadeLess20, eventFieldNumberIdade16)).isTrue();                   // Test #7
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberIdadeBetween18e65, eventFieldNumericIdade25)).isTrue();            // Test #8
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateEquals2018_07_05, eventFieldDateVencimento2018_07_05)).isTrue();     // Test #9
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateNotEquals2018_07_05, eventFieldDateVencimento2018_07_06)).isTrue();  // Test #10
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateGreaterThan2018_07_05, eventFieldDateVencimento2018_07_06)).isTrue();    // Test #11
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateLessThan2018_07_06, eventFieldDateVencimento2018_07_05)).isTrue();       // Test #12

        assertThat(eventNotificationRulesSolver.testCondition(conditionDateGreaterAnd2018_07_06, eventFieldDateVencimento2018_07_06)).isTrue();    // Test #11
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateLessAnd2018_07_06, eventFieldDateVencimento2018_07_06)).isTrue();       // Test #12

        assertThat(eventNotificationRulesSolver.testCondition(conditionDateVencimentoBetween2018_07_05e2018_07_10, eventFieldDateVencimento2018_07_06)).isTrue(); // Test #13

    
        assertThat(eventNotificationRulesSolver.testCondition(conditionStringNomeEqualsJose, eventFieldStringNomeMaria)).isFalse();          // Test #3
        assertThat(eventNotificationRulesSolver.testCondition(conditionNumberDddEquals11, eventFieldNumberDdd12)).isFalse();                 // Test #5
        assertThat(eventNotificationRulesSolver.testCondition(conditionDateEquals2018_07_05, eventFieldDateVencimento2018_07_06)).isFalse(); // Test #10
    }
  
}
