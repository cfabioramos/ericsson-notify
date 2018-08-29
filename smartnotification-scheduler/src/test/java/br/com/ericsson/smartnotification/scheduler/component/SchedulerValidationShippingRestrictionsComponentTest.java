package br.com.ericsson.smartnotification.scheduler.component;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.TopicPriority;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.repository.EventMessageEnrichedScheduledRepository;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerValidationShippingRestrictionsComponentTest {
    
    @Mock
    private EventMessageEnrichedScheduledRepository scheduleMessageRepository;

    @InjectMocks
    private SchedulerValidationShippingRestrictionsComponent schedulerValidationShippingRestrictionsComponent;
    
    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_SemRestricao() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, false, Arrays.asList(DayOfWeek.values()), LocalTime.MIN, LocalTime.MAX);
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isTrue();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaSemIntervalos() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), null, null);
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isTrue();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloStart() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), getTimeStartToSend(true), null);
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isTrue();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloEnd() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), null, getTimeEndToSend(true));
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isTrue();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloStartEndValido() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), getTimeStartToSend(true), getTimeEndToSend(true));
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isTrue();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloStartInvalidoEndValido() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), getTimeStartToSend(false), getTimeEndToSend(true));
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isFalse();
    }

    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloStartValidoEndInvalido() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), getTimeStartToSend(true), getTimeEndToSend(false));
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isFalse();
    }
    
    @Test
    public void evaluateRouteValidationShippingRestrictionsComponent_ComRestricaoMesmoDiaComIntervaloStartInvalidoEndInvalido() throws ApplicationException {    
        MessageEnrichedObject eventMessageEnriched = getEventMessageEnriched(1, true, getDiaSemana(true), getTimeStartToSend(false), getTimeEndToSend(false));
        assertThat(schedulerValidationShippingRestrictionsComponent.isValid(eventMessageEnriched)).isFalse();
    }

    private MessageEnrichedObject getEventMessageEnriched(int id, boolean isSendRestrict, List<DayOfWeek> daysOfWeek, LocalTime timeStartToSend, LocalTime timeEndToSend) {
        Event event = new Event(id, "Evento "+Integer.toString(id));
        
        MessageEnriched messageEnriched = new MessageEnriched("title", "Mensagem a ser enviada", Channel.APP, "1");
        MessageEnrichedObject eventMessageEnriched = new MessageEnrichedObject(new ShippingRestriction());
        eventMessageEnriched.setEventEnrichmentObject(new EventEnrichmentObject(TopicPriority.HIGH, event, "idTemplate"));
        eventMessageEnriched.getMessagesEnriched().add(messageEnriched);
        eventMessageEnriched.getShippingRestriction().setDaysOfWeek(daysOfWeek);
        eventMessageEnriched.getShippingRestriction().setTimeStart(timeStartToSend);
        eventMessageEnriched.getShippingRestriction().setTimeEnd(timeEndToSend);
        return eventMessageEnriched;
    }
    private List<DayOfWeek> getDiaSemana(boolean validDay) {
        DayOfWeek diaSemanaDataAtual = LocalDate.now().getDayOfWeek();
        if (!validDay) {
            diaSemanaDataAtual = LocalDate.now().plusDays(1).getDayOfWeek();
        }
        List<DayOfWeek> diasValidos = new ArrayList<DayOfWeek>();
        diasValidos.add(diaSemanaDataAtual);
        return diasValidos;
    }
    private LocalTime getTimeStartToSend(boolean validTime) {
        LocalTime horario = LocalTime.of(0,0,1);
        if (!validTime) {
            horario = LocalTime.of(23,0,0);
        }
        return horario;
    }
    private LocalTime getTimeEndToSend(boolean validTime) {
        LocalTime horario = LocalTime.of(23,59,59);
        if (!validTime) {
            horario = LocalTime.of(0,0,1);
        }
        return horario;
    }
}
