package br.com.ericsson.smartnotification.scheduler.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.domain.TokenKey;
import br.com.ericsson.smartnotification.domain.api.ResponseNotification;
import br.com.ericsson.smartnotification.domain.api.ResultNotification;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.FirebaseApp;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;
import br.com.ericsson.smartnotification.repository.FirebaseAppRepository;
import br.com.ericsson.smartnotification.route.component.RouteFowardedNotificationsComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageAppComponent;
import br.com.ericsson.smartnotification.route.utils.PushFirebaseApp;

@RunWith(MockitoJUnitRunner.class)
public class RouteSendMessageAppComponentTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private KafkaTopicLogProducer logQueue;
    
    @Mock
    private FirebaseAppRepository firebaseAppRepository;
    
    @Mock
    private PushFirebaseApp pushFirebaseApp;

    @Mock
    private RouteFowardedNotificationsComponent routeFowardedNotificationsComponent;
    
    @InjectMocks
    private RouteSendMessageAppComponent sendMessageApp;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testMensagemComTamanhoExcedido() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Mensagem com tamanho maximo excedido.");
        
        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999999", "1", 0, 0, "mensagem com mais de 240 caracteres "+repeat("1", 240));
        sendMessageApp.send(eventMessageEnriched);
    }

    @Test
    public void testAppNameNaoLocalizado() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("AppName não encontrado para id 1");
        Mockito.when(firebaseAppRepository.findById("1")).thenReturn(Optional.empty());
        
        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999999", "1", 0, 0, "");
        sendMessageApp.send(eventMessageEnriched);
    }

    @Test
    public void testTokenNaoLocalizado() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Token não localizado.");
        Mockito.when(firebaseAppRepository.findById("1")).thenReturn(Optional.ofNullable(new FirebaseApp("1","AppName", true, "desc",Channel.APP,"11111111111111111111")));
        TokenKey tokenKey = new TokenKey("5511999999999", "AppName");
        Mockito.when(tokenRepository.get(tokenKey.getKey())).thenReturn("");
        
        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999999", "1", 0, 0, "");
        sendMessageApp.send(eventMessageEnriched);
    }

    @Test
    public void testSucessoChamadaFirebase() throws ApplicationException, IOException {
        Mockito.when(firebaseAppRepository.findById("1")).thenReturn(Optional.ofNullable(new FirebaseApp("1","AppName", true, "desc",Channel.APP,"11111111111111111111")));
        TokenKey tokenKey = new TokenKey("5511999999999", "AppName");
        Mockito.when(tokenRepository.get(tokenKey.getKey())).thenReturn("22222222222222222222");
        
        ResponseNotification responseNotification = new ResponseNotification(123456L, 1, 0, 0, new ArrayList<ResultNotification>());
        //TODO Mockito.when(pushFirebaseApp.sendMessage("11111111111111111111", "Titulo", "","22222222222222222222")).thenReturn(responseNotification);

        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999999", "1", 0, 0, "");
        
        assertThat(sendMessageApp.send(eventMessageEnriched)).isTrue();

    }

    @Test
    public void testSucessoChamadaFirebaseTokenInvalido() throws ApplicationException, IOException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Token inválido.");
        Mockito.when(firebaseAppRepository.findById("1")).thenReturn(Optional.ofNullable(new FirebaseApp("1","AppName", true, "desc",Channel.APP,"11111111111111111111")));
        TokenKey tokenKey = new TokenKey("5511999999925", "AppName");
        Mockito.when(tokenRepository.get(tokenKey.getKey())).thenReturn("22222222222222222222");
        
        Mockito.doNothing().when(tokenRepository).delete(tokenKey.getKey());
        
        ResponseNotification responseNotification = new ResponseNotification(123456L, 0, 0, 1, new ArrayList<ResultNotification>());
        // TODO Mockito.when(pushFirebaseApp.sendMessage("11111111111111111111", "Titulo", "","22222222222222222222")).thenReturn(responseNotification);

        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999925", "1", 0, 0, "");
        
        assertThat(sendMessageApp.send(eventMessageEnriched)).isTrue();
    }

    @Test
    public void testSucessoChamadaFirebaseTokenErroGenerico() throws ApplicationException, IOException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Falha Generica.");
        Mockito.when(firebaseAppRepository.findById("1")).thenReturn(Optional.ofNullable(new FirebaseApp("1","AppName", true, "desc",Channel.APP,"11111111111111111111")));
        TokenKey tokenKey = new TokenKey("5511999999924", "AppName");
        Mockito.when(tokenRepository.get(tokenKey.getKey())).thenReturn("22222222222222222222");
                
        ResponseNotification responseNotification = new ResponseNotification(123456L, 0, 0, 0, new ArrayList<ResultNotification>());
        // TODO Mockito.when(pushFirebaseApp.sendMessage("11111111111111111111", "Titulo", "","22222222222222222222")).thenReturn(responseNotification);

        MessageEnrichedObject eventMessageEnriched = getMessageEnrichedObject("5511999999924", "1", 0, 0, "");
        
        sendMessageApp.send(eventMessageEnriched);
    }

    private MessageEnrichedObject getMessageEnrichedObject(String msisdn, String appId, int indexOfTheLastEnrichedMessageSent,
            int numberOfMessageSubmissions, String mensagem) {
        List<MessageEnriched> MessagesEnriched = new ArrayList<MessageEnriched>();
        MessageEnriched message = new MessageEnriched("Titulo", mensagem, Channel.APP, appId);
        MessagesEnriched.add(message);
        message = new MessageEnriched("Titulo", "Mensagem", Channel.APP, "appId");
        MessagesEnriched.add(message);
        
        ShippingRestriction shippingRestriction = new ShippingRestriction(new Date(), new ArrayList<DayOfWeek>(),
                LocalTime.MIN, LocalTime.MAX, 5, 60);

        Event event = new Event(1, "Teste", LocalDateTime.now());
        List<EventField> eventFields = new ArrayList<EventField>();
        eventFields.add(new EventField("msisdn", msisdn));
        event.setFields(eventFields);

        EventEnrichmentObject enrichmentObject = new EventEnrichmentObject(TopicPriority.HIGH, event, "idTemplate");
        MessageEnrichedObject returnObject = new MessageEnrichedObject(MessagesEnriched, shippingRestriction, enrichmentObject);
        
        returnObject.setIndexOfTheLastEnrichedMessageSent(indexOfTheLastEnrichedMessageSent);
        returnObject.setNumberOfMessageSubmissions(numberOfMessageSubmissions);
        
        return returnObject;

    }

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }

}
