package br.com.ericsson.smartnotification.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.interfaces.repository.OptOutRepository;



@Repository
public class RedisOptOutRepository implements OptOutRepository {

    private static final String INDEX_OPT_OUT = "optOut:";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void add(String msisdn) {
        redisTemplate.opsForValue().set(INDEX_OPT_OUT.concat(msisdn), String.valueOf(true));
    }

    @Override
    public boolean hasOptedOut(String msisdn) {
        String optOut = redisTemplate.opsForValue().get(INDEX_OPT_OUT.concat(msisdn));
        return !StringUtils.isEmpty(optOut) ? Boolean.parseBoolean(optOut) : false;
    }

    @Override
    public void remove(String msisdn) {
        redisTemplate.delete(INDEX_OPT_OUT.concat(msisdn));
    }

}
