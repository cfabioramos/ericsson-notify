package br.com.ericsson.smartnotification.web.domain;

import br.com.ericsson.smartnotification.web.enums.MessageType;

public class MessageView {
       
    private String title;
    
    private String info;
    
    private MessageType type;

    public MessageView(String title, String info, MessageType type) {
        this.title = title;
        this.info = info;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public MessageType getType() {
        return type;
    }
    
    
    
    

}
