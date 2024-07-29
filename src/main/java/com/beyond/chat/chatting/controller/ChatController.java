package com.beyond.chat.chatting.controller;

import com.beyond.chat.chatting.domain.ChatMessage;
import com.beyond.chat.chatting.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage/{channel}")
    @SendTo("/topic/{channel}")
    public ChatMessage sendMessage(@DestinationVariable String channel, ChatMessage message) {
        message.setChannel(channel); // 채널 설정
        logger.info("Received message: {}", message);
        ChatMessage savedMessage = chatService.saveChatMessage(message);
        logger.info("Saved message: {}", savedMessage);
        return savedMessage;
    }

    @GetMapping("/chat/{channel}/{username}")
    public ModelAndView chatIndex(@PathVariable String channel, @PathVariable String username) {
        ModelAndView modelAndView = new ModelAndView("chat/index");
        modelAndView.addObject("username", username); // 사용자 이름을 모델에 추가
        modelAndView.addObject("channel", channel); // 채널 이름을 모델에 추가
        return modelAndView;
    }
}
