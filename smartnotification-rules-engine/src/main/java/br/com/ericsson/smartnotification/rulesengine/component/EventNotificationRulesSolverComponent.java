package br.com.ericsson.smartnotification.rulesengine.component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.entities.Condition;
import br.com.ericsson.smartnotification.entities.Rule;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.ConditionEnum;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;
import br.com.ericsson.smartnotification.rulesengine.business.RulesEngineBusiness;
import br.com.ericsson.smartnotification.rulesengine.domain.RuleTemplateMessage;
import br.com.ericsson.smartnotification.utils.DateUtil;

@Service
public class EventNotificationRulesSolverComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RulesEngineBusiness.class);

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TemplateMessageRepository templateMessageRepository;

    public List<RuleTemplateMessage> notificationsFor(Event event) throws ApplicationException {

        LOG.info("IdEvento {} - Recuperando evento da fila de triagem: evento: {}", event.getId(), event);

        LOG.info("IdEvento {} - Obtendo regras para eventType: {}", event.getId(), event.getEventType());
        List<Rule> eventRules = obtemEventRules(event.getEventType());

        List<RuleTemplateMessage> ruleTemplates = new ArrayList<>();

        for (Rule rule : eventRules) {
            if (isRuleValid(event.getId(), rule)) {
                LOG.info("IdEvento {} - Regra Ativa e Vigente: {}", event.getId(), rule);
                LOG.info("IdEvento {} - Validando Condições: {}", event.getId(), rule);
                if (mustNotify(event, rule)) {

                    LOG.info("IdEvento {} - Regra {} valida", event.getId(), rule.getId());

                    LOG.info("IdEvento {} - buscando template {} da regra {}", event.getId(), rule.getTemplateMessage(),
                            rule.getId());

                    this.geTemplateMessageActive(event, rule, ruleTemplates);

                    if (rule.isStopFlow()) {
                        LOG.info("IdEvento {} - Regra com StopFlow, interrompendo o tratamento das demais regras: {}",
                                event.getId(), rule);
                        break;
                    }
                } else {
                    LOG.info("IdEvento {} - Regra invalida, template nao adicionado.", event.getId());
                }
            }
        }

        return ruleTemplates;

    }

    private void geTemplateMessageActive(Event event, Rule rule, List<RuleTemplateMessage> ruleTemplates) {
        Optional<TemplateMessage> optional = templateMessageRepository.findById(rule.getTemplateMessage());
        if (optional.isPresent()) {
            if (optional.get().isActive()) {
                ruleTemplates.add(new RuleTemplateMessage(rule.getId(), optional.get()));
            } else {
                LOG.info("IdEvento {} - Template {} inativo. Descartando regra {}", event.getId(),
                        optional.get().getId(), rule.getId());
            }
        } else {
            LOG.info("IdEvento {} - Template {} não encontraado. Descartando regra {}", event.getId(),
                    rule.getTemplateMessage(), rule.getId());
        }
    }

    protected List<Rule> obtemEventRules(Integer eventType) {
        List<Rule> rules = ruleRepository.findByEventType(eventType);
        Collections.sort(rules);
        return rules;
    }

    protected boolean mustNotify(Event event, Rule rule) throws ApplicationException {
        boolean isRuleValid = true;
        for (Condition condition : rule.getConditions()) {
            EventField eventField = event.getField(condition.getField());
            if (eventField == null)
                throw new ApplicationException(String.format("campo %s inexistente no evento", condition.getField()));

            isRuleValid = testCondition(condition, eventField);

            if (!isRuleValid) {
                LOG.info("IdEvento {} - Condição Invalida, campo{} - condição: {}", event.getId(), eventField,
                        condition);
                break;
            } else {
                LOG.info("IdEvento {} - Condição Valida, campo{} - condição: {}", event.getId(), eventField, condition);
            }
        }
        return isRuleValid;
    }

    private boolean isRuleValid(String eventId, Rule rule) throws ApplicationException {
        boolean isRuleValid = rule.isActive();
        if (!isRuleValid) {
            LOG.info("IdEvento {} - Regra inativa: {}", eventId, rule);
        } else {
            try {
                DateFormat cleanDate = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNow = cleanDate.parse(cleanDate.format(new Date()));
                isRuleValid = (rule.getValidityStart() == null || dateNow.after(rule.getValidityStart())
                        || dateNow.equals(rule.getValidityStart()))
                        && (rule.getValidityEnd() == null || dateNow.before(rule.getValidityEnd())
                                || dateNow.equals(rule.getValidityEnd()));
            } catch (ParseException e) {
                throw new ApplicationException(e.getMessage());
            }

            if (!isRuleValid) {
                LOG.info("IdEvento {} - Regra Não Vigente: {}", eventId, rule);
            }
        }
        return isRuleValid;
    }

    boolean testCondition(Condition condition, EventField eventField) throws ApplicationException {
        ConditionEnum conditionEnum = condition.getClause();
        if (ConditionEnum.EQUALS.equals(conditionEnum)) {
            return testEquals(condition, eventField);
        } else if (ConditionEnum.NOT_EQUALS.equals(conditionEnum)) {
            return testNotEquals(condition, eventField);
        } else if (ConditionEnum.BETWEEN.equals(conditionEnum)) {
            return testBetween(condition, eventField);
        } else if (ConditionEnum.GREATER_AND.equals(conditionEnum)) {
            return testGreaterAnd(condition, eventField);
        } else if (ConditionEnum.LESS_AND.equals(conditionEnum)) {
            return testLessAnd(condition, eventField);
        } else if (ConditionEnum.GREATER_THAN.equals(conditionEnum)) {
            return testGreaterThan(condition, eventField);
        } else if (ConditionEnum.LESS_THAN.equals(conditionEnum)) {
            return testLessThan(condition, eventField);
        }
        return false;
    }

    private boolean testEquals(Condition condidion, EventField eventField) {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return eventField.getValue().equals(condidion.getValue());
        } else {
            return Integer.parseInt(eventField.getValue().toString()) == Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testNotEquals(Condition condidion, EventField eventField) {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return !eventField.getValue().equals(condidion.getValue());
        } else {
            return Integer.parseInt(eventField.getValue().toString()) != Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testGreaterAnd(Condition condidion, EventField eventField) throws ApplicationException {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return DateUtil.parseToDate(eventField.getValue().toString())
                    .after(DateUtil.parseToDate(condidion.getValue()))
                    || DateUtil.parseToDate(eventField.getValue().toString())
                            .equals(DateUtil.parseToDate(condidion.getValue()));
        } else {
            return Integer.parseInt(eventField.getValue().toString()) > Integer.parseInt(condidion.getValue())
                    || Integer.parseInt(eventField.getValue().toString()) == Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testLessAnd(Condition condidion, EventField eventField) throws ApplicationException {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return DateUtil.parseToDate(eventField.getValue().toString())
                    .before(DateUtil.parseToDate(condidion.getValue()))
                    || DateUtil.parseToDate(eventField.getValue().toString())
                            .equals(DateUtil.parseToDate(condidion.getValue()));
        } else {
            return Integer.parseInt(eventField.getValue().toString()) < Integer.parseInt(condidion.getValue())
                    || Integer.parseInt(eventField.getValue().toString()) == Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testGreaterThan(Condition condidion, EventField eventField) throws ApplicationException {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return DateUtil.parseToDate(eventField.getValue().toString())
                    .after(DateUtil.parseToDate(condidion.getValue()));
        } else {
            return Integer.parseInt(eventField.getValue().toString()) > Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testLessThan(Condition condidion, EventField eventField) throws ApplicationException {
        if (eventField.getType().equals(FieldType.STRING) || eventField.getType().equals(FieldType.DATE)
                || eventField.getType().equals(FieldType.DATETIME)) {
            return DateUtil.parseToDate(eventField.getValue().toString())
                    .before(DateUtil.parseToDate(condidion.getValue()));
        } else {
            return Integer.parseInt(eventField.getValue().toString()) < Integer.parseInt(condidion.getValue());
        }
    }

    private boolean testBetween(Condition condidion, EventField eventField) throws ApplicationException {
        if (eventField.getType().equals(FieldType.DATE) || eventField.getType().equals(FieldType.DATETIME)) {
            return (DateUtil.parseToDate(eventField.getValue().toString()).after(condidion.getDateStart())
                    || DateUtil.parseToDate(eventField.getValue().toString()).equals(condidion.getDateStart()))
                    && (DateUtil.parseToDate(eventField.getValue().toString()).before(condidion.getDateEnd())
                            || (DateUtil.parseToDate(eventField.getValue().toString()).equals(condidion.getDateEnd())));
        } else {
            return Integer.parseInt(eventField.getValue().toString()) >= condidion.getNumStart()
                    && Integer.parseInt(eventField.getValue().toString()) <= condidion.getNumEnd();
        }
    }

}
