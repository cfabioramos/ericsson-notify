package br.com.ericsson.smartnotification.gateway.business;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.domain.log.LogPropertiesBuilder;
import br.com.ericsson.smartnotification.enums.Interfaces;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.log.component.LogComponent;

@Service
public class OpenPushBusiness {

	@Autowired
	private LogComponent logComponent;

	public void send(String notificationID, HttpServletRequest request) throws ValidationException {
		LogPropertiesBuilder logPropertiesBuilderInterface = LogPropertiesBuilder.getBuilder()
				.setNotificationId(notificationID).setInterfaceRequest(Interfaces.OPEN_PUSH).setRequest(request);
		logComponent.publishInterface(logPropertiesBuilderInterface.getLogProperties());
		try {
			validator(notificationID);
			logComponent.publishNotification(LogPropertiesBuilder.getBuilder().setNotificationId(notificationID)
					.setOpened("yes").setDateOpen(LocalDateTime.now()).getLogProperties());
		} catch (ValidationException e) {
			logComponent.publishInterface(logPropertiesBuilderInterface.setResponseCode("400")
					.setResponseDesc(e.getMessage()).getLogProperties());
			throw e;
		}
		logComponent.publishInterface(logPropertiesBuilderInterface.setResponseCode("200")
				.setResponseDesc(HttpStatus.ACCEPTED.getReasonPhrase()).getLogProperties());
	}

	private void validator(String notificationID) throws ValidationException {
		if (StringUtils.isEmpty(notificationID)) {
			throw new ValidationException(notificationID.concat(" inválido"));
		}
	}

}
