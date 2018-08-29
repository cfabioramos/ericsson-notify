package br.com.ericsson.smartnotification.route.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.domain.TokenKey;
import br.com.ericsson.smartnotification.domain.api.ResponseNotification;
import br.com.ericsson.smartnotification.domain.api.ResultNotification;
import br.com.ericsson.smartnotification.entities.FirebaseApp;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;
import br.com.ericsson.smartnotification.repository.FirebaseAppRepository;
import br.com.ericsson.smartnotification.route.component.sender.MessageServiceMediator;
import br.com.ericsson.smartnotification.route.utils.PushFirebaseApp;

@Component
public class RouteSendMessageAppComponent extends MessageServiceMediator {
    
    protected static final Logger LOG = LoggerFactory.getLogger(RouteSendMessageAppComponent.class);

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private FirebaseAppRepository firebaseAppRepository;
/*
    @Autowired
    private PushFirebaseApp pushFirebaseApp;
*/    
    @Override
    protected boolean publish(MessageEnrichedObject messageEnriched) throws ApplicationException {
        String idApp = messageEnriched.getMessagesEnriched().get(messageEnriched.getIndexOfTheLastEnrichedMessageSent()).getAppId();
        FirebaseApp firebaseApp = this.getApp(idApp);
        TokenKey tokenKey = new TokenKey(messageEnriched.getMsisdn(), firebaseApp.getName());
        String token = tokenRepository.get(tokenKey.getKey());
        
        if (StringUtils.isEmpty(token)) {

            throw new ApplicationException("Token não localizado.");

        } else {
            
            try {
                /*
                 * TODO
                MessageEnriched message = messageEnriched.getMessagesEnriched().get(messageEnriched.getIndexOfTheLastEnrichedMessageSent());
                ResponseNotification responseNotification = pushFirebaseApp.sendMessage(firebaseApp.getAuthorizationKey(), message.getTitle(), message.getMessage(), token);
                 * 
                 */
                String msisdn = messageEnriched.getMsisdn();
                ResponseNotification responseNotification;
                if (msisdn.equals("5511999999923")) {
                    throw new IOException("Conection timeout.");
                } else if (msisdn.equals("5511999999924")) {
                    responseNotification = new ResponseNotification(123456L, 0, 0, 0, new ArrayList<ResultNotification>());
                } else if (msisdn.equals("5511999999925")) {
                    responseNotification = new ResponseNotification(123456L, 0, 0, 1, new ArrayList<ResultNotification>());
                } else {
                    responseNotification = new ResponseNotification(123456L, 1, 0, 0, new ArrayList<ResultNotification>());
                }
                // FIM TODO
                
                if (responseNotification.isSucess()) {

                    LOG.info("Notificação enviada com sucesso.");

                    return true;

                } else {
                    if (responseNotification.getCanonicalIds()==1) {

                        tokenRepository.delete(tokenKey.getKey());
                        
                        LOG.info("Token invalido no envio do Push.");

                        throw new ApplicationException("Token inválido.");

                    } else {
                        
                        LOG.info("Falha no envio do Push : {}", responseNotification.getFailure());

                        throw new ApplicationException("Falha Generica.");

                    }
                }
                
            } catch (IOException e) {
                
                LOG.info("Falha no envio do Push : {}", e.getMessage());

                return false;

            }
        }
    }

    @Override
    protected int getMaxSizeMessage(MessageEnrichedObject messageEnriched) {
        return Constants.MAX_SIZE_APP_MESSAGE;
    }
    
    protected FirebaseApp getApp(String idApp) throws ApplicationException {
        LOG.info("Obtendo AppName : {}", idApp);
        Optional<FirebaseApp> optional = firebaseAppRepository.findById(idApp);
        if (optional.isPresent()) {
            LOG.info("App recuperada: {}", optional.get());
            return optional.get();
        } else {
            LOG.info("AppName não encontrado para id {}", idApp);
            throw new ApplicationException(String.format("AppName não encontrado para id %s", idApp));
        }
    }

}
