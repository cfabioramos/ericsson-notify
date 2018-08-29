package br.com.ericsson.smartnotification.gateway.domain;

import java.io.Serializable;

public class TokenFields implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msisdn;
    private String appname;
    private String token;
    
    public TokenFields() {
    }
    
    public TokenFields(String  msisdn, String appname, String token) {
        this.msisdn = msisdn;
        this.appname = appname;
        this.token = token;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public String getKey() {
        return msisdn+":"+appname;
    }

}
