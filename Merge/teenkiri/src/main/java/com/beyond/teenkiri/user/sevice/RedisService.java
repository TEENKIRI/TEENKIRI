package com.beyond.teenkiri.user.sevice;

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
        redisTemplate.opsForValue().set(email, code, 10, TimeUnit.MINUTES);
        System.out.println("Redis에 저장된 코드: " + code);  // 인증 코드 디버깅 출력
    }

    public boolean verifyCode(String email, String code) {
        if (email == null || code == null) {
            throw new IllegalArgumentException("Email and code must not be null");
        }
        Object cachedCode = redisTemplate.opsForValue().get(email);
        System.out.println("Redis에서 가져온 코드: " + cachedCode);  // 저장된 코드 디버깅 출력
        if (cachedCode != null && cachedCode.equals(code)) {
            redisTemplate.delete(email);  // 검증 후 코드 삭제
            return true;
        }
        return false;
    }
}
