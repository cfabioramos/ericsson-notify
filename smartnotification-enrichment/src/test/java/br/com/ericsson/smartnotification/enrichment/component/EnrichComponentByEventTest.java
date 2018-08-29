package br.com.ericsson.smartnotification.enrichment.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
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
public class EnrichComponentByEventTest {

    @Mock
    private TemplateMessageRepository templateMessageRepository;

    @Mock
    private EnrichmentEventFields enrichmentEventFields;

    @Mock
    private EnrichmentCampaingFields enrichmentCampaingFields;

    @Mock
    private EnrichmentGenericApi enrichmentClaroMsisdn;

    @Mock
    private KafkaTopicLogProducer logQueue;

    @InjectMocks
    private EnrichComponent enrichComponent;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        enrichComponent.setEnrichmentCampaingFields(enrichmentCampaingFields);
        enrichComponent.setEnrichmentGenericApi(new EnrichmentClaroApiMsisdnImpl());
        enrichComponent.setEnrichmentEventFields(new EnrichmentEventFieldsImpl());
    }
    
    @Test
    public void enrichCampaignEventSucessTest() throws ApplicationException {

        Optional<TemplateMessage> templateMessage = this.geTemplateMessageEnrichment();
        EventEnrichmentObject enrichmentObject = this.eventEnrichmentObject();

        when(templateMessageRepository.findById(enrichmentObject.getTemplateId())).thenReturn(templateMessage);

        MessageEnrichedObject messageEnrichedObject = enrichComponent.enrich(enrichmentObject);

        for (MessageEnriched enriched : messageEnrichedObject.getMessagesEnriched()) {
            assertNotNull(enriched);
            assertEquals(
                    "Parabéns Ricard! Voce completou 2 ano de Promoção e por isso ganhou 100 pontos. Acesse agora o site https://www.claro.com.br e saiba como aproveitar!",
                    enriched.getMessage());
        }

        verifyZeroInteractions(enrichmentCampaingFields);
    }

    private Optional<TemplateMessage> geTemplateMessageEnrichment() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("teste",
                "Parabéns {claro_api.name}! Voce completou {ano} ano de {promoName} e por isso ganhou {pontos} pontos. Acesse agora o site {claro_api.url_info} e saiba como aproveitar!",
                Channel.WEB, 1, "1"));
        return Optional.ofNullable(new TemplateMessage(TopicPriority.MEDIUM, messages));
    }
    
    private EventEnrichmentObject eventEnrichmentObject() {
        Event event = new Event(1, "teste", LocalDateTime.now());
        event.getFields().add(new EventField("msisdn", "999999999999"));
        event.getFields().add(new EventField("nType", "1"));
        event.getFields().add(new EventField("ano", "2"));
        event.getFields().add(new EventField("promoName", "Promoção"));
        event.getFields().add(new EventField("pontos", "100"));
        return new EventEnrichmentObject(TopicPriority.MEDIUM, event, "1");
    }
    
}
