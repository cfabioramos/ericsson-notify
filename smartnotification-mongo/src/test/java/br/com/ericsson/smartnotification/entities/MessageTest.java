package br.com.ericsson.smartnotification.entities;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import br.com.ericsson.smartnotification.enums.Channel;


public class MessageTest {

    @Test
    public void getEventFieldsInTemplateTest() {
        Message message = message();
        String[] fields = message.getEnrichmentTemplateFields();

        assertArrayEquals(fields, new String[] { "timePromo", "promoName" });
    }

    @Test
    public void getOtherFieldsInTemplateTest() {
        Message message = message();
        String[] fields = message.getEnrichmentOtherFields();

        assertArrayEquals(fields, new String[] { "claroAPi.clientName", "claroApi.promoPoints" });
    }

    private Message message() {
        return new Message("teste", "Parabéns {claroAPi.clientName}! Voce completou {timePromo} ano(s) de {promoName} e por isso ganhou {claroApi.promoPoints} pontos. Acesse agora o site http://bit.ly/claroclubepos e saiba como aproveitar!", Channel.SMS, 1, "1");
    }
}
