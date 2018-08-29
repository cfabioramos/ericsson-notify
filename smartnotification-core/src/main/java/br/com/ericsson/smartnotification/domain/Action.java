package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("action")
    private String name;

    private String title;

    public Action() {
    }

    public Action(String action, String title) {
        this.name = action;
        this.title = title;
    }

    public String getAction() {
        return name;
    }

    public void setAction(String action) {
        this.name = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
