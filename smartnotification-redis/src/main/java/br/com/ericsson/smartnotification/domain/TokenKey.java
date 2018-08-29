package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;

public class TokenKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msisdn;
    private String appname;
    
    public TokenKey() {
    }
    
    public TokenKey(String  msisdn, String appname) {
        this.msisdn = msisdn;
        this.appname = appname;
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

    public String getKey() {
        return msisdn+":"+appname;
    }

}
