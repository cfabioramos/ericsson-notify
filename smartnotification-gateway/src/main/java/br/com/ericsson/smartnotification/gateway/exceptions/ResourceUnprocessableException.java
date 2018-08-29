package br.com.ericsson.smartnotification.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;


@ResponseStatus( value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceUnprocessableException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ResourceUnprocessableException(String resouce, String identify) {
        super(String.format("%s inativo para identificador %s", resouce, identify));
    }

}
