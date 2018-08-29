package br.com.ericsson.smartnotification.domain.log;

import br.com.ericsson.smartnotification.domain.kafka.KafkaTopicObject;

public class LogObject extends KafkaTopicObject {

    private static final long serialVersionUID = 1L;

    private LogMapping mappings;

    public LogObject() {
    }
    
    public LogObject(LogProperties logProperties) {
        this.mappings = new LogMapping(new LogDoc(logProperties));
    }

    public LogMapping getMappings() {
        return mappings;
    }

    public void setMappings(LogMapping mappings) {
        this.mappings = mappings;
    }

}
