package br.com.ericsson.smartnotification.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ericsson.smartnotification.entities.CampaignRecipient;

public interface CampaignRecipientRepository extends MongoRepository<CampaignRecipient, String>, PageableCreate{
    
    public List<CampaignRecipient> findByCampaignId(String campaign, Pageable pageable);
    
    public CampaignRecipient findByCampaignIdAndMsisdn(String campaign, String msisdn);
    
}
