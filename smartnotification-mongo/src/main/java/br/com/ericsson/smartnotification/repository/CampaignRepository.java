package br.com.ericsson.smartnotification.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ericsson.smartnotification.entities.Campaign;

public interface CampaignRepository extends MongoRepository<Campaign, String>, PageableCreate{
    
    public List<Campaign> findByStartDateBeforeAndEndDateAfterAndActiveTrue(
            LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd, Pageable pageable);
    
    public List<Campaign> findByStartDateBeforeAndEndDateIsNullAndActiveTrue(
            LocalDateTime dateTimeStart, Pageable pageable);

}
