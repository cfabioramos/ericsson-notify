package br.com.ericsson.smartnotification.campaign.tasks;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.campaign.busines.CampaignBusiness;

@Component
public class CampaignSchedulerTasks {
    
    private static final Logger log = LoggerFactory.getLogger(CampaignSchedulerTasks.class);
    
    @Autowired
    private CampaignBusiness business;
    
    @Scheduled(fixedRate = 10000)
    public void loadCampaigns() {
        LocalDateTime date = LocalDateTime.now();
        log.info("Iniciando busca de campanhas{}", date);
        business.publishMessagesEnrichedsScheduleds(date);
    }

}
