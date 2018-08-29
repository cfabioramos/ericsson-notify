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
import br.com.ericsson.smartnotification.entities.EventDefinition;
import br.com.ericsson.smartnotification.entities.FirebaseApp;
import br.com.ericsson.smartnotification.entities.Rule;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.repository.EventDefinitionRepository;
import br.com.ericsson.smartnotification.repository.FirebaseAppRepository;
import br.com.ericsson.smartnotification.repository.RedisForwardedNotificationsRepository;
import br.com.ericsson.smartnotification.repository.RuleRepository;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;

@RestController
@RequestMapping(value = "/api/v1/rules-engine")
public class RulesEngineGatewayController {

    @Autowired
    private FirebaseAppRepository firebaseAppRepository;

    @Autowired
    private EventDefinitionRepository eventDefinitionRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TemplateMessageRepository templateMessageRepository;
    
    @Autowired
    private RedisForwardedNotificationsRepository forwardedNotificationsRepository;

    @PostMapping("/eventDefinition")
    public ResponseEntity<ApiResponse> eventDefinition(@RequestBody EventDefinition eventDefinition,
            HttpServletRequest request) {
        this.eventDefinitionRepository.save(eventDefinition);
        return new ResponseEntity<>(
                new ApiResponse(HttpStatus.OK, eventDefinition.getId(), "EventDefinition salvo", request),
                HttpStatus.OK);
    }

    @PostMapping("/rule")
    public ResponseEntity<ApiResponse> rule(@RequestBody Rule rule, HttpServletRequest request) {
        this.ruleRepository.save(rule);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, rule.getId(), "Regra salva", request),
                HttpStatus.OK);
    }

    @PostMapping("/template")
    public ResponseEntity<ApiResponse> template(@RequestBody TemplateMessage templateMessage,
            HttpServletRequest request) {
        this.templateMessageRepository.save(templateMessage);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, templateMessage.getId(), "Template salvo", request),
                HttpStatus.OK);
    }
    
    
    @PostMapping("/firebaseapp")
    public ResponseEntity<ApiResponse> firebaseapp(@RequestBody FirebaseApp firebaseApp,
            HttpServletRequest request) {
        this.firebaseAppRepository.save(firebaseApp);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, firebaseApp.getId(), "FirebaseApp salvo", request),
                HttpStatus.OK);
    }
    
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> template(HttpServletRequest request) {
        this.firebaseAppRepository.deleteAll();
        this.templateMessageRepository.deleteAll();
        this.ruleRepository.deleteAll();
        this.eventDefinitionRepository.deleteAll();
        this.forwardedNotificationsRepository.delete("forwardednotifications:5511999999991:912");
        this.forwardedNotificationsRepository.delete("forwardednotifications:5511999999991:911");
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, HttpStatus.OK, "Banco de dados limpo", request),
                HttpStatus.OK);
    }

}