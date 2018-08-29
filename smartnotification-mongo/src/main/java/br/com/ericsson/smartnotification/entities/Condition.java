package br.com.ericsson.smartnotification.entities;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.ericsson.smartnotification.enums.ConditionEnum;

public class Condition extends AgregationDocument{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String field;

    private String description;
    
    private ConditionEnum clause;

    private int numStart;

    private int numEnd;

    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateStart;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateEnd;
    
    private String value;
    
    public Condition() {
        super();
    }
    
    public Condition(String id, boolean active, String field, String description, ConditionEnum clause, String value) {
        super(id, active);
        this.field = field;
        this.clause = clause;
        this.description = description;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public ConditionEnum getClause() {
        return clause;
    }

    public void setClause(ConditionEnum clause) {
        this.clause = clause;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumStart() {
        return numStart;
    }

    public void setNumStart(int numStart) {
        this.numStart = numStart;
    }

    public int getNumEnd() {
        return numEnd;
    }

    public void setNumEnd(int numEnd) {
        this.numEnd = numEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

}
