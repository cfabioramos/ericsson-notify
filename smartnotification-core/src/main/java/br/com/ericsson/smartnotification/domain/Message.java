package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;

public class Message implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String title;
    
    private String text;
    
    private String client;

    public Message() {
    }

    public Message(String title, String text, String client) {
        this.title = title;
        this.text = text;
        this.client = client;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return text;
    }

    public void setMessage(String text) {
        this.text = text;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
    
    
    
}
