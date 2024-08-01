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

@Controller // Spring MVC에서 컨트롤러임을 나타내는 애너테이션
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class); // 로깅을 위한 Logger 객체 생성

    @Autowired // Spring의 의존성 주입을 통해 ChatService를 자동으로 주입
    private ChatService chatService;

    @MessageMapping("/chat.sendMessage/{channel}") // 특정 채널로 메시지를 전송하기 위한 엔드포인트를 매핑
    @SendTo("/topic/{channel}") // 해당 채널의 구독자들에게 메시지를 전송
    public ChatMessage sendMessage(@DestinationVariable String channel, ChatMessage message) {
        message.setChannel(channel); // 메시지 객체에 채널 설정
        logger.info("Received message: {}", message); // 메시지 수신 로그 기록
        ChatMessage savedMessage = chatService.saveChatMessage(message); // 메시지를 저장하고 저장된 메시지 객체를 반환
        logger.info("Saved message: {}", savedMessage); // 저장된 메시지 로그 기록
        return savedMessage; // 저장된 메시지 반환
    }

    @GetMapping("/chat/{channel}/{username}") // 특정 채널과 사용자 이름으로 채팅 페이지를 반환하는 엔드포인트를 매핑
    public ModelAndView chatIndex(@PathVariable String channel, @PathVariable String username) {
        ModelAndView modelAndView = new ModelAndView("chat/index"); // "chat/index" 뷰를 반환하는 ModelAndView 객체 생성
        modelAndView.addObject("username", username); // 사용자 이름을 모델에 추가
        modelAndView.addObject("channel", channel); // 채널 이름을 모델에 추가
        return modelAndView; // ModelAndView 객체 반환
    }
}

