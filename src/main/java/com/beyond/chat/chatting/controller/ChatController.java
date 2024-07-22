package com.beyond.chat.chatting.controller;

import com.beyond.chat.chatting.domain.ChatMessage;
import com.beyond.chat.chatting.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

// 이 클래스는 채팅 메시지를 처리하는 컨트롤러입니다.
@Controller
public class ChatController {

    // ChatService를 자동으로 주입받아 사용합니다.
    @Autowired
    private ChatService chatService;

    // 클라이언트가 "/chat.sendMessage" 경로로 메시지를 보낼 때 호출됩니다.
    @MessageMapping("/chat.sendMessage")
    // 이 경로로 메시지를 브로드캐스트합니다.
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        // 메시지를 저장하고 저장된 메시지를 반환합니다.
        return chatService.saveChatMessage(message);
    }

    // 클라이언트가 "/chat/{username}" 경로로 GET 요청을 보낼 때 호출됩니다.
    @GetMapping("/chat/{username}")
    public ModelAndView chatIndex(@PathVariable String username) {
        // "chat/index" 뷰를 반환하고, 모델에 사용자 이름을 추가합니다.
        ModelAndView modelAndView = new ModelAndView("chat/index");
        modelAndView.addObject("username", username); // 사용자 이름을 모델에 추가
        return modelAndView;
    }
}
