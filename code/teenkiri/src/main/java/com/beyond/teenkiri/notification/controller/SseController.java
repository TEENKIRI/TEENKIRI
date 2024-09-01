package com.beyond.teenkiri.notification.controller;


import com.beyond.teenkiri.notification.domain.Notification;
import com.beyond.teenkiri.notification.service.NotificationService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j

@RestController
public class SseController implements MessageListener {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Set<String> subscribeList = ConcurrentHashMap.newKeySet();
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisTemplate<String, Object> sseRedisTemplate;
    private final NotificationService notificationService;

    public SseController(@Qualifier("sseRedisTemplate") RedisTemplate<String, Object> sseRedisTemplate,
                         @Qualifier("sseRedisContainer") RedisMessageListenerContainer redisMessageListenerContainer, NotificationService notificationService) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.notificationService = notificationService;
    }

//    @GetMapping("/subscribe")
//    public SseEmitter subscribe() {
//        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L); // 4시간 동안 연결 유지
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        emitters.put(email, emitter);
//
//        emitter.onCompletion(() -> emitters.remove(email));
//        emitter.onTimeout(() -> emitters.remove(email));
//
//        try {
//            emitter.send(SseEmitter.event().name("connect").data("Connected"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        subscribeChannel(email);
//        return emitter;
//    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L); // 4시간 동안 연결 유지
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        emitters.put(email, emitter);

        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));

        subscribeChannel(email);

        return emitter;
    }

    private void subscribeChannel(String email) {
        if (!subscribeList.contains(email)) {
            MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(this, "onMessage");
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("연결 개수 : " + emitters.size());
        System.out.println("이름 : " + emitters.toString());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Notification notification = objectMapper.readValue(message.getBody(), Notification.class);
            String email = new String(pattern, StandardCharsets.UTF_8);
            SseEmitter emitter = emitters.get(email);

            if (emitter != null) {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                String body = new String(message.getBody());
                String channel = new String(message.getChannel());
                log.info("Received message: {} from channel: {}", body, channel);
                System.out.println("Received message: {} from channel: {}" + body + "   "+ channel);

            }
        } catch (IOException e) {
            // 로깅 추가
            System.err.println("Failed to process message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void publishMessage(Notification dto) {
//        NotificationDto dto = new NotificationDto();
//        dto.setQnaId(qnaId);
//        dto.setPostId(postId);
//        dto.setUserEmail(userEmail);
//        dto.setMessage(message);
//        dto.setDelYN(DelYN.N);
//        dto.saveDto(qnaId, postId, userEmail, message);

        String userEmail = dto.getUserEmail();
        SseEmitter emitter = emitters.get(userEmail);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(dto));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            sseRedisTemplate.convertAndSend(userEmail, dto);
        }
    }

}
