package br.com.ericsson.smartnotification.enums;

public enum Project {
    RULES_ENGINE("rules-engine"),
    API_EVENT("api aventos"),
    API_OPTOUT("api OptOut"),
    API_TOKEN("api Token"),
    ROUTE("Route"),
    ENRICHMENT("Enriquecimento"),
    CAMPAIGNS("campanhas"),
    
    ;

    private String desc;
    
    private Project(String desc) {
        this.desc = desc;
    }
    
    public String getDesc() {
        return desc;
    }
}
