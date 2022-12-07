package org.data.dao;

import org.data.model.response.PasteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;

@Repository
public class CacheDao<T> {

    private static final Long SECONDS_IN_A_DAY = 86400L;
    @Autowired
    private JedisPooled jedisClient;

    public String save(String key, T t) {
        return save(key, "$", t);
    }

    public String save(String key, T t, boolean expiry) {
        String response = save(key, "$", t);
        if(expiry)
            jedisClient.expire(key, SECONDS_IN_A_DAY);
        return response;
    }

    public String save(String key, String path, T t) {
        return jedisClient.jsonSet(key, new Path(path), t);
    }

    public T get(String key, Class<T> clazz) {
        return jedisClient.jsonGet(key, clazz);
    }
}
