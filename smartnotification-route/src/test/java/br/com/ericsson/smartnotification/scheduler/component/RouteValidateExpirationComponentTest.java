package br.com.ericsson.smartnotification.scheduler.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.component.RouteValidateExpirationComponent;

@RunWith(MockitoJUnitRunner.class)
public class RouteValidateExpirationComponentTest {

    @InjectMocks
    private RouteValidateExpirationComponent routeValidateExpirationComponent;

    @Test
    public void test_NotExpired() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(LocalDateTime.now(), 60);
        assertThat(routeValidateExpirationComponent.isExpired(eventMessageEnriched)).isFalse();
    }

    @Test
    public void test_Expired() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(LocalDateTime.now().minusMinutes(70), 60);
        assertThat(routeValidateExpirationComponent.isExpired(eventMessageEnriched)).isTrue();
    }

    private MessageEnrichedObject getEventMessageEnriched(LocalDateTime dateTimeReceived, int minutesToExpire) {
        ShippingRestriction shippingRestriction = new ShippingRestriction(new Date(), new ArrayList<DayOfWeek>(), LocalTime.MIN, LocalTime.MAX, 0, minutesToExpire);
        return new MessageEnrichedObject(new ArrayList<MessageEnriched>(), shippingRestriction, new EventEnrichmentObject(TopicPriority.MEDIUM, new Event(1,"Teste",dateTimeReceived), "1") );
    }

}
