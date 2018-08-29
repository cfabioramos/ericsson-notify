package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document
public class Rule extends AbstractDocument implements Comparable<Rule>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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

    public Rule() {
        super();
        this.conditions = new ArrayList<>();
    }

    public Rule(String id, Boolean active, Integer eventType, Integer order, String description, String templateMessage,
            Date validityStart, Date validityEnd, boolean stopFlow) {
        super(id, active);
        this.eventType = eventType;
        this.order = order;
        this.templateMessage = templateMessage;
        this.validityStart = validityStart;
        this.validityEnd = validityEnd;
        this.stopFlow = stopFlow;
        this.description = description;
        this.conditions = new ArrayList<>();
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

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public boolean isStopFlow() {
        return stopFlow;
    }

    public void setStopFlow(boolean stopFlow) {
        this.stopFlow = stopFlow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Condition getCondition(String id) {
        for (Condition condition : this.conditions) {
            if (id.equals(condition.getId())) {
                return condition;
            }
        }
        return null;
    }

    public void setCondition(Condition condition) {
        for (int x = 0; x < this.conditions.size(); x++) {
            if (this.conditions.get(x).equals(condition)) {
                this.conditions.remove(x);
                this.conditions.add(x, condition);
                return;
            }
        }
        this.conditions.add(condition);
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }


    @Override
    public int compareTo(Rule o) {
        if(this.getOrder() < o.getOrder()) {
            return -1;
        }else if(this.getOrder() > o.getOrder()){
            return 1;
        }
        return 0;
    }
}
