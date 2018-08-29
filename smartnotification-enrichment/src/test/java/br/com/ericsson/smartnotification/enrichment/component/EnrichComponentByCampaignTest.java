package br.com.ericsson.smartnotification.enrichment.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.entities.Message;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentCampaingFields;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentEventFields;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentGenericApi;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnrichComponentByCampaignTest {

    @Mock
    private TemplateMessageRepository templateMessageRepository;

    @Mock
    private EnrichmentGenericApi enrichmentGenericApi;

    @Mock
    private EnrichmentCampaingFields enrichmentCampaignFields;

    @Mock
    private EnrichmentEventFields enrichmentEventFields;

    @Mock
    private KafkaTopicLogProducer logQueue;

    @InjectMocks
    private EnrichComponent enrichComponent;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        enrichComponent.setEnrichmentCampaingFields(new EnrichmentCampaignFieldsImpl());
        enrichComponent.setEnrichmentGenericApi(new EnrichmentClaroApiMsisdnImpl());
        enrichComponent.setEnrichmentEventFields(enrichmentEventFields);
    }

    @Test
    public void enrichCampaignOriginSucessTest() throws ApplicationException {

        Optional<TemplateMessage> templateMessage = this.geTemplateMessageEnrichment();
        CampaignEnrichmentObject enrichmentObject = this.campaignEnrichmentObject();

        when(templateMessageRepository.findById(enrichmentObject.getTemplateId())).thenReturn(templateMessage);

        MessageEnrichedObject messageEnrichedObject = enrichComponent.enrich(enrichmentObject);

        for (MessageEnriched enriched : messageEnrichedObject.getMessagesEnriched()) {
            assertNotNull(enriched);
            assertEquals(
                    "Parabéns Ricard! Voce completou 3 ano de Promoção e por isso ganhou 300 pontos. Acesse agora o site https://www.claro.com.br e saiba como aproveitar!",
                    enriched.getMessage());
        }

        verifyZeroInteractions(enrichmentEventFields);
    }

    @Test
    public void enrichCampaignOriginNoTemplateTest() throws ApplicationException {
        CampaignEnrichmentObject enrichmentObject = this.campaignEnrichmentObject();

        expectedException.expect(ApplicationException.class);
        expectedException
                .expectMessage(String.format("Template não encontrado para id %s", enrichmentObject.getTemplateId()));

        when(templateMessageRepository.findById(enrichmentObject.getTemplateId())).thenReturn(Optional.empty());

        try {
            enrichComponent.enrich(enrichmentObject);
        } catch (ApplicationException e) {
            verifyZeroInteractions(enrichmentGenericApi);
            verifyZeroInteractions(enrichmentEventFields);
            verifyZeroInteractions(enrichmentCampaignFields);
            throw e;
        }

    }

    @Test
    public void enrichCampaignOriginOnlyApiSucessTest() throws ApplicationException {

        Optional<TemplateMessage> templateMessage = this.geTemplateMessageEnrichmentOnlyApi();
        CampaignEnrichmentObject enrichmentObject = this.campaignEnrichmentObjectNoEnrichment();

        when(templateMessageRepository.findById(enrichmentObject.getTemplateId())).thenReturn(templateMessage);

        MessageEnrichedObject messageEnrichedObject = enrichComponent.enrich(enrichmentObject);

        for (MessageEnriched enriched : messageEnrichedObject.getMessagesEnriched()) {
            assertNotNull(enriched);
            assertEquals(
                    "Parabéns Ricard! Voce completou 1 ano de Promoção em 2018-03-03 e por isso ganhou 100 pontos. Acesse agora o site https://www.claro.com.br e saiba como aproveitar!",
                    enriched.getMessage());
        }

        verifyZeroInteractions(enrichmentEventFields);
        verifyZeroInteractions(enrichmentCampaignFields);
    }

    @Test
    public void enrichCampaignOriginFailEnrichmentFieldsTemplateTest() throws ApplicationException {

        Optional<TemplateMessage> templateMessage = this.geTemplateMessageEnrichment();
        CampaignEnrichmentObject enrichmentObject = this.campaignEnrichmentObjectNoEnrichment();

        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage(String.format(
                "Existem campos no template %s que não foram definidos para enriquecimento na campanha %s - campos do template %s : campos informados no enriquecimento da campanha %s",
                enrichmentObject.getTemplateId(),
                enrichmentObject.getCampaignId(),
                Arrays.asList(templateMessage.get().getMessages().get(0).getEnrichmentTemplateFields()),
                enrichmentObject.getEnrichmentValues()));

        when(templateMessageRepository.findById(enrichmentObject.getTemplateId())).thenReturn(templateMessage);

        try {
            enrichComponent.enrich(enrichmentObject);
        } catch (ApplicationException e) {
            verifyZeroInteractions(enrichmentEventFields);
            verifyZeroInteractions(enrichmentCampaignFields);
            throw e;
        }

    }

    private CampaignEnrichmentObject campaignEnrichmentObject() {

        return new CampaignEnrichmentObject("1", TopicPriority.MEDIUM, "999999999999", "1", LocalDateTime.now(),
                Arrays.asList(new String[] { "3", "Promoção", "300" }));
    }

    private CampaignEnrichmentObject campaignEnrichmentObjectNoEnrichment() {

        return new CampaignEnrichmentObject("1", TopicPriority.MEDIUM, "999999999999", "1", LocalDateTime.now(),
                Arrays.asList(new String[] {}));
    }

    private Optional<TemplateMessage> geTemplateMessageEnrichment() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("teste",
                "Parabéns {claro_api.name}! Voce completou {ano} ano de {promoName} e por isso ganhou {pontos} pontos. Acesse agora o site {claro_api.url_info} e saiba como aproveitar!",
                Channel.WEB, 1, "1"));
        return Optional.ofNullable(new TemplateMessage(TopicPriority.MEDIUM, messages));
    }

    private Optional<TemplateMessage> geTemplateMessageEnrichmentOnlyApi() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("teste",
                "Parabéns {claro_api.name}! Voce completou 1 ano de Promoção em {claro_api.data_adesao} e por isso ganhou 100 pontos. Acesse agora o site {claro_api.url_info} e saiba como aproveitar!",
                Channel.WEB, 1, "1"));
        return Optional.ofNullable(new TemplateMessage(TopicPriority.MEDIUM, messages));
    }

}
