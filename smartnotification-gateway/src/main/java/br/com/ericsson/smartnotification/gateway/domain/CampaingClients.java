package br.com.ericsson.smartnotification.gateway.domain;

import java.util.ArrayList;
import java.util.List;

public class CampaingClients {

    private String campaignId;

    private List<Clients> clients;

    public CampaingClients() {
        super();
    }

    public CampaingClients(String campaignId) {
        super();
        this.campaignId = campaignId;
        this.clients = new ArrayList<>();
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public List<Clients> getClients() {
        return clients;
    }

    public void setClients(List<Clients> clients) {
        this.clients = clients;
    }



    
}
