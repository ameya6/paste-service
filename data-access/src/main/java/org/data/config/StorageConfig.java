package org.data.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;
import org.data.model.params.CacheParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Log4j2
@Configuration
public class StorageConfig {
    @Autowired
    CacheParams cacheParams;

    @Bean
    public JedisPooled jedisClient() {
        JedisPooled pool = new JedisPooled(cacheParams.getUrl(), cacheParams.getPort());
        log.info("Jedis connected " + pool.getPool().getResource().isConnected() + " at " + cacheParams);
        log.info("Redis ping " + pool.getPool().getResource().ping());
        return pool;
    }

    @Bean
    public RedisCommands<String, String> syncCommands() {
        RedisURI redisURI = RedisURI.Builder
                .redis(cacheParams.getUrl(), cacheParams.getPort())
                //.auth(password)
                .withDatabase(1)
                .build();
        RedisClient redisClient =  RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        return connection.sync();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                        .endpoint("192.168.0.107", 9000, false)
                        .credentials("admin", "password")
                        .build();
    }
}
