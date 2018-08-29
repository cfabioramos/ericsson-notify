package br.com.ericsson.smartnotification.route.component;

import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.component.sender.MessageServiceMediator;

@Component
public class RouteSendMessageSmsComponent extends MessageServiceMediator {

    @Override
    public boolean publish(MessageEnrichedObject messageEnriched) throws ApplicationException {
        // TODO send SMS
        String msisdn = messageEnriched.getMsisdn();

        if (msisdn.equals("5511999999913")) {
            
            LOG.info("Erro no envio do SMS: Conection timeout.");

            return false;
            
        }
        
        if (msisdn.equals("5511999999914")) {
            
            LOG.info("Erro no envio do SMS: Generic error.");

            return false;
            
        }

        LOG.info("Mensagem enviada com sucesso.");

        return true;
    }

    @Override
    protected int getMaxSizeMessage(MessageEnrichedObject messageEnriched) {
        return Constants.MAX_SIZE_SMS_MESSAGE;
    }
}
