package br.com.ericsson.smartnotification.web.dto;

import br.com.ericsson.smartnotification.entities.FirebaseApp;
import br.com.ericsson.smartnotification.enums.Channel;

public class FirebaseAppDto extends AbstractDto<FirebaseApp, FirebaseAppDto>{
    
    private String name;
    
    private String desc;
    
    private Channel channel;
    
    private String authorizationKey;
      
    public FirebaseAppDto() {
        super(null, true);
    }

    public FirebaseAppDto(String id, boolean active, String name, String desc, Channel type, String authorizationKey) {
        super(id, active);
        this.setActive(active);
        this.name = name;
        this.desc = desc;
        this.channel = type;
        this.authorizationKey = authorizationKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel type) {
        this.channel = type;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    @Override
    protected FirebaseApp getDocument() {
        return new FirebaseApp();
    }

}
