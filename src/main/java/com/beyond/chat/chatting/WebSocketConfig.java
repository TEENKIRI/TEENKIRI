package com.beyond.chat.chatting;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// 이 클래스는 WebSocket을 설정 클래스
@Configuration
// WebSocket 메시지 브로커 활성화
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 이 메서드는 클라이언트가 WebSocket 서버에 연결할 수 있는 엔드포인트를 등록합니다.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 "/ws" 엔드포인트를 통해 WebSocket 서버에 연결할 수 있습니다.
        // setAllowedOrigins("http://localhost:8080")는 CORS 설정을 통해 이 출처에서의 요청을 허용
        // withSockJS()는 SockJS 폴백 옵션을 활성화하여 WebSocket을 지원하지 않는 브라우저에서도 연결이 가능합니다.
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:8080").withSockJS();
    }

    // 이 메서드는 메시지 브로커를 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지를 보낼 때 사용할 애플리케이션 목적지 접두사를 설정합니다.
        // 클라이언트는 "/app" 접두사를 사용하여 메시지를 서버로 보냅니다.
        registry.setApplicationDestinationPrefixes("/app");
        // "/topic" 접두사를 사용하는 목적지로 보내진 메시지는 메시지 브로커가 처리하고, 해당 주제를 구독한 클라이언트들에게 브로드캐스트합니다.
        registry.enableSimpleBroker("/topic");
    }
}
