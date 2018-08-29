package br.com.ericsson.smartnotification.gateway.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.domain.TokenKey;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.domain.TokenFields;
import br.com.ericsson.smartnotification.gateway.exceptions.ResourceNotFoundException;
import br.com.ericsson.smartnotification.gateway.exceptions.ValidationException;
import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;

@Component
public class TokenComponent {

    @Autowired
    private TokenRepository tokenRepository;

    public void updateToken(TokenFields tokenFields) throws ApplicationException {
        checkRequiredFields(tokenFields);

        tokenRepository.put(tokenFields.getKey(), tokenFields.getToken());
    }

    public String getToken(TokenKey tokenKey) throws ApplicationException {
        String token = tokenRepository.get(tokenKey.getKey());
        if(StringUtils.isEmpty(token)) {
            throw new ResourceNotFoundException("Token", tokenKey.getKey());
        }
        return token;
    }

    public void deleteToken(TokenKey tokenKey) throws ApplicationException {
        tokenRepository.delete(tokenKey.getKey());
    }

    void checkRequiredFields(TokenFields tokenFields) throws ApplicationException {
        if (StringUtils.isEmpty(tokenFields.getMsisdn())) throw new ValidationException("O Campo msisdn é obrigatório.");
        if (StringUtils.isEmpty(tokenFields.getAppname())) throw new ValidationException("O Campo appname é obrigatório.");
        if (StringUtils.isEmpty(tokenFields.getToken())) throw new ValidationException("O Campo token é obrigatório.");
        
        String regexOnlyNumber = "[0-9]+";
        String regexSizeform13To20 = "\\w{13,20}\\b";
        String regexSizeform1To128 = "\\w{1,128}\\b";
        String regexSizeform1To2024 = "\\w{1,2024}\\b";
        
        
        if (!tokenFields.getMsisdn().matches(regexOnlyNumber)) throw new ValidationException("O msisdn está no formato inválido.");
        
        if (!tokenFields.getMsisdn().matches(regexSizeform13To20)) throw new ValidationException("O msisdn possui quantidade de caracteres inválido.");
        
        if (!tokenFields.getAppname().matches(regexSizeform1To128)) throw new ValidationException("O Campo appname possui mais de 128 caracteres.");

        if (!tokenFields.getToken().matches(regexSizeform1To2024)) throw new ValidationException("O Campo token possui mais de 2024 caracteres.");
    }
    
}
