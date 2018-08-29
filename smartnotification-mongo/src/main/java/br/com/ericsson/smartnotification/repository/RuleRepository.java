package br.com.ericsson.smartnotification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ericsson.smartnotification.entities.Rule;

public interface RuleRepository extends MongoRepository<Rule, String>{

    public List<Rule> findByEventType(Integer eventType);
}
