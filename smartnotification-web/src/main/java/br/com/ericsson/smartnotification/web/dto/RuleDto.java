package br.com.ericsson.smartnotification.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.ericsson.smartnotification.entities.Condition;
import br.com.ericsson.smartnotification.entities.Rule;

public class RuleDto extends AbstractDto<Rule, RuleDto> implements Comparable<RuleDto>{

    private Integer eventType;
    
    private Integer order;
    
    private String description;
    
    private String templateMessage;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validityStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validityEnd;
    
    private boolean stopFlow;

    private List<Condition> conditions;
    
    
    
    public RuleDto(String id, boolean active, Integer eventType, Integer order, String description, String templateMessage, Date validityStart, Date validityEnd,
            boolean stopFlow) {
        super(id, active);
        this.eventType = eventType;
        this.order = order;
        this.description = description;
        this.templateMessage = templateMessage;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
        this.stopFlow = stopFlow;
        this.conditions = new ArrayList<>();
    }


    public RuleDto() {
        super(null, true);
        this.conditions = new ArrayList<>();
    }
    
    
    @Override
    protected Rule getDocument() {
        return new Rule();
    }


    public Integer getEventType() {
        return eventType;
    }


    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }


    public Integer getOrder() {
        return order;
    }


    public void setOrder(Integer order) {
        this.order = order;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getTemplateMessage() {
        return templateMessage;
    }


    public void setTemplateMessage(String templateMessage) {
        this.templateMessage = templateMessage;
    }


    public Date getValidityStart() {
        return validityStart;
    }


    public void setValidityStart(Date validityStart) {
        this.validityStart = validityStart;
    }


    public Date getValidityEnd() {
        return validityEnd;
    }


    public void setValidityEnd(Date validityEnd) {
        this.validityEnd = validityEnd;
    }


    public boolean isStopFlow() {
        return stopFlow;
    }


    public void setStopFlow(boolean stopFlow) {
        this.stopFlow = stopFlow;
    }


    public List<Condition> getConditions() {
        return conditions;
    }


    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public Condition getCondition(String idCondition) {
        for(Condition condition : conditions) {
            if(condition.getId().equals(idCondition)) {
                return condition;
            }
        }
        return null;
    }
    
    public void setCondition(Condition condition) {
        for(int x = 0 ; x < conditions.size() ; x++) {
            if(conditions.get(x).equals(condition)) {
                conditions.remove(x);
                conditions.add(x, condition);
                return;
            }
        }
        conditions.add(condition);
    }


    @Override
    public int compareTo(RuleDto o) {
        if(this.getOrder() < o.getOrder()) {
            return -1;
        }else if(this.getOrder() > o.getOrder()){
            return 1;
        }
        return 0;
    }
    

}
