package br.com.ericsson.smartnotification.scheduler.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.component.RouteValidationsComponent;

@RunWith(MockitoJUnitRunner.class)
public class RouteValidationsComponentTest {

    @InjectMocks
    private RouteValidationsComponent validations;

    @Test
    public void testEventoExpidado() throws ApplicationException {
        MessageEnrichedObject eventMessageEnrichedExpirado = getEventMessageEnriched(LocalDateTime.now().minusMinutes(90), 60, 0, 0);
        assertThat(validations.isNotExpired(eventMessageEnrichedExpirado)).isFalse();
    }

    @Test
    public void testEventoNaoExpirado() throws ApplicationException {
        MessageEnrichedObject eventMessageEnrichedNaoExpirado = getEventMessageEnriched(LocalDateTime.now().minusMinutes(10), 60, 0, 0);
        assertThat(validations.isNotExpired(eventMessageEnrichedNaoExpirado)).isTrue();
    }

    private MessageEnrichedObject getEventMessageEnriched(LocalDateTime dateTimeReceived, int MinutesToExpire, int indexOfTheLastEnrichedMessageSent, int numberOfMessageSubmissions) {
        
    	List<MessageEnriched> messagesEnriched = new ArrayList<MessageEnriched>();
        MessageEnriched message = new MessageEnriched("Titulo", "Mensagem", Channel.APP, "appId");
        messagesEnriched.add(message);
        message = new MessageEnriched("Titulo", "Mensagem", Channel.SMS, "appId");
        messagesEnriched.add(message);
        
        ShippingRestriction shippingRestriction = new ShippingRestriction(new Date(), new ArrayList<DayOfWeek>(), LocalTime.MIN, LocalTime.MAX, 5, MinutesToExpire);
        
        EnrichmentObject enrichmentTopicObject = new EventEnrichmentObject(TopicPriority.HIGH, new Event(1, "Teste", dateTimeReceived), "idTemplate");
        
        MessageEnrichedObject returnObject = new MessageEnrichedObject(messagesEnriched, shippingRestriction, enrichmentTopicObject);
        returnObject.setIndexOfTheLastEnrichedMessageSent(indexOfTheLastEnrichedMessageSent);
        returnObject.setNumberOfMessageSubmissions(numberOfMessageSubmissions);
        
        return returnObject;

    }

}
