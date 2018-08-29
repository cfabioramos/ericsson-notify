package br.com.ericsson.smartnotification.scheduler.component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.route.component.RouteFowardedNotificationsComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageAppComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageSmsComponent;
import br.com.ericsson.smartnotification.route.component.sender.MessageServiceMediatorFactory;
import br.com.ericsson.smartnotification.route.kafka.KafkaRouteSchedulerPriorityTopic;

@RunWith(MockitoJUnitRunner.class)
public class RouteSendMessageComponentTest {
    @Mock
    private RouteSendMessageAppComponent sendMessageApp;
    
    @Mock
    private RouteSendMessageSmsComponent sendMessageSms;
    
    @Mock
    private RouteFowardedNotificationsComponent fowardedNotifications;

    @Mock
    private KafkaRouteSchedulerPriorityTopic kafkaSchedulerTopic;
    
    @Mock
    private MessageServiceMediatorFactory messageServiceMediatorFactory;
    
    @InjectMocks
    private RouteSendMessageComponent routeSendMessageComponent;

    @Test
    public void testEnvioPrimeiraMensagem0TentativasSucesso() throws Exception {
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(0, 0);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(true);
        
        Mockito.when(messageServiceMediatorFactory.buildMessageServiceMediator(Channel.APP)).thenReturn(sendMessageApp);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(true);

        routeSendMessageComponent.enviarMensagens(eventMessageEnriched);
    }

    @Test
    public void testEnvioPrimeiraMensagem0TentativasFalha() throws Exception {
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(0, 0);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(true);
        
        Mockito.when(messageServiceMediatorFactory.buildMessageServiceMediator(Channel.APP)).thenReturn(sendMessageApp);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(false);

        routeSendMessageComponent.enviarMensagens(eventMessageEnriched);
    }

    @Test
    public void testEnvioPrimeiraMensagem2Tentativas() throws Exception {
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(0, 2);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(true);
        
        Mockito.when(messageServiceMediatorFactory.buildMessageServiceMediator(Channel.APP)).thenReturn(sendMessageApp);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(false);

        Mockito.when(messageServiceMediatorFactory.buildMessageServiceMediator(Channel.SMS)).thenReturn(sendMessageApp);

        routeSendMessageComponent.enviarMensagens(eventMessageEnriched);
    }

    @Test
    public void testEnvioSegundaMensagem2Tentativas() throws Exception {
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, 2);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(true);
        
        Mockito.when(messageServiceMediatorFactory.buildMessageServiceMediator(Channel.SMS)).thenReturn(sendMessageApp);
        Mockito.when(sendMessageApp.send(eventMessageEnriched)).thenReturn(false);


        routeSendMessageComponent.enviarMensagens(eventMessageEnriched);
    }

    private MessageEnrichedObject getEventMessageEnriched(int indexOfTheLastEnrichedMessageSent,
            int numberOfMessageSubmissions) {
        List<MessageEnriched> messagesEnriched = new ArrayList<MessageEnriched>();
        MessageEnriched message = new MessageEnriched("Titulo", "Mensagem", Channel.APP, "appId");
        messagesEnriched.add(message);
        message = new MessageEnriched("Titulo", "Mensagem", Channel.SMS, "appId");
        messagesEnriched.add(message);

        ShippingRestriction shippingRestriction = new ShippingRestriction(new Date(), new ArrayList<DayOfWeek>(),
                LocalTime.MIN, LocalTime.MAX, 5, 60);

        Event event = new Event(1, "Teste", LocalDateTime.now());
        List<EventField> eventFields = new ArrayList<EventField>();
        eventFields.add(new EventField("msisdn", "5511993399966"));
        event.setFields(eventFields);

        EnrichmentObject enrichmentTopicObject = new EventEnrichmentObject(TopicPriority.HIGH, event, "idTemplate");
        
        MessageEnrichedObject returnObject = new MessageEnrichedObject(messagesEnriched, shippingRestriction, enrichmentTopicObject);
        
        returnObject.setIndexOfTheLastEnrichedMessageSent(indexOfTheLastEnrichedMessageSent);
        returnObject.setNumberOfMessageSubmissions(numberOfMessageSubmissions);
        
        return returnObject;

    }
}
