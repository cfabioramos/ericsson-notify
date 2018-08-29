package br.com.ericsson.smartnotification.gateway.business;

import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.gateway.component.OptOutComponent;
import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;

@RunWith(MockitoJUnitRunner.class)
public class OptOutTest {
    
    @Mock
    private OptOutRepository optoutRepository;

    @InjectMocks
    private OptOutComponent optoutServices;
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void optoutPutMustHaveMsisdn() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn é obrigatório.");

        optoutServices.optoutPut("");
    }

    @Test
    public void optoutDeleteMustHaveMsisdn() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn é obrigatório.");

        optoutServices.optoutDelete("");
    }
    
    @Test
    public void optoutPutMsisdnMaxSize() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn está no formato inválido.");

        String msisdn = repeat("*", 21);
        optoutServices.optoutPut(msisdn);
    }

    @Test
    public void optoutDeleteMsisdnMaxSize() throws ApplicationException {
        expectedException.expect(ApplicationException.class);
        expectedException.expectMessage("Requisição inválida : O msisdn possui quantidade de caracteres inválido.");

        String msisdn = repeat("1", 21);
        optoutServices.optoutDelete(msisdn);
    }
    
    @Test
    public void optoutTestInsert() throws ApplicationException {
        String msisdn = "5511999998888";
        optoutServices.optoutPut(msisdn);
        
        verify(optoutRepository).add(msisdn);
    }

    @Test
    public void testDeleteToken() throws ApplicationException {
        String msisdn = "5511999998888";
        optoutServices.optoutDelete(msisdn);

        verify(optoutRepository).remove(msisdn);
    }

    public static String repeat(String str, int times) {
        return new String(new char[times]).replace("\0", str);
    }
    
}
