package com.example.customer.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Value("${spring.data.redis.timeout}")
    private long duration;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Long id, String value) {

        redisTemplate.opsForValue().set(String.valueOf(id), value, duration);
    }

    public void remove(Long id) {

        redisTemplate.delete(String.valueOf(id));
    }
}
