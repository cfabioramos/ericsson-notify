package br.com.ericsson.smartnotification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.entities.EventDefinition;

@Repository
public interface EventDefinitionRepository extends MongoRepository<EventDefinition, String> , PageableCreate{
    public Optional<EventDefinition> findByEventType(Integer eventType);
    
    public List<EventDefinition> findByActiveEqualsOrActiveNotNull(Boolean active, Pageable pageable);
}