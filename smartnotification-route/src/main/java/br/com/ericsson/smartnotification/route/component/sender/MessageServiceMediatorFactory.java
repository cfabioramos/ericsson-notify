package br.com.ericsson.smartnotification.route.component.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageAppComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageSmsComponent;
import br.com.ericsson.smartnotification.route.component.RouteSendMessageWebComponent;

@Service
public class MessageServiceMediatorFactory {

    @Autowired
    private RouteSendMessageAppComponent routeSendMessageAppComponent;

    @Autowired
    private RouteSendMessageSmsComponent routeSendMessageSmsComponent;

    @Autowired
    private RouteSendMessageWebComponent routeSendMessageWebComponent;

    private MessageServiceMediatorFactory() {
    }

    public MessageServiceMediator buildMessageServiceMediator(Channel channel) throws ApplicationException {
        if (Channel.APP.equals(channel))
            return routeSendMessageAppComponent;
        else if (Channel.SMS.equals(channel))
            return routeSendMessageSmsComponent;
        else if (Channel.WEB.equals(channel))
            return routeSendMessageWebComponent;
        else
            throw new ApplicationException("Canal inválido.");
    }

}
