package br.com.ericsson.smartnotification.campaign.busines;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.campaign.kafka.KafkaCampaignEnrichmentPriorityTopic;
import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.domain.log.LogPropertiesBuilder;
import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.entities.CampaignRecipient;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.ErrorType;
import br.com.ericsson.smartnotification.enums.Project;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;
import br.com.ericsson.smartnotification.log.component.LogComponent;
import br.com.ericsson.smartnotification.repository.CampaignRecipientRepository;
import br.com.ericsson.smartnotification.repository.CampaignRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;

@Service
public class CampaignBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(CampaignBusiness.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignRecipientRepository recipientRepository;

    @Autowired
    private OptOutRepository optOutRepository;

    @Autowired
    private TemplateMessageRepository templateMessageRepository; 
    
    @Autowired
    private KafkaCampaignEnrichmentPriorityTopic campaignEnrichmentPriorityTopic;

    @Autowired
    private LogComponent logComponent;

    public void publishMessagesEnrichedsScheduleds(LocalDateTime date) {
        int total = 0;
        LOG.info("Buscando campanhas cadastradas {}", date);
        int page = 0;
        List<Campaign> campaigns = null;
        do {
            campaigns = this.getCampaignScheduleds(date, page);
            for (Campaign campaign : campaigns) {
                try {
                    LOG.info("Buscando template {} para campanha {} ", campaign.getTemplate(), campaign.getId());
                    TemplateMessage templateMessage = this.getTemplateMessage(campaign.getTemplate());
                    
                    LOG.info("Buscando clientes da campanha {} ", campaign);
                    this.searchClients(campaign, templateMessage.getPriority());
                    total++;
                }catch (ApplicationException e) {
                    LOG.info("Descartando Campanha {} por {}", campaign.getId(), e.getMessage());
                }
                LOG.info("Desativando campanha {}", campaign);
                campaign.setActive(false);
                campaignRepository.save(campaign);
            }
            page++;
        }while (!campaigns.isEmpty());
        LOG.info("{} campanha(s) processada(s) pelo agendador de campanhas", total);
    }

    private void searchClients(Campaign campaign, TopicPriority priority) {
        List<CampaignRecipient> campaignRecipients = null;
        int totalMsisdnsValid = 0;
        int totalMsisdns = 0;
        int page = 0;
        do {
            campaignRecipients = recipientRepository.findByCampaignId(campaign.getId(),
                    recipientRepository.createPageRequest(page));
            totalMsisdns = totalMsisdns + campaignRecipients.size();
            for (CampaignRecipient campaignRecipient : campaignRecipients) {
                
                if (!isOptOut(campaignRecipient.getMsisdn())) {
                    LOG.info("msisdn {} enviado para enriquecimento", campaignRecipient.getMsisdn());
                    CampaignEnrichmentObject enrichmentObject = buildCampaignEnrichmentObject(campaign,
                            campaignRecipient.getMsisdn(), priority, campaignRecipient.getEnrichmentValues());
                	campaignEnrichmentPriorityTopic.toTopic(enrichmentObject);
                    totalMsisdnsValid++;
                } else {
                    LOG.info("Descartando msisdn {} por optOut", campaignRecipient.getMsisdn());
                    logComponent.publishNotification(LogPropertiesBuilder.getBuilder()
                    		.setErrorType(ErrorType.OPTOUT_CAMPAIGN.toString())
                    		.setProcess(Project.CAMPAIGNS.toString())
                    		.setCampaign(campaign.getId())
                    		.setMsisdn(campaignRecipient.getMsisdn())
                    		.getLogProperties());
                }
                LOG.info("Removendo msisdn {} da campanha", campaignRecipient.getMsisdn());
                recipientRepository.delete(campaignRecipient);
            }
            page++;
        } while (!campaignRecipients.isEmpty());
        LOG.info("{} de {} clientes foram processados para emitir notificações para campanha {}", totalMsisdnsValid,
                totalMsisdns, campaign.getId());
    }

    private List<Campaign> getCampaignScheduleds(LocalDateTime date, int page) {
        Pageable pageable = campaignRepository.createPageRequest(page);
        List<Campaign> campaigns = campaignRepository.findByStartDateBeforeAndEndDateAfterAndActiveTrue(date, date, pageable);
        campaigns.addAll(campaignRepository.findByStartDateBeforeAndEndDateIsNullAndActiveTrue(date, pageable));
        return campaigns;
    }

    private CampaignEnrichmentObject buildCampaignEnrichmentObject(Campaign campaign, String msisdn, TopicPriority priority, List<String> enrichmentValues) {
        return new CampaignEnrichmentObject(campaign.getId(), priority, msisdn, campaign.getTemplate(), LocalDateTime.now(), enrichmentValues);
    }

    private TemplateMessage getTemplateMessage(String templateId) throws ApplicationException {
        Optional<TemplateMessage>  templateMessage = templateMessageRepository.findById(templateId);
        if(templateMessage.isPresent()) {
            return templateMessage.get();
        }
        throw new ApplicationException(String.format("Template %s não encontrado", templateId));
    }
    
    private boolean isOptOut(String msisdn) {
        return optOutRepository.hasOptedOut(msisdn);
    }

}
