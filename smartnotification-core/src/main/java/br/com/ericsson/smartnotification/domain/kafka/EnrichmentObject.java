package br.com.ericsson.smartnotification.domain.kafka;

import java.time.LocalDateTime;

import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicObjectQueue;

public abstract class EnrichmentObject extends KafkaTopicObject implements KafkaTopicObjectQueue {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private TopicPriority priority;
    
    private String ruleId;

    private String templateId;
    
    private String origin;

    public EnrichmentObject() {
        this.origin = this.getClass().getSimpleName();
    }

    public EnrichmentObject(TopicPriority priority, String templateId) {
        this.priority = priority;
        this.ruleId = null;
        this.templateId = templateId;
        this.origin = this.getClass().getSimpleName();
    }

    public EnrichmentObject(TopicPriority priority, String ruleId, String templateId) {
        this.priority = priority;
        this.ruleId = ruleId;
        this.templateId = templateId;
        this.origin = this.getClass().getSimpleName();
    }

    public TopicPriority getPriority() {
        return priority;
    }

    public void setPriority(TopicPriority priority) {
        this.priority = priority;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public abstract LocalDateTime getDateTimeReceived();

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
    
    

}
