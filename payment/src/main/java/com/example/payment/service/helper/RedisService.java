package com.example.payment.service.helper;

import com.example.payment.constants.ErrorCode;
import com.example.payment.exception.CustomException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getMessage(Long id) {

        var optionalValue = Optional.ofNullable(redisTemplate.opsForValue().get(String.valueOf(id)));

        if (optionalValue.isEmpty()) {
            throw new CustomException(ErrorCode.CUSTOMER_NOT_FOUN_IN_CACHE);
        }
        return (String) optionalValue.get();
    }
}
