package br.com.ericsson.smartnotification.rulesengine.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.ericsson.smartnotification.entities.TemplateMessage;

@Document
public class RuleTemplateMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idRule;
    
    private TemplateMessage templateMessage;

    public RuleTemplateMessage(String idRule, TemplateMessage templateMessage) {
        super();
        this.idRule = idRule;
        this.templateMessage = templateMessage;
    }

    public String getIdRule() {
        return idRule;
    }

    public void setIdRule(String idRule) {
        this.idRule = idRule;
    }

    public TemplateMessage getTemplateMessage() {
        return templateMessage;
    }

    public void setTemplateMessage(TemplateMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
    
}