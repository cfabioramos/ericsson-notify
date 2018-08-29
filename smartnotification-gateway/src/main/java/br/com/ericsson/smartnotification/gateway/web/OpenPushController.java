package br.com.ericsson.smartnotification.gateway.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.gateway.business.OpenPushBusiness;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;

@RestController
@RequestMapping(value = "/api/v1/openedPush")
public class OpenPushController {

	@Autowired
	private OpenPushBusiness business;

	@PostMapping("/{notificationID}")
	public ResponseEntity<ApiResponse> setToken(@PathVariable(name = "notificationID") String notificationID,
			HttpServletRequest request) throws ValidationException {
		business.send(notificationID, request);
		return new ResponseEntity<>(
				new ApiResponse(HttpStatus.OK, HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.getReasonPhrase(), request), HttpStatus.OK);
	}

}