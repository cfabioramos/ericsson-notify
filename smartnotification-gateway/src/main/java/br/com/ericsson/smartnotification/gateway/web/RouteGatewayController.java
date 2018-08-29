package br.com.ericsson.smartnotification.gateway.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.entities.EventMessageEnrichedScheduled;
import br.com.ericsson.smartnotification.repository.EventMessageEnrichedScheduledRepository;

@RestController
@RequestMapping(value = "/api/v1/route")
public class RouteGatewayController {

    @Autowired
    private EventMessageEnrichedScheduledRepository scheduledRepository;

    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse> eventDefinition(@RequestBody EventMessageEnrichedScheduled eventMessageEnrichedScheduled,
            HttpServletRequest request) {
        this.scheduledRepository.save(eventMessageEnrichedScheduled);
        return new ResponseEntity<>(
                new ApiResponse(HttpStatus.OK, eventMessageEnrichedScheduled.getId(), "Mensagem Agendada", request),
                HttpStatus.OK);
    }

}