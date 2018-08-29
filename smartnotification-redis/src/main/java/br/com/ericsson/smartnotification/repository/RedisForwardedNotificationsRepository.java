package br.com.ericsson.smartnotification.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import br.com.ericsson.smartnotification.domain.ForwardedNotifications;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Repository
public class RedisForwardedNotificationsRepository {
    
    private static final String INDEX_FORWARDEDNOTIFICATIONS = "forwardednotifications:";
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    public void put(String key, ForwardedNotifications forwardedNotifications) {
        redisTemplate.opsForValue().set(INDEX_FORWARDEDNOTIFICATIONS.concat(key), forwardedNotifications.toString());
    }

    public ForwardedNotifications get(String key) {
        String object = redisTemplate.opsForValue().get(INDEX_FORWARDEDNOTIFICATIONS.concat(key));

        return JsonUtil.getGson().fromJson(object, ForwardedNotifications.class);
    }

    public void delete(String key) {
        redisTemplate.delete(INDEX_FORWARDEDNOTIFICATIONS.concat(key));
    }

}
