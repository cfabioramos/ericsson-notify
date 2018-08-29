package br.com.ericsson.smartnotification.scheduler.kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.kafka.KafkaRouteReceiver;

@RunWith(MockitoJUnitRunner.class)
public class KafkaRouteReceiverTest {

    @InjectMocks
    private KafkaRouteReceiver kafkaRouteReceiver;

    @Test
    public void enrichTemplateSucess() throws ApplicationException {
/*
        String eventMessageEnrichedJson = "";
        
        kafkaRouteReceiver.receive(eventMessageEnrichedJson);

        TemplateMessage templateMessage = this.geTemplateMessage1();
        Event event = this.geEvent();
        
        when(enrichmentEventFields.enrich(templateMessage.getEnrichmentEventFields(), event)).thenReturn(this.geValuesEvent());
        
        Map<String, String> values = enrichComponent.enrichEvent(templateMessage, event);
        
        MessageEnriched messageEnriched = templateMessage.getMessageEnriched(values);
        
        assertNotNull(messageEnriched);
        
        for (Map.Entry<String, String> entry : values.entrySet()) {
            assertTrue(messageEnriched.getMessage().contains(entry.getValue()));
        }
*/
    }

}
