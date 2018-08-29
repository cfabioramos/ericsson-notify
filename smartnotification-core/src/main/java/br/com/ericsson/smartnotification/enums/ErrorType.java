package br.com.ericsson.smartnotification.enums;

public enum ErrorType {
    OPTOUT("Descartando evento para msisdn por OptOut"),
    OPTOUT_CAMPAIGN("Descartando notificação para msisdn por OptOut"),
    RULE("Descartando evento na aplicação de regras"),
    ENRICHEMENT("Descartando evento por erro no enriquecimento"),
    
    ESCHEDULED("Descartando mensagem no agendamento"),
    
    ;

    private String desc;
    
    private ErrorType(String desc) {
        this.desc = desc;
    }
    
    public String getDesc() {
        return desc;
    }
}
