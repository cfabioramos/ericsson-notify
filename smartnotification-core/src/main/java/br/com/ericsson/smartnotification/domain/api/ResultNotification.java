package br.com.ericsson.smartnotification.domain.api;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ResultNotification implements Serializable{
    

    private static final long serialVersionUID = 1L;

    @SerializedName("message_id") 
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
