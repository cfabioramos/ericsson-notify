package br.com.ericsson.smartnotification.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.entities.EventMessageEnrichedScheduled;

@Repository
public interface EventMessageEnrichedScheduledRepository
        extends MongoRepository<EventMessageEnrichedScheduled, String>, PageableCreate {

    public List<EventMessageEnrichedScheduled> findByEventMessageEnrichedShippingRestrictionDateStartBeforeAndEventMessageEnrichedShippingRestrictionDaysOfWeekContainingAndEventMessageEnrichedShippingRestrictionTimeStartLessThan(
            Date dateStart, DayOfWeek dayOfWeek, LocalTime time, Pageable pageable);

}