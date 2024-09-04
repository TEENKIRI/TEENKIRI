package com.beyond.teenkiri.chat.service;

import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publishMessage(String topic, ChatMessageDto chatMessageDto) {
        try {
            String message = objectMapper.writeValueAsString(chatMessageDto);
            redisTemplate.convertAndSend(topic, message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
