package br.com.ericsson.smartnotification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.entities.FirebaseApp;

@Repository
public interface FirebaseAppRepository extends MongoRepository<FirebaseApp, String>{

}
