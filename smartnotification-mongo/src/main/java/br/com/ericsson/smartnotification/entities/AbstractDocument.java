package br.com.ericsson.smartnotification.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import br.com.ericsson.smartnotification.utils.JsonUtil;

public class AbstractDocument implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Boolean active;
    
    private Boolean excluded;
    
    public AbstractDocument() {
        this.active = true;
    }

    public AbstractDocument(String id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean isExcluded() {
        return excluded != null ? excluded : false;
    }

    public void setExcluded(boolean excluded) {
        this.excluded = excluded;
    }
    
    
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof AbstractDocument) ) return false;

        final AbstractDocument otherOocument = (AbstractDocument) other;
        
        if(otherOocument.getId() == null || this.getId() == null) return false;
        
        
        if (!otherOocument.getId().equals( this.getId())) return false;
        
        return this.getClass().getSimpleName().equalsIgnoreCase(otherOocument.getClass().getSimpleName());
        
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return JsonUtil.parseToJsonString(this);
    }
    
}
