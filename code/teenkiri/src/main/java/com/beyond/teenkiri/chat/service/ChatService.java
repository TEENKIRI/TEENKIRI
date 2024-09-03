package com.beyond.teenkiri.chat.service;

import com.beyond.teenkiri.chat.domain.Chat;
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.repository.ChatMessageRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatService implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    private Set<Pattern> forbiddenWordsPatterns = new HashSet<>();

    @PostConstruct
    public void init() {
        loadForbiddenWordsFromFile();
    }

    private void loadForbiddenWordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("badwords.txt").getInputStream(), StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                forbiddenWordsPatterns.add(Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE));
            }
        } catch (IOException e) {
            log.error("Error reading forbidden words file", e);
        }
    }

    private String filterMessage(String content) {
        for (Pattern pattern : forbiddenWordsPatterns) {
            content = pattern.matcher(content).replaceAll(m -> "*".repeat(m.group().length()));
        }
        return content;
    }

    public void publishMessage(String topic, ChatMessageDto chatMessageDto) {
        try {
            String message = objectMapper.writeValueAsString(chatMessageDto);
            redisTemplate.convertAndSend(topic, message);
        } catch (JsonProcessingException e) {
            log.error("Failed to publish message", e);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = new String(message.getBody());
            ChatMessageDto chatMessageDto = objectMapper.readValue(msg, ChatMessageDto.class);
            log.info("Received message from Redis: {}", chatMessageDto);

            messagingTemplate.convertAndSend("/topic/" + chatMessageDto.getChannel(), chatMessageDto);
        } catch (Exception e) {
            log.error("Failed to process received message", e);
        }
    }

    public ChatMessageDto saveMessage(String content, String userEmail, String channel) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String filteredContent = filterMessage(content);

        Chat chatMessage = Chat.builder()
                .content(filteredContent)
                .user(user)
                .channel(channel)
                .build();

        Chat savedMessage = chatMessageRepository.save(chatMessage);
        ChatMessageDto chatMessageDto = Chat.fromEntity(savedMessage);

        publishMessage("/topic/" + channel, chatMessageDto);

        return chatMessageDto;
    }

    public List<ChatMessageDto> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(Chat::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatMessageDto> getMessagesSince(LocalDateTime since) {
        return chatMessageRepository.findByCreatedTimeAfter(since).stream()
                .map(Chat::fromEntity)
                .collect(Collectors.toList());
    }
}
