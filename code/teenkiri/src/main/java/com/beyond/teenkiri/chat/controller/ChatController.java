package com.beyond.teenkiri.chat.controller;

import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations messagingTemplate;

    @GetMapping("/messages")
    public List<ChatMessageDto> getAllMessages() {
        return chatMessageService.getAllMessages();
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        ChatMessageDto savedMessage = chatMessageService.saveMessage(chatMessageDto.getContent(), chatMessageDto.getSenderId());
        messagingTemplate.convertAndSend("/topic/public", savedMessage);
    }
}
