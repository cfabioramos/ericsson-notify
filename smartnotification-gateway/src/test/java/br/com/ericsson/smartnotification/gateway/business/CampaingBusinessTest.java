package br.com.ericsson.smartnotification.gateway.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.time.LocalDateTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.repository.CampaignRepository;

@RunWith(MockitoJUnitRunner.class)
public class CampaingBusinessTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaingBusiness business;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void saveTest() throws ValidationException {
        Campaign campaign = this.getCampaign();
        business.save(campaign);
        verify(campaignRepository).save(campaign);
    }

    @Test
    public void saveTestFailDesc() throws ValidationException {
        expectedException.expect(ValidationException.class);
        
        Campaign campaign = this.getCampaignNoRequiredFielddesc();
        business.save(campaign);
        verifyZeroInteractions(campaignRepository);
    }
    
    @Test
    public void saveTestFailTemplate() throws ValidationException {
        expectedException.expect(ValidationException.class);
        
        Campaign campaign = this.getCampaignNoRequiredFieldTemplate();
        business.save(campaign);
        verifyZeroInteractions(campaignRepository);
    }
    
    @Test
    public void saveTestFailStartDate() throws ValidationException {
        expectedException.expect(ValidationException.class);
        
        Campaign campaign = this.getCampaignNoRequiredFieldStartDate();
        business.save(campaign);
        verifyZeroInteractions(campaignRepository);
    }

    @Test
    public void saveTestFailStartDateAfterEndDate() throws ValidationException {
        expectedException.expect(ValidationException.class);

        Campaign campaign = this.getCampaignFieldStartDateAFterEndDate();
        business.save(campaign);
        verifyZeroInteractions(campaignRepository);
    }

    private Campaign getCampaign() {
        Campaign campaign = new Campaign("teste", "1", LocalDateTime.now(), LocalDateTime.now());
        return campaign;
    }

    private Campaign getCampaignNoRequiredFielddesc() {
        Campaign campaign = new Campaign("1", null, LocalDateTime.now(), LocalDateTime.now());
        return campaign;
    }

    private Campaign getCampaignNoRequiredFieldTemplate() {
        Campaign campaign = new Campaign(null, "teste", LocalDateTime.now(), LocalDateTime.now());
        return campaign;
    }

    private Campaign getCampaignNoRequiredFieldStartDate() {
        Campaign campaign = new Campaign("1", "teste", null, null);
        return campaign;
    }

    private Campaign getCampaignFieldStartDateAFterEndDate() {
        Campaign campaign = new Campaign("1", "teste", LocalDateTime.MAX, LocalDateTime.MIN);
        return campaign;
    }

}
