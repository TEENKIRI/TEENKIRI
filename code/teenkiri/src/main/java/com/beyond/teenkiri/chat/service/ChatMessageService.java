package com.beyond.teenkiri.chat.service;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.repository.ChatMessageRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private Set<Pattern> forbiddenWordsPatterns = new HashSet<>();


    public ChatMessageDto saveMessage(String content, Long senderId) {
        content = filterMessage(content);
        ChatMessage chatMessage = ChatMessage.builder()
                .content(content)
                .senderId(senderId)
                .build();

        chatMessage = chatMessageRepository.save(chatMessage);
        String nickname = userRepository.findById(senderId).orElseThrow().getNickname();
        return ChatMessageDto.fromEntity(chatMessage, nickname);
    }


    public List<ChatMessageDto> getAllMessages() {
        return chatMessageRepository.findAll().stream()
                .map(chatMessage -> {
                    String nickname = userRepository.findById(chatMessage.getSenderId()).orElseThrow().getNickname();
                    return ChatMessageDto.fromEntity(chatMessage, nickname);
                })
                .collect(Collectors.toList());
    }

    public String filterMessage(String content) {
        for (Pattern pattern : forbiddenWordsPatterns) {
            content = pattern.matcher(content).replaceAll(m -> "*".repeat(m.group().length())); // 패턴과 일치하는 부분을 *로 대체
        }
        return content;
    }

    public List<ChatMessageDto> getMessagesSince(LocalDateTime since) {
        return chatMessageRepository.findByCreatedTimeAfter(since).stream()
                .map(chatMessage -> {
                    String nickname = userRepository.findById(chatMessage.getSenderId()).orElseThrow().getNickname();
                    return ChatMessageDto.fromEntity(chatMessage, nickname);
                })
                .collect(Collectors.toList());
    }
}
