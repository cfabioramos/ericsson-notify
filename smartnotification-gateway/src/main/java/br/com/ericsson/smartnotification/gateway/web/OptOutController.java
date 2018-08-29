package br.com.ericsson.smartnotification.gateway.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.component.OptOutComponent;




@RestController
@RequestMapping(value="/api/v1/optout")
public class OptOutController {

    @Autowired
    private OptOutComponent optoutServices;


    @RequestMapping(method=RequestMethod.PUT, value="/{msisdn}")
    public ResponseEntity<ApiResponse> put(@PathVariable(name = "msisdn") String msisdn, HttpServletRequest request) throws ApplicationException {
        optoutServices.optoutPut(msisdn);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, msisdn, "msisdn incluído no OptOut", request), HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/{msisdn}")
    public ResponseEntity<ApiResponse> delete(@PathVariable(name = "msisdn") String msisdn, HttpServletRequest request) throws ApplicationException {
        optoutServices.optoutDelete(msisdn);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, msisdn, "msisdn removido do OptOut", request), HttpStatus.OK);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/{msisdn}")
    public ResponseEntity<ApiResponse> get(@PathVariable(name = "msisdn") String msisdn, HttpServletRequest request) throws ApplicationException {
        optoutServices.optoutGet(msisdn);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, msisdn, "msisdn no OptOut", request), HttpStatus.OK);
    }

}
