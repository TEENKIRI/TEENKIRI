package com.beyond.teenkiri.chat.config;


import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지를 전달할 경로를 /topic 으로 설정.
        // 클라이언트는 /topic으로 시작하는 주소를 구독할 수 있음
        config.enableSimpleBroker("/topic");
        // 클라이언트가 메시지를 보낼 때 사용하는 주소의 앞부분을 /app으로 설정
        config.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 웹소켓 서버에 연결할 때 사용할 엔드포인트를 /ws로 설정
        // setAllowedOriginPatterns("*")는 모든 도메인에서의 접속을 허용함
        // withSockJS()는 SockJS를 사용하여 웹소켓을 지원하지 않는 브라우저도 사용할 수 있게 해줌
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
