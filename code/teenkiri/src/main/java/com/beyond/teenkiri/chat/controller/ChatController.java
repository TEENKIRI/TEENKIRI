package com.beyond.teenkiri.chat.controller;

import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.service.ChatService;
import com.beyond.teenkiri.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;
    private final SimpMessageSendingOperations messagingTemplate;

    private static final Map<String, String> CHANNEL_TOPIC_MAP = new HashMap<>();

    static {
        CHANNEL_TOPIC_MAP.put("korean", "/topic/korean");
        CHANNEL_TOPIC_MAP.put("english", "/topic/english");
        CHANNEL_TOPIC_MAP.put("math", "/topic/math");
        CHANNEL_TOPIC_MAP.put("social", "/topic/social");
        CHANNEL_TOPIC_MAP.put("science", "/topic/science");
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        String channel = chatMessageDto.getChannel();

        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel must not be null or empty");
        }

        String topic = CHANNEL_TOPIC_MAP.getOrDefault(channel, null);

        if (topic == null) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }

        ChatMessageDto savedMessage = chatService.saveMessage(chatMessageDto);
        redisPublisher.publishMessage(topic, savedMessage);
    }

    @GetMapping("/subscribe/{channel}")
    public void subscribe(@PathVariable String channel) {
        String topic = CHANNEL_TOPIC_MAP.getOrDefault(channel, null);

        if (topic == null) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }

        messagingTemplate.convertAndSend(topic, "Subscribed to " + channel);
    }

    @PostMapping("/onMessage")
    public void onMessage(@RequestBody ChatMessageDto chatMessageDto) {
        String topic = CHANNEL_TOPIC_MAP.getOrDefault(chatMessageDto.getChannel(), null);

        if (topic == null) {
            throw new IllegalArgumentException("Invalid channel: " + chatMessageDto.getChannel());
        }
        messagingTemplate.convertAndSend(topic, chatMessageDto);
    }

    @PostMapping("/publish/{channel}")
    public void publishMessage(@PathVariable String channel, @RequestBody ChatMessageDto chatMessageDto) {
        String topic = CHANNEL_TOPIC_MAP.getOrDefault(channel, null);

        if (topic == null) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }
        redisPublisher.publishMessage(topic, chatMessageDto);
    }
}
