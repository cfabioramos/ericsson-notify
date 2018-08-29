package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;

public class IdMessage implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String title;
    
    private String client;

    public IdMessage() {
    }

    public IdMessage(String title, String client) {
        this.title = title;
        this.client = client;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
    
    
    
}
