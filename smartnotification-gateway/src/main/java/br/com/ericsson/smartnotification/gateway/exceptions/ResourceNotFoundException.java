package br.com.ericsson.smartnotification.gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;


@ResponseStatus( value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String resouce, String identify) {
        super(String.format("%s não encontrado para identificador %s", resouce, identify));
    }

}
