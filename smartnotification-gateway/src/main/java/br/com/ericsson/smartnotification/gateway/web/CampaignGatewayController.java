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
import br.com.ericsson.smartnotification.entities.Campaign;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.business.CampaingBusiness;

@RestController
@RequestMapping(value = "/api/v1/campaign")
public class CampaignGatewayController {

    @Autowired
    private CampaingBusiness business;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> gatewayListener(@RequestBody Campaign campaign, HttpServletRequest request)
            throws ApplicationException {
        business.save(campaign);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, campaign.getId(), "Campanha Salva", request),
                HttpStatus.OK);
    }

}