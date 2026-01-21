package com.lotus.check_point.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory conectionFactory) {
        RedisTemplate<Object, Object> resdisTemplate = new RedisTemplate<>();
        resdisTemplate.setConnectionFactory(conectionFactory);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        resdisTemplate.setKeySerializer(new StringRedisSerializer());
        resdisTemplate.setValueSerializer(serializer);

        resdisTemplate.setHashKeySerializer(new StringRedisSerializer());
        resdisTemplate.setHashValueSerializer(serializer);

        resdisTemplate.afterPropertiesSet();
        return resdisTemplate;
    }
}