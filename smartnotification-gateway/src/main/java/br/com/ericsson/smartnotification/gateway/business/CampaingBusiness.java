package br.com.ericsson.smartnotification.gateway.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.repository.CampaignRepository;

@Service
public class CampaingBusiness {

    @Autowired
    private CampaignRepository campaignRepository;

    public void save(Campaign campaign) throws ValidationException {
        validation(campaign);
        campaignRepository.save(campaign);
    }

    protected void validation(Campaign campaign) throws ValidationException {
        String messageFailDefaut = "o campo %s é obrigatório.";
        if (StringUtils.isEmpty(campaign.getTemplate())) {
            throw new ValidationException(String.format(messageFailDefaut, "template"));
        }
        if (StringUtils.isEmpty(campaign.getStartDate())) {
            throw new ValidationException(String.format(messageFailDefaut, "startDate"));
        }
        if (StringUtils.isEmpty(campaign.getDescription())) {
            throw new ValidationException(String.format(messageFailDefaut, "description"));
        }
        if (!StringUtils.isEmpty(campaign.getEndDate()) && campaign.getStartDate().isAfter(campaign.getEndDate())) {
            throw new ValidationException("o campo startDate não pode ter uma data posterior a endDate");
        }
    }
}
