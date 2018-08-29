package br.com.ericsson.smartnotification.web.enums;

public enum MessageType {
    INFO,
    WARNING,
    ERROR,
    SUCCESS;
    
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
