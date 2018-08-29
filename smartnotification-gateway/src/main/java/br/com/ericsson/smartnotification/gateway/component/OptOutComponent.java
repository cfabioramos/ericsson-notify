package br.com.ericsson.smartnotification.gateway.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceNotFoundException;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;

@Component
public class OptOutComponent {

    @Autowired
    private OptOutRepository optoutRepository;

    public void optoutPut(String msisdn) throws ApplicationException {
        checkRequiredFields(msisdn);
        optoutRepository.add(msisdn);
    }

    public void optoutDelete(String msisdn) throws ApplicationException {
        checkRequiredFields(msisdn);
        optoutRepository.remove(msisdn);
    }

    public boolean optoutGet(String msisdn) throws ApplicationException {
        checkRequiredFields(msisdn);
        boolean optOut = optoutRepository.hasOptedOut(msisdn);
        
        if(!optOut) {
            throw new ResourceNotFoundException("OptOut", msisdn);
        }
        
        return optOut;
    }

    void checkRequiredFields(String msisdn) throws  ApplicationException{
        String regexOnlyNumber = "[0-9]+";
        String regexSize = "\\w{13,20}\\b";

        if (StringUtils.isEmpty(msisdn)) throw new ValidationException("O msisdn é obrigatório."); 
        
        if (!msisdn.matches(regexOnlyNumber)) throw new ValidationException("O msisdn está no formato inválido.");
        
        if (!msisdn.matches(regexSize)) throw new ValidationException("O msisdn possui quantidade de caracteres inválido.");
    }

}
