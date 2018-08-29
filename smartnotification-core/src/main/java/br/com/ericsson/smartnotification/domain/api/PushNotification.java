package br.com.ericsson.smartnotification.domain.api;

import java.io.Serializable;

public class PushNotification implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Notification notification;
    
    private String to;

    public PushNotification() {
    }

    public PushNotification(String title, String message, String to) {
        this.notification = new Notification(title, message);
        this.to = to;
    }
    
    public PushNotification(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
