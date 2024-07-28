package com.beyond.teenkiri.User.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveVerificationCode(String email, String code) {
        if (email == null || code == null) {
            throw new IllegalArgumentException("Email and code must not be null");
        }
        redisTemplate.opsForValue().set(email, code, 10, TimeUnit.MINUTES);  // 10분 동안 유효
    }

    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) {
            throw new IllegalArgumentException("Email and code must not be null");
        }
        Object cachedCode = redisTemplate.opsForValue().get(email);
        if (cachedCode != null && cachedCode.equals(code)) {
            redisTemplate.delete(email);  // 검증 후 코드 삭제
            return true;
        }
        return false;
    }
}