package br.com.ericsson.smartnotification.gateway.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.entities.CampaignRecipient;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.business.CampaingClientsBusiness;
import br.com.ericsson.smartnotification.gateway.domain.CampaingClients;
import br.com.ericsson.smartnotification.repository.CampaignRecipientRepository;

@RestController
@RequestMapping(value = "/api/v1/campaign/clients")
public class CampaignClientsGatewayController {


    @Autowired
    private CampaingClientsBusiness business;
    
    @Autowired
    private CampaignRecipientRepository campaignRecipientRepository;
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> gatewayListener(@RequestBody CampaingClients campaignClients, HttpServletRequest request)
            throws ApplicationException {
        business.save(campaignClients);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, campaignClients.getCampaignId(), "Clientes inseridos na campanha om sucesso", request),
                HttpStatus.OK);
    }
    
    @GetMapping("/{campaign}")
    public ResponseEntity<ApiResponse> gatewayListener(@PathVariable(name = "campaign") String campaign, HttpServletRequest request) {
        List<CampaignRecipient> campaignClients = campaignRecipientRepository.findByCampaignId(campaign, campaignRecipientRepository.createPageRequest(0));
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, campaignClients, "Sucesso", request),
                HttpStatus.OK);
    }

}