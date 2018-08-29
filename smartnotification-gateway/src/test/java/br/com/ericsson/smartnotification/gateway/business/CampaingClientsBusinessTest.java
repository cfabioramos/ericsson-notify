package br.com.ericsson.smartnotification.gateway.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.entities.CampaignRecipient;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.domain.CampaingClients;
import br.com.ericsson.smartnotification.gateway.domain.Clients;
import br.com.ericsson.smartnotification.repository.CampaignRecipientRepository;
import br.com.ericsson.smartnotification.repository.CampaignRepository;

@RunWith(MockitoJUnitRunner.class)
public class CampaingClientsBusinessTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CampaignRecipientRepository campaignRecipientRepository;

    @InjectMocks
    private CampaingClientsBusiness campaingClientsBusiness;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void saveSucessTest() throws ApplicationException {
        Optional<Campaign> campaign = this.campaign();
        CampaignRecipient campaignRecipient = this.campaignRecipientSucess();

        CampaingClients campaingClients = this.campaingClientsSucess();

        when(campaignRepository.findById("1")).thenReturn(campaign);
        when(campaignRecipientRepository.findByCampaignIdAndMsisdn("1", "557199999993")).thenReturn(campaignRecipient);

        campaingClientsBusiness.save(campaingClients);

        verify(campaignRecipientRepository).save(campaignRecipient);
    }

    @Test
    public void saveFailNoCampainTest() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        CampaingClients campaingClients = this.campaingClientsNoCampaign();

        campaingClientsBusiness.save(campaingClients);

        verifyZeroInteractions(campaignRepository);
        verifyZeroInteractions(campaignRecipientRepository);
    }

    @Test
    public void saveFailNoMsisdnTest() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        CampaingClients campaingClients = this.campaingClientsNoMsisdns();

        campaingClientsBusiness.save(campaingClients);

        verifyZeroInteractions(campaignRepository);
        verifyZeroInteractions(campaignRecipientRepository);
    }

    @Test
    public void saveFailFormatMsisdnTest() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        CampaingClients campaingClients = this.campaingClientsFormatMsisdn();

        campaingClientsBusiness.save(campaingClients);

        verifyZeroInteractions(campaignRepository);
        verifyZeroInteractions(campaignRecipientRepository);
    }

    private Optional<Campaign> campaign() {
        return Optional.of(new Campaign("teste", "1", LocalDateTime.now(), LocalDateTime.now()));
    }

    private CampaingClients campaingClientsSucess() {
        CampaingClients campaignRecipient = new CampaingClients("1");
        campaignRecipient.getClients().add(new Clients("557199999993||Edu||teste"));
        return campaignRecipient;
    }

    private CampaignRecipient campaignRecipientSucess() {
        CampaignRecipient campaignRecipient = new CampaignRecipient("1", "557199999993");
        return campaignRecipient;
    }

    private CampaingClients campaingClientsNoCampaign() {
        CampaingClients campaignRecipient = new CampaingClients("");
        campaignRecipient.getClients().add(new Clients("557199999993||Edu||teste"));
        return campaignRecipient;
    }

    private CampaingClients campaingClientsNoMsisdns() {
        CampaingClients campaignRecipient = new CampaingClients("1");
        return campaignRecipient;
    }

    private CampaingClients campaingClientsFormatMsisdn() {
        CampaingClients campaignRecipient = new CampaingClients("1");
        campaignRecipient.getClients().add(new Clients("4||Edu||teste"));
        return campaignRecipient;
    }
}
