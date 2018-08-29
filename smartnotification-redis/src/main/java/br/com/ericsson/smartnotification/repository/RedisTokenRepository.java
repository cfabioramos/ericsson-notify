package br.com.ericsson.smartnotification.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.interfaces.repository.TokenRepository;

@Repository
public class RedisTokenRepository implements TokenRepository {
    
    private static final String INDEX_TOKEN = "token:";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public void put(String key, String token) {
        redisTemplate.opsForValue().set(INDEX_TOKEN.concat(key), token);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(INDEX_TOKEN.concat(key));
    }

    public void delete(String key) {
        redisTemplate.delete(INDEX_TOKEN.concat(key));
    }

}
