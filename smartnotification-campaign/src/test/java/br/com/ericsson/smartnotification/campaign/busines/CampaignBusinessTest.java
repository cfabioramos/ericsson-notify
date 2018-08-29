package br.com.ericsson.smartnotification.campaign.busines;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.ericsson.smartnotification.campaign.kafka.KafkaCampaignEnrichmentPriorityTopic;
import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.entities.CampaignRecipient;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;
import br.com.ericsson.smartnotification.log.component.LogComponent;
import br.com.ericsson.smartnotification.repository.CampaignRecipientRepository;
import br.com.ericsson.smartnotification.repository.CampaignRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;

@RunWith(MockitoJUnitRunner.class)
public class CampaignBusinessTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRecipientRepository recipientRepository;

    @Mock
    private OptOutRepository optOutRepository;

    @Mock
    private LogComponent logComponent;

    @Mock
    private KafkaCampaignEnrichmentPriorityTopic campaignEnrichmentPriorityTopic;

    @Mock
    private TemplateMessageRepository templateMessageRepository;

    @InjectMocks
    private CampaignBusiness business;

    @Test
    public void loadMoreCampaigns() {
        LocalDateTime date = LocalDateTime.now();
        Pageable pageable = this.getPage(0);
        List<Campaign> campaigns = this.campaignsPage0();
        List<CampaignRecipient> campaignRecipients = this.campaignRecipientsPage0();
        
        when(campaignRepository.createPageRequest(0)).thenReturn(pageable);
        when(campaignRepository.findByStartDateBeforeAndEndDateAfterAndActiveTrue(date, date, pageable)).thenReturn(campaigns);
        when(campaignRepository.findByStartDateBeforeAndEndDateIsNullAndActiveTrue(date, pageable)).thenReturn(new ArrayList<>());
        when(recipientRepository.createPageRequest(0)).thenReturn(pageable);
        when(recipientRepository.findByCampaignId(campaigns.get(0).getId(), pageable)).thenReturn(campaignRecipients);
        when(templateMessageRepository.findById(campaigns.get(0).getTemplate())).thenReturn(templateMenssage());
        
        
        business.publishMessagesEnrichedsScheduleds(date);
    }

    private List<Campaign> campaignsPage0() {
        List<Campaign> campaigns = new ArrayList<>();
        for (int x = 1; x < 3; x++) {
            campaigns.add(new Campaign("teste" + x, "" + x, LocalDateTime.now(), LocalDateTime.now()));
        }
        return campaigns;
    }

    private Pageable getPage(int page) {
        return PageRequest.of(page, Constants.MAX_PAGINATION);
    }

    private List<CampaignRecipient> campaignRecipientsPage0() {
        List<CampaignRecipient> recipients = new ArrayList<>();
        for (int x = 1; x < 5; x++) {
            recipients.add(new CampaignRecipient("teste " + x, "111111111111111" + x));
        }
        return recipients;
    }

    private Optional<TemplateMessage> templateMenssage() {
        return Optional.of(new TemplateMessage(TopicPriority.LOW, new ArrayList<>()));
    }
}
