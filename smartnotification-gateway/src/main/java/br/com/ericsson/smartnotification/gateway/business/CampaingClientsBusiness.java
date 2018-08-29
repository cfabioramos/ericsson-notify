package br.com.ericsson.smartnotification.gateway.business;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.entities.CampaignRecipient;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.domain.CampaingClients;
import br.com.ericsson.smartnotification.gateway.domain.Clients;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceNotFoundException;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.repository.CampaignRecipientRepository;
import br.com.ericsson.smartnotification.repository.CampaignRepository;

@Service
public class CampaingClientsBusiness {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignRecipientRepository recipientRepository;

    public void save(CampaingClients campaignClients) throws ApplicationException {
        validation(campaignClients);

        this.isCampaignExists(campaignClients.getCampaignId());

        this.addClients(campaignClients);
    }

    private boolean isCampaignExists(String id) throws ResourceNotFoundException {
        Optional<Campaign> optional = campaignRepository.findById(id);
        if (optional.isPresent()) {
            return true;
        }
        throw new ResourceNotFoundException("Campanha", id);
    }

    protected void addClients(CampaingClients campaingClients) {
        for (Clients client : campaingClients.getClients()) {
            CampaignRecipient campaignRecipientMongo = recipientRepository.findByCampaignIdAndMsisdn(campaingClients.getCampaignId(), client.getMsisdn());
            if(campaignRecipientMongo != null) {
                campaignRecipientMongo.setEnrichmentValues(client.getEnrichments());
                recipientRepository.save(campaignRecipientMongo);
            }else {
                recipientRepository.save(new CampaignRecipient(campaingClients.getCampaignId(), client.getMsisdn(), client.getEnrichments()));
            }
        }
    }

    protected void validation(CampaingClients campaignClients) throws ValidationException {
        String messageFailDefaut = "o campo %s é obrigatório.";
        if (StringUtils.isEmpty(campaignClients.getCampaignId())) {
            throw new ValidationException(String.format(messageFailDefaut, "campaign"));
        }
        if (StringUtils.isEmpty(campaignClients.getClients())) {
            throw new ValidationException(String.format(messageFailDefaut, "clients"));
        }
        for (Clients client : campaignClients.getClients()) {
            if (client.getMsisdn() == null || client.getMsisdn().isEmpty()) {
                throw new ValidationException(String.format(messageFailDefaut, "msisdn"));
            }

            String regexOlyNumber = "^[0-9]*$";
            String regexSizeform13To20 = "\\w{12,20}\\b";

            if (!client.getMsisdn().matches(regexOlyNumber))
                throw new ValidationException(String.format("MSISDN %s inválido", client.getMsisdn()));
            if (!client.getMsisdn().matches(regexSizeform13To20))
                throw new ValidationException("O msisdn possui quantidade de caracteres inválido");
        }
    }
}
