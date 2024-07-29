package com.beyond.chat.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 이 클래스가 스프링 설정 클래스임을 나타내는 애너테이션
@EnableWebSocketMessageBroker // WebSocket 메시지 브로커를 활성화하기 위한 애너테이션
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 엔드포인트를 등록합니다. SockJS를 사용하여 WebSocket이 지원되지 않는 브라우저에서도 동작하도록 합니다.
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:8080").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 애플리케이션 메시지의 목적지 접두사를 "/app"으로 설정합니다.
        registry.setApplicationDestinationPrefixes("/app");
        // "/topic" 접두사가 붙은 경로를 간단한 메모리 기반 메시지 브로커가 처리하도록 설정합니다.
        registry.enableSimpleBroker("/topic");
    }
}
