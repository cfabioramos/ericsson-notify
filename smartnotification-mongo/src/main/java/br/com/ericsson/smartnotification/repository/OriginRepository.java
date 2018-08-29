package br.com.ericsson.smartnotification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.ericsson.smartnotification.entities.Origin;

public interface OriginRepository extends MongoRepository<Origin, String>{
	
	public Origin findByName(String name);

	public List<Origin> findByNameLike(String name);

}
