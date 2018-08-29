package br.com.ericsson.smartnotification.route.component.sender;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.ericsson.smartnotification.domain.ForwardedNotifications;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.route.component.RouteFowardedNotificationsComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageAppComponent;

/**
 * <p>
 * This is a functional Mediator class for sending messages whose abstract
 * method is {@link #send(String, String)}.
 * 
 * @version 1.0.0
 * 
 */
public abstract class MessageServiceMediator {

    protected static final Logger LOG = LoggerFactory.getLogger(RouteSendMessageAppComponent.class);

    @Autowired
    private RouteFowardedNotificationsComponent routeFowardedNotificationsComponent;

    @Autowired
    private KafkaTopicLogProducer logQueue;

    protected abstract boolean publish(MessageEnrichedObject messageEnriched) throws ApplicationException;

    protected abstract int getMaxSizeMessage(MessageEnrichedObject messageEnriched) throws ApplicationException;

    public boolean send(MessageEnrichedObject messageEnriched) throws ApplicationException {

        if (messageEnriched.getMessagesEnriched()
                .get(messageEnriched.getIndexOfTheLastEnrichedMessageSent()).getMessage()
                .length() > getMaxSizeMessage(messageEnriched)) {

            LOG.info("Mensagem com tamanho maximo excedido : {}", messageEnriched.getMessagesEnriched()
                    .get(messageEnriched.getIndexOfTheLastEnrichedMessageSent()).getChannel());

            throw new ApplicationException("Mensagem com tamanho maximo excedido.");

        } else {

            LOG.info("Enviando Mensagem para o canal : {}", messageEnriched.getMessagesEnriched()
                    .get(messageEnriched.getIndexOfTheLastEnrichedMessageSent()).getChannel());

            try {
                boolean result = publish(messageEnriched);

                if (result) {
                    routeFowardedNotificationsComponent
                            .updateForwardedNotifications(new ForwardedNotifications(messageEnriched.getMsisdn(),
                                    messageEnriched.geEnrichmentObject().getTemplateId(), LocalDateTime.now()));

                    sendToLogProducer(true, null, messageEnriched);

                } else {

                    LOG.info("Falha no envio da mensagem pelo canal {} : {}",
                            messageEnriched.getMessagesEnriched().get(
                                    messageEnriched.getIndexOfTheLastEnrichedMessageSent())
                                    .getChannel(),
                            messageEnriched);

                    sendToLogProducer(false, "erro", messageEnriched);

                }
                return result;

            } catch (ApplicationException e) {

                sendToLogProducer(false, e.getMessage(), messageEnriched);

                throw new ApplicationException(e.getMessage());

            }

        }
    }

    private void sendToLogProducer(boolean result, String errorType, MessageEnrichedObject messageEnriched) {
/*
 * TODO
        logQueue.publish(new LogProperties((result ? "yes" : "no"),
                messageEnriched.getMessagesEnriched()
                        .get(messageEnriched.getShippingRestriction().getIndexOfTheLastEnrichedMessageSent())
                        .getChannel().toString(),
                messageEnriched.getMessagesEnriched()
                        .get(messageEnriched.getShippingRestriction().getIndexOfTheLastEnrichedMessageSent())
                        .getAppId(),
                "keyword", messageEnriched.getShippingRestriction().getNumberOfMessageSubmissions(), errorType,
                messageEnriched.getCampaignEnrichmentObject() != null
                        ? messageEnriched.getCampaignEnrichmentObject().getCampaignId()
                        : null,
                messageEnriched.getEventEnrichmentObject() != null
                        ? messageEnriched.getEventEnrichmentObject().getEvent().getId()
                        : null,
                messageEnriched.getEventEnrichmentObject() != null
                        ? messageEnriched.getEventEnrichmentObject().getEvent().getDescription()
                        : null,
                messageEnriched.geEnrichmentObject().getTemplateId(), messageEnriched.geEnrichmentObject().getRuleId(),
                messageEnriched.getMessagesEnriched()
                        .get(messageEnriched.getShippingRestriction().getIndexOfTheLastEnrichedMessageSent())
                        .getChannel().toString(),
                messageEnriched.getMsisdn(),
                messageEnriched.getMessagesEnriched()
                        .get(messageEnriched.getShippingRestriction().getIndexOfTheLastEnrichedMessageSent()).getId(),
                messageEnriched.getMessagesEnriched()
                        .get(messageEnriched.getShippingRestriction().getIndexOfTheLastEnrichedMessageSent())
                        .getMessage(),
                new Date()));
 */

    }
}
