package br.com.ericsson.smartnotification.route.component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ericsson.smartnotification.domain.ForwardedNotifications;
import br.com.ericsson.smartnotification.repository.RedisForwardedNotificationsRepository;

@Service
public class RouteFowardedNotificationsComponent {

    private static final Logger LOG = LoggerFactory.getLogger(RouteSendMessageComponent.class);

    @Autowired
    private RedisForwardedNotificationsRepository forwardedNotificationsRepository;

    public void updateForwardedNotifications(ForwardedNotifications forwardedNotifications) {

        ForwardedNotifications forwardedNotificationsDB = getForwardedNotifications(forwardedNotifications);

        if (forwardedNotificationsDB != null && forwardedNotificationsDB.getDateTimeLastSend() != null) {
            
            LocalDate dateLastSend = forwardedNotificationsDB.getDateTimeLastSend().toLocalDate();
            
            if (dateLastSend.isEqual(LocalDate.now())) {
                forwardedNotifications.setNumberOfSends(forwardedNotificationsDB.getNumberOfSends() + 1);
            } else {
                forwardedNotifications.setDateTimeLastSend(LocalDateTime.now());
                forwardedNotifications.setNumberOfSends(1);
            }
            
        } else {

            forwardedNotifications.setDateTimeLastSend(LocalDateTime.now());
            forwardedNotifications.setNumberOfSends(1);

        }

        LOG.info("Atualizando o envio para o msisdn {} no Redis, salvando : {}", forwardedNotifications.getMsisdn(), forwardedNotifications);

        forwardedNotificationsRepository.put(forwardedNotifications.getKey(), forwardedNotifications);
    }

    public ForwardedNotifications getForwardedNotifications(ForwardedNotifications forwardedNotifications) {

        return forwardedNotificationsRepository.get(forwardedNotifications.getKey());

    }
}
