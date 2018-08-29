package br.com.ericsson.smartnotification.route.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.component.sender.MessageServiceMediatorFactory;
import br.com.ericsson.smartnotification.route.kafka.KafkaRouteSchedulerPriorityTopic;

@Component
public class RouteSendMessageComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RouteSendMessageComponent.class);

    @Autowired
    private MessageServiceMediatorFactory messageServiceMediatorFactory;

    @Autowired
    private KafkaRouteSchedulerPriorityTopic kafkaSchedulerTopic;

    public void enviarMensagens(MessageEnrichedObject messageEnrichedObject) {
        boolean msgSent = false;

        int indexOfTheLastEnrichedMessageSent = messageEnrichedObject.getIndexOfTheLastEnrichedMessageSent();
        int numberOfSubmissionAttemptsOnTheChannel = messageEnrichedObject.getNumberOfMessageSubmissions();

        while (!msgSent && indexOfTheLastEnrichedMessageSent < messageEnrichedObject.getMessagesEnriched().size()
                && numberOfSubmissionAttemptsOnTheChannel < Constants.MAX_ATTEMPTS_FOR_CHANNEL) {

            MessageEnriched message = messageEnrichedObject.getMessagesEnriched()
                    .get(indexOfTheLastEnrichedMessageSent);

            try {

                if (messageServiceMediatorFactory.buildMessageServiceMediator(message.getChannel())
                        .send(messageEnrichedObject)) {

                    LOG.info("Mensagem enviada pelo canal {} com sucesso : {}", message.getChannel(),
                            messageEnrichedObject);
                    msgSent = true;

                } else {

                    numberOfSubmissionAttemptsOnTheChannel++;
                    if (numberOfSubmissionAttemptsOnTheChannel < Constants.MAX_ATTEMPTS_FOR_CHANNEL) {

                        LOG.info("Enviando para Scheduler pela {} vez : {}", numberOfSubmissionAttemptsOnTheChannel,
                                messageEnrichedObject);
                        messageEnrichedObject.setIndexOfTheLastEnrichedMessageSent(indexOfTheLastEnrichedMessageSent);
                        messageEnrichedObject.setNumberOfMessageSubmissions(numberOfSubmissionAttemptsOnTheChannel);
                        sendToScheduler(messageEnrichedObject);
                        break;

                    }

                    LOG.info("Tentativas esgotadas para envio pelo Canal : {}", message.getChannel());
                }

            } catch (ApplicationException e) {

                LOG.info("Mensagem descartada pelo Canal {} por Exception {} no envio : {}", message.getChannel(),
                        e.getMessage(), messageEnrichedObject);
                numberOfSubmissionAttemptsOnTheChannel = Constants.MAX_ATTEMPTS_FOR_CHANNEL;

            }

            indexOfTheLastEnrichedMessageSent++;
            numberOfSubmissionAttemptsOnTheChannel = 0;
            messageEnrichedObject.setIndexOfTheLastEnrichedMessageSent(indexOfTheLastEnrichedMessageSent);
            messageEnrichedObject.setNumberOfMessageSubmissions(numberOfSubmissionAttemptsOnTheChannel);
        }

        if (!msgSent && indexOfTheLastEnrichedMessageSent == messageEnrichedObject.getMessagesEnriched().size()) {
            LOG.info(
                    "Mensagem Descartada por esgotamento nas tentativas de envio para todos os canais configurados : {}",
                    messageEnrichedObject);
        }
    }

    public void sendToScheduler(MessageEnrichedObject eventMessageEnriched) {
        LOG.info("Enviando Mensagens Enriquecida para Scheduler: {}", eventMessageEnriched);
        kafkaSchedulerTopic.toTopic(eventMessageEnriched);
    }
}
