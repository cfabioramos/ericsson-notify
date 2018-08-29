package br.com.ericsson.smartnotification.route.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageComponent;
import br.com.ericsson.smartnotification.route.component.RouteValidationsComponent;

@Service
public class RouteBusiness {

    private static final Logger LOG = LoggerFactory.getLogger(RouteBusiness.class);

    @Autowired
    private RouteValidationsComponent routeValidationsComponent;

    @Autowired
    private RouteSendMessageComponent routeSendMessageComponent;

    public void route(MessageEnrichedObject eventMessageEnriched) {
        LOG.info("Mensagem Recebida pelo route : {}", eventMessageEnriched);

        if (eventMessageEnriched.getShippingRestriction() == null ||
        		routeValidationsComponent.isNotExpired(eventMessageEnriched)) {

            LOG.info("Mensagem não expirada - {}", eventMessageEnriched);

            if (eventMessageEnriched.getShippingRestriction() == null ||
            		routeValidationsComponent.isNotExceded(eventMessageEnriched)) {

                LOG.info("Mensagem não excedida - {}", eventMessageEnriched);

                routeSendMessageComponent.enviarMensagens(eventMessageEnriched);

            } else {

                LOG.info("Mensagem Descartada por exceder o limite maximo de {} envios por dia para o template {} ",
                        eventMessageEnriched.getShippingRestriction().getMaximumNumberSubmissions(),
                        eventMessageEnriched.geEnrichmentObject().getTemplateId());
            }

        } else {

            LOG.info("Mensagem Descartada por ter expirado, acima de {} minutos : {}",
                    eventMessageEnriched.getShippingRestriction().getMinutesToExpire(), eventMessageEnriched);
        }
    }

}
