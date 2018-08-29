package br.com.ericsson.smartnotification.gateway.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ericsson.smartnotification.domain.TokenKey;
import br.com.ericsson.smartnotification.domain.api.ApiResponse;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.component.TokenComponent;
import br.com.ericsson.smartnotification.gateway.domain.TokenFields;


@RestController
@RequestMapping(value="/api/token")
public class TokenController {
    
    @Autowired
    private TokenComponent tokenServices;
    
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> setToken(TokenFields tokenFields, HttpServletRequest request) throws ApplicationException {
        tokenServices.updateToken(tokenFields);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, HttpStatus.OK, "Token salvo com sucesso", request), HttpStatus.OK);
    }
 
    
    @GetMapping(value="/get")
    public ResponseEntity<ApiResponse> getToken(TokenKey tokenKey, HttpServletRequest request) throws ApplicationException {
        String tolken = tokenServices.getToken(tokenKey);
        return new ResponseEntity<>( new ApiResponse(HttpStatus.OK, tolken, "Sucesso", request), HttpStatus.OK);
    }

}
