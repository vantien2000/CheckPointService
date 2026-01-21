package com.lotus.check_point.cache.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lotus.check_point.cache.RedisService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public boolean delete(String Key) {
        return false;
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(obj -> {
                try {
                    return targetClass.cast(obj);
                } catch (ClassCastException e) {
                    log.error("Failed to cast object from Redis for key {}: {}", key, e.getMessage());
                    return null;
                }
            }).orElse(null);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
        .map(String::valueOf)
        .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        log.info("SET CACHE: {}", key);
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES); //TTL 5 phút
    }

    @Override
    public void setString(String key, String value) {
        log.info("SET CACHE: {}", key);
        redisTemplate.opsForValue().set(key, value, 10, TimeUnit.MINUTES); //TTL 5 phút
    }
    
}
