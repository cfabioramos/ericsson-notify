package br.com.ericsson.smartnotification.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import br.com.ericsson.smartnotification.utils.JsonUtil;

public class ForwardedNotifications implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msisdn;
    private String idTemplate;
    private LocalDateTime dateTimeLastSend;
    private int numberOfSends;

    public ForwardedNotifications() {
    }

    public ForwardedNotifications(String msisdn, String idTemplate, LocalDateTime dateTimeLastSend) {
        this.msisdn = msisdn;
        this.idTemplate = idTemplate;
        this.dateTimeLastSend = dateTimeLastSend;
    }

    public ForwardedNotifications(String msisdn, String idTemplate) {
        this.msisdn = msisdn;
        this.idTemplate = idTemplate;
        this.dateTimeLastSend = LocalDateTime.now();
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getIdTemplate() {
        return idTemplate;
    }

    public void setIdTemplate(String idTemplate) {
        this.idTemplate = idTemplate;
    }

    public LocalDateTime getDateTimeLastSend() {
        return dateTimeLastSend;
    }

    public void setDateTimeLastSend(LocalDateTime dateTimeLastSend) {
        this.dateTimeLastSend = dateTimeLastSend;
    }

    public int getNumberOfSends() {
        return numberOfSends;
    }

    public void setNumberOfSends(int numberOfSends) {
        this.numberOfSends = numberOfSends;
    }

    public String getKey() {
        return msisdn + ":" + idTemplate;
    }

    @Override
    public String toString() {
        return JsonUtil.parseToJsonString(this);
    }

}
