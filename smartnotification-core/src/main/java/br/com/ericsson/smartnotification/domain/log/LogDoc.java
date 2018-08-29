package br.com.ericsson.smartnotification.domain.log;

import java.io.Serializable;

public class LogDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    private LogProperties properties;

    public LogDoc() {
    }

    public LogDoc(LogProperties properties) {
        this.properties = properties;
    }

    public LogProperties getProperties() {
        return properties;
    }

    public void setProperties(LogProperties properties) {
        this.properties = properties;
    }

}
