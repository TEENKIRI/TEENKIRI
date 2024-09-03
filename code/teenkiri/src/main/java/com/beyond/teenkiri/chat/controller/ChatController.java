package com.beyond.teenkiri.chat.controller;

import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.service.ChatService;
import com.beyond.teenkiri.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final RedisPublisher redisPublisher;

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

    @PostMapping("/publish/{channel}")
    public void publishMessage(@PathVariable String channel, @RequestBody ChatMessageDto chatMessageDto) {
        String topic = CHANNEL_TOPIC_MAP.getOrDefault(channel, null);

        if (topic == null) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }
        redisPublisher.publishMessage(topic, chatMessageDto);
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> getMessages(@RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        return chatService.getMessagesSince(since);
    }
}
