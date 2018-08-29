package br.com.ericsson.smartnotification.domain.log;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LogMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @SerializedName("_doc")
    private LogDoc doc;

    public LogMapping() {
    }
    
    public LogMapping(LogDoc doc) {
        this.doc = doc;
    }

    public LogDoc getDoc() {
        return doc;
    }

    public void setDoc(LogDoc doc) {
        this.doc = doc;
    }
    
    

}
