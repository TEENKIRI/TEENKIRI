package com.beyond.teenkiri.chat.service;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.repository.ChatMessageRepository;
import com.beyond.teenkiri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatMessageDto saveMessage(String content, Long senderId) {
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
}
