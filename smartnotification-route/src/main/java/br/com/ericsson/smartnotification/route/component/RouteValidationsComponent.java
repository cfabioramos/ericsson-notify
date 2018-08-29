package br.com.ericsson.smartnotification.route.component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.ForwardedNotifications;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;

@Component
public class RouteValidationsComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RouteValidationsComponent.class);

    @Autowired
    private RouteFowardedNotificationsComponent routeFowardedNotificationsComponent;

    public boolean isNotExpired(MessageEnrichedObject eventMessageEnriched) {

        LOG.info("Validando prazo de vigência da Mensagem Eriquecida - {}", eventMessageEnriched);

        if (eventMessageEnriched.getShippingRestriction() == null)
            return true;

        if (eventMessageEnriched.getShippingRestriction().getMinutesToExpire() < 1)
            return true;

        LocalDateTime dateTimeReceived = eventMessageEnriched.geEnrichmentObject().getDateTimeReceived();

        return LocalDateTime.now().isBefore(
                dateTimeReceived.plusMinutes(eventMessageEnriched.getShippingRestriction().getMinutesToExpire()));
    }

    public boolean isNotExceded(MessageEnrichedObject eventMessageEnriched) {

        LOG.info("Validando maximo de notificações configurado no template {} da Mensagem Eriquecida - {}",
                eventMessageEnriched.geEnrichmentObject().getTemplateId(), eventMessageEnriched);

        if (eventMessageEnriched.getShippingRestriction() == null)
            return true;

        if (eventMessageEnriched.getShippingRestriction().getMaximumNumberSubmissions() < 1)
            return true;

        String msisdn = eventMessageEnriched.getMsisdn();

        ForwardedNotifications forwardedNotificationsDB = routeFowardedNotificationsComponent.getForwardedNotifications(
                new ForwardedNotifications(msisdn, eventMessageEnriched.geEnrichmentObject().getTemplateId()));

        if (forwardedNotificationsDB != null && forwardedNotificationsDB.getDateTimeLastSend() != null) {

            LOG.info("Recuperado do Redis o msisdn {} e o template {} notificações enviadas na data atual {} ", msisdn,
                    eventMessageEnriched.geEnrichmentObject().getTemplateId(), forwardedNotificationsDB);

            if (forwardedNotificationsDB.getDateTimeLastSend().toLocalDate().isEqual(LocalDate.now())) {

                return (forwardedNotificationsDB.getNumberOfSends() < eventMessageEnriched.getShippingRestriction()
                        .getMaximumNumberSubmissions());

            }
        }

        return true;
    }

}
