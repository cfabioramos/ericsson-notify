package br.com.ericsson.smartnotification.gateway.web;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.business.EventBusiness;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;

@RestController
@RequestMapping(value="/api/v1/event")
public class EventGatewayController {

    @Autowired
    private EventBusiness busines;
    

    @PostMapping("")
    public ResponseEntity<ApiResponse>  gatewayListener(HttpServletRequest request) throws ApplicationException {
        Event event = Event.buildEvent(this.getParameters(request));
        event.setDateTimeReceived(LocalDateTime.now());
        this.busines.publishEvent(event);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, event.getId(), "Evento incluído na fila de procesamento", request), HttpStatus.OK);
    }
    
    
    private Map<String, String> getParameters(HttpServletRequest request) throws ValidationException{
        Map<String, String> parameters = new HashMap<>();
        for(String name : Collections.list(request.getParameterNames())) {
            parameters.put(name, request.getParameterValues(name)[0]);
        }
        
        if(parameters.isEmpty()) {
            throw new ValidationException("Request sem parâmetros");
        }
        
        return parameters;
    }
    
    
}