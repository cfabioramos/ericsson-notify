package br.com.ericsson.smartnotification.scheduler.business;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.EventMessageEnrichedScheduled;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.ShippingRestriction;
import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.kafka.KafkaTopicLogProducer;
import br.com.ericsson.smartnotification.repository.EventMessageEnrichedScheduledRepository;
import br.com.ericsson.smartnotification.utils.DateUtil;

@RunWith(value = MockitoJUnitRunner.class)
public class MessageEnchimentScheduledBusinesTest {

    @Mock
    private EventMessageEnrichedScheduledRepository scheduledRepository;

    @Mock
    private SchedulerBusiness routeBusiness;

    @Mock
    private KafkaTopicLogProducer logQueue;

    @InjectMocks
    private MessageEnchimentScheduledBusines busines;

    @Test
    public void publishMessagesEnrichedsScheduledsTest() throws ApplicationException {
        Date now = Calendar.getInstance().getTime();

        LocalTime time = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault()).toLocalTime();

        Pageable pageable = PageRequest.of(0, Constants.MAX_PAGINATION);
        List<EventMessageEnrichedScheduled> enrichedScheduleds = getMessagesEnrichedsScheduleds();

        when(scheduledRepository.createPageRequest(0)).thenReturn(pageable);

        when(scheduledRepository
                .findByEventMessageEnrichedShippingRestrictionDateStartBeforeAndEventMessageEnrichedShippingRestrictionDaysOfWeekContainingAndEventMessageEnrichedShippingRestrictionTimeStartLessThan(
                        now, DateUtil.getDayOfWeek(now), time, pageable)).thenReturn(enrichedScheduleds);

        busines.publishMessagesEnrichedsScheduleds(now);

        for (EventMessageEnrichedScheduled enrichedScheduled : enrichedScheduleds) {
            verify(scheduledRepository).delete(enrichedScheduled);
        }

    }

    private List<EventMessageEnrichedScheduled> getMessagesEnrichedsScheduleds() throws ApplicationException {

        List<EventMessageEnrichedScheduled> enrichedScheduleds = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            MessageEnrichedObject eventMessageEnriched = new MessageEnrichedObject(
                    new ShippingRestriction(DateUtil.parseToDate("2017-10-09"), Arrays.asList(DayOfWeek.values()),
                            LocalTime.MIN, LocalTime.MAX, 5, 0));
            eventMessageEnriched.getMessagesEnriched()
                    .add(new MessageEnriched("teste " + i, "message" + i, Channel.APP, "" + 1));
            EventMessageEnrichedScheduled eventMessageEnrichedScheduled = new EventMessageEnrichedScheduled(
                    eventMessageEnriched);
            enrichedScheduleds.add(eventMessageEnrichedScheduled);
        }

        return enrichedScheduleds;
    }

}
