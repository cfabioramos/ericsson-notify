package br.com.ericsson.smartnotification.route.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;

@Component
public class TokenComponent {

    private static final Logger LOG = LoggerFactory.getLogger(TokenComponent.class);
    
    @Autowired
    private TokenRepository tokenRepository;
    
    public String get(String msisdn) {
        LOG.info("Verificando OptOut para msisdn {}", msisdn);
        return this.get(msisdn);
    }
    
}
