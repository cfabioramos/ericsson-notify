package br.com.ericsson.smartnotification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.entities.TemplateMessage;

@Repository
public interface TemplateMessageRepository extends MongoRepository<TemplateMessage, String> {
}