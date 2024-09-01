package com.beyond.teenkiri.chat.service;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.repository.ChatMessageRepository;
import com.beyond.teenkiri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 금지된 단어 패턴을 저장하는 Set
    private Set<Pattern> forbiddenWordsPatterns = new HashSet<>();

    // 서비스 초기화 시 금지된 단어 목록을 로드
    @PostConstruct
    public void init() {
        loadForbiddenWordsFromFile();
    }

    // badwords.txt 파일에서 금지된 단어 목록을 로드하는 메서드
    private void loadForbiddenWordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("badwords.txt").getInputStream(), StandardCharsets.UTF_8))) {
            String word;
            while ((word = reader.readLine()) != null) {
                forbiddenWordsPatterns.add(Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE));
            }
        } catch (IOException e) {
            System.err.println("Error reading forbidden words file: " + e.getMessage());
        }
    }

    // 메시지를 저장하는 메서드
    public ChatMessageDto saveMessage(String content, Long senderId, String channel) {
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel must not be null or empty");
        }

        // 메시지의 내용을 필터링함 (예: 금지된 단어를 걸러냄)
        content = filterMessage(content);
        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .senderId(senderId)
                .channel(channel)
                .build();

        // 필터링된 메시지를 저장하고
        chatMessage = chatMessageRepository.save(chatMessage);

        // 사용자 닉네임을 가져와서
        String nickname = userRepository.findById(senderId).orElseThrow().getNickname();

        // DTO 객체로 변환하여 반환
        return ChatMessageDto.fromEntity(chatMessage, nickname);
    }

    // 모든 메시지를 가져오는 메서드
    public List<ChatMessageDto> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(chatMessage -> {
                    String nickname = userRepository.findById(chatMessage.getSenderId()).orElseThrow().getNickname();
                    return ChatMessageDto.fromEntity(chatMessage, nickname);
                })
                .collect(Collectors.toList());
    }

    // 메시지 필터링 메서드 (예: 금지된 단어를 *로 대체)
    public String filterMessage(String content) {
        for (Pattern pattern : forbiddenWordsPatterns) {
            content = pattern.matcher(content).replaceAll(m -> "*".repeat(m.group().length())); // 패턴과 일치하는 부분을 *로 대체
        }
        return content;
    }

    // 특정 시간 이후의 메시지를 가져오는 메서드
    public List<ChatMessageDto> getMessagesSince(LocalDateTime since) {
        return chatMessageRepository.findByCreatedTimeAfter(since).stream()
                .map(chatMessage -> {
                    String nickname = userRepository.findById(chatMessage.getSenderId()).orElseThrow().getNickname();
                    return ChatMessageDto.fromEntity(chatMessage, nickname);
                })
                .collect(Collectors.toList());
    }
}
