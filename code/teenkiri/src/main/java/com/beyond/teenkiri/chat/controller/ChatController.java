package com.beyond.teenkiri.chat.controller;

// 필요한 클래스와 어노테이션을 가져옴
import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 모든 채팅 메시지 또는 특정 시간 이후의 채팅 메시지를 가져오는 API 엔드포인트
     *
     * @param since (Optional) 기준 시간 이후의 메시지만 가져오기 위한 파라미터
     * @return List<ChatMessageDto> 채팅 메시지 목록
     */
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

    /**
     * 클라이언트가 "/app/chat.sendMessage" 경로로 메시지를 보내면 호출되는 메서드
     * 클라이언트가 전송한 메시지를 저장하고 해당 주제로 전송한다.
     *
     * @param chatMessageDto 클라이언트로부터 받은 메시지 정보 (내용, 발신자 ID, 채널 정보)
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        // 사용자가 선택한 채널에 따라 적절한 주제를 결정
        String channel = chatMessageDto.getChannel(); // 클라이언트로부터 받은 채널 정보

        // 채널 정보가 null이거나 잘못된 경우 예외 처리
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel must not be null or empty");
        }

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
                // 유효하지 않은 채널의 경우 예외 발생
                throw new IllegalArgumentException("Invalid channel: " + channel);
        }

        // 메시지를 저장 (채널 정보를 추가로 전달)
        ChatMessageDto savedMessage = chatMessageService.saveMessage(chatMessageDto.getContent(), chatMessageDto.getUser(), channel);

        // 결정된 주제(topic)로 저장된 메시지를 전송함
        messagingTemplate.convertAndSend(topic, savedMessage);
    }
}
