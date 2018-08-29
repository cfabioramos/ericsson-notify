package br.com.ericsson.smartnotification.domain.api;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import br.com.ericsson.smartnotification.domain.Action;

public class Notification implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String title;
    private String body;
    private String icon;
    @SerializedName("click_action") 
    private String clickAction;
    private String image;
    private List<Action> actions;
    
    public Notification() {
    }


    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }


    public Notification(String title, String body, String icon, String clickAction, String image, List<Action> actions) {
        this.title = title;
        this.body = body;
        this.icon = icon;
        this.clickAction = clickAction;
        this.image = image;
        this.actions = actions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

}
