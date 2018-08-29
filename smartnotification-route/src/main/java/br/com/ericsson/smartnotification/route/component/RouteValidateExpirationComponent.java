package br.com.ericsson.smartnotification.route.component;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;

@Component
public class RouteValidateExpirationComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RouteValidateExpirationComponent.class);

    public boolean isExpired(MessageEnrichedObject eventMessageEnriched) {

        LOG.info("Validando Mensagem Eriquecida- {}", eventMessageEnriched);
        
        int minutesToExpire = eventMessageEnriched.getShippingRestriction().getMinutesToExpire();

        LocalDateTime dateTimeReceived = eventMessageEnriched.getEventEnrichmentObject().getEvent().getDateTimeReceived();

        LocalDateTime dateTimeMax = dateTimeReceived.plusMinutes(minutesToExpire);

        return LocalDateTime.now().isAfter(dateTimeMax);

    }
}
