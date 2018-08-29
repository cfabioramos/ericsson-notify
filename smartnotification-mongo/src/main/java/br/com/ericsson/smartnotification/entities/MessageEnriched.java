package br.com.ericsson.smartnotification.entities;

import java.io.Serializable;

import br.com.ericsson.smartnotification.enums.Channel;

public class MessageEnriched extends AgregationDocument implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String title;

    private String message;

    private Channel channel;

    private String appId;

    private int attempts;
    
    public MessageEnriched() {
    }

    public MessageEnriched(String title, String message, Channel channel, String appId) {
        super();
        this.title = title;
        this.message = message;
        this.channel = channel;
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    
    
    
}
