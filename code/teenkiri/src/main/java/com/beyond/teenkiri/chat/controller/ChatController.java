package com.beyond.teenkiri.chat.controller;

// 필요한 클래스와 어노테이션을 가져옴
import com.beyond.teenkiri.chat.domain.ChatMessage;
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations messagingTemplate;


    @GetMapping("/messages")
    public List<ChatMessageDto> getAllMessages(@RequestParam("since") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> since) {
        if (since.isPresent()) {
            // 특정 시간 이후의 메시지만 가져옴
            return chatMessageService.getMessagesSince(since.get());
        } else {
            // 모든 메시지를 가져옴
            return chatMessageService.getAllMessages();
        }
    }

    // 클라이언트가 /app/chat.sendMessage로 메시지를 보내면 호출되는 메서드
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        // 메시지를 저장하고
        ChatMessageDto savedMessage = chatMessageService.saveMessage(chatMessageDto.getContent(), chatMessageDto.getSenderId());

        // 사용자가 선택한 채널에 따라 다른 주제로 메시지를 전송
        String channel = chatMessageDto.getChannel(); // 클라이언트로부터 받은 채널 정보

        String topic;
        switch (channel) {
            case "korean":
                topic = "/topic/korean";
                break;
            case "english":
                topic = "/topic/english";
                break;
            case "math":
                topic = "/topic/math";
                break;
            case "social":
                topic = "/topic/social";
                break;
            case "science":
                topic = "/topic/science";
                break;
            default:
                throw new IllegalArgumentException("Invalid channel: " + channel);
        }
        // /topic/public 주제로 저장된 메시지를 전송함
        // messagingTemplate.convertAndSend()를 사용하면 다음과 같은 장점이 있음:
        // 1. 메시지를 보낼 주제를 동적으로 결정할 수 있음.
        // 2. 여러 주제로 동시에 메시지를 보낼 수 있음.
        // 3. 메시지를 전송하기 전에 추가적인 비즈니스 로직을 수행할 수 있음.
        messagingTemplate.convertAndSend("/topic/public", savedMessage);

    }

}
