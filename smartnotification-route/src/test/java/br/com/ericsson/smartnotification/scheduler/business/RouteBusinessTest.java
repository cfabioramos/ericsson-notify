package br.com.ericsson.smartnotification.scheduler.business;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.business.RouteBusiness;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageComponent;
import br.com.ericsson.smartnotification.route.component.RouteValidationsComponent;

@RunWith(MockitoJUnitRunner.class)
public class RouteBusinessTest {

    @Mock
    private RouteValidationsComponent validations;

    @Mock
    private RouteSendMessageComponent sendMessage;

    @InjectMocks
    private RouteBusiness routeBusiness;

    @Test
    public void testExpirada() throws ApplicationException {
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(LocalDateTime.now().minusMinutes(1), 60);
        Mockito.when(validations.isNotExpired(eventMessageEnriched)).thenReturn(true);
        Mockito.when(validations.isNotExceded(eventMessageEnriched)).thenReturn(true);
        
        Mockito.doNothing().when(sendMessage).enviarMensagens(eventMessageEnriched);

        routeBusiness.route(eventMessageEnriched);
    }

    private MessageEnrichedObject getEventMessageEnriched(LocalDateTime dateTimeReceived, int MinutesToExpire) {
        ShippingRestriction shippingRestriction = new ShippingRestriction(new Date(), new ArrayList<DayOfWeek>(), LocalTime.MIN, LocalTime.MAX, 5, MinutesToExpire);
        EnrichmentObject enrichmentTopicObject = new EventEnrichmentObject(TopicPriority.HIGH, new Event(1, "Teste", dateTimeReceived), "idTemplate");
        return new MessageEnrichedObject(new ArrayList<MessageEnriched>(), shippingRestriction, enrichmentTopicObject);
    }

}
