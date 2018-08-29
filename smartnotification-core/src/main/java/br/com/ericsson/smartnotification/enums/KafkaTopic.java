package br.com.ericsson.smartnotification.enums;

public enum KafkaTopic {
    
    EVENT("event"),
    ENRICHMENT("enrichment"),
    SCHEDULER("scheduler"),
    ROUTE("route"),
    NOTIFICATION("notification"),
    NOTIFICATION_LOG("notification_log"),
    INTERFACE_LOG("interface_log");
	
    private String name;
    
    private KafkaTopic(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }

}
