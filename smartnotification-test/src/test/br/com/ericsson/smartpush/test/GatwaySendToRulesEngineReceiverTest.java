package br.com.ericsson.smartpush.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ericsson.smartpush.gateway.kafka.KafkaGatewaySender;
import br.com.ericsson.smartpush.rulesengine.kafka.KafkaRulesEngineReceiver;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class GatwaySendToRulesEngineReceiverTest {
    
    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "test.t");
    
    @Autowired
    private KafkaRulesEngineReceiver receiver;

    @Autowired
    private KafkaGatewaySender sender;

    @Test
    public void testReceive() throws Exception {
      sender.send("Teste Kafka!");

      receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
      assertThat(receiver.getLatch().getCount()).isEqualTo(0);
    }
    

}
