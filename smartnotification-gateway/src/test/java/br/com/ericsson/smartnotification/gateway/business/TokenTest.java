package br.com.ericsson.smartnotification.gateway.business;

import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.TokenKey;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.component.TokenComponent;
import br.com.ericsson.smartnotification.gateway.domain.TokenFields;
import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;

@RunWith(MockitoJUnitRunner.class)
public class TokenTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenComponent tokenServices;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void tokensMustHaveMsisdn() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O Campo msisdn é obrigatório.");

        TokenFields tokenpush = invalidTokenMissingMsisdn();
        tokenServices.updateToken(tokenpush);
    }

    @Test
    public void tokensMsisdnCaracterInvalid() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn está no formato inválido.");
        
        String msisdn = repeat("*", 21);
        TokenFields tokenpush = new TokenFields(msisdn, "MinhaClaro", "token123");
        tokenServices.updateToken(tokenpush);
    }
    
    @Test
    public void tokensMsisdnMaxSize() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn possui quantidade de caracteres inválido.");

        String msisdn = repeat("1", 21);
        TokenFields tokenpush = new TokenFields(msisdn, "MinhaClaro", "token123");
        tokenServices.updateToken(tokenpush);
    }

    @Test
    public void tokensMustHaveAppname() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O Campo appname é obrigatório.");

        TokenFields tokenpush = invalidTokenMissingAppname();
        tokenServices.updateToken(tokenpush);
    }


    @Test
    public void tokensAppnameMaxSize() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O Campo appname possui mais de 128 caracteres.");

        String appName = repeat("*", 129);
        TokenFields tokenpush = new TokenFields("5511999998888", appName, "token123");
        tokenServices.updateToken(tokenpush);
    }

    @Test
    public void tokensMustHaveToken() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O Campo token é obrigatório.");

        TokenFields tokenpush = invalidTokenMissingToken();
        tokenServices.updateToken(tokenpush);
    }
    
    @Test
    public void tokensTokenMaxSize() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O Campo token possui mais de 2024 caracteres.");

        String token = repeat("*", 2049);
        TokenFields tokenpush = new TokenFields("5511999998888", "MinhaClaro", token);
        tokenServices.updateToken(tokenpush);
    }

    @Test
    public void tokenTestInsert() throws ApplicationException {
        TokenFields fields = validToken();
        tokenServices.updateToken(fields);
        
        verify(tokenRepository).put(fields.getKey(),fields.getToken());
    }

    @Test
    public void tokenTestDelete() throws ApplicationException {
        TokenKey key = new TokenKey("5511999998888", "MinhaClaro");
        tokenServices.deleteToken(key);
        
        verify(tokenRepository).delete(key.getKey());
    }

    private TokenFields validToken() {
        return new TokenFields("5511999998888", "MinhaClaro", "token123");
    }

    private TokenFields invalidTokenMissingMsisdn() {
        return new TokenFields("", "MinhaClaro", "token123");
    }
    
    private TokenFields invalidTokenMissingAppname() {
        return new TokenFields("5511999998888", "", "token123");
    }

    private TokenFields invalidTokenMissingToken() {
        return new TokenFields("5511999998888", "MinhaClaro", "");
    }
    
    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
}
