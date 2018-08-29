package br.com.ericsson.smartnotification.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.ericsson.smartnotification.enums.Channel;



@Document
public class FirebaseApp extends AbstractDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String name;
    
    private String desc;
    
    private Channel channel;
    
    private String authorizationKey;

    
    
    public FirebaseApp() {
        super();
    }

    public FirebaseApp(String id, String name, boolean active, String desc, Channel channel, String authorizationKey) {
        super(id, active);
        this.name = name;
        this.desc = desc;
        this.channel = channel;
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

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }
    
    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
