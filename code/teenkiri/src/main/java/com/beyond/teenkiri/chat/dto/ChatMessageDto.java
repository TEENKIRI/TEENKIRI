package com.beyond.teenkiri.chat.dto;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// 이 클래스는 클라이언트와 데이터를 주고받기 위한 객체를 나타냄
@Getter
@Setter
@Builder
public class ChatMessageDto {
    private Long id;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String createdTime;
    private String channel;

    // 엔티티 객체를 DTO 객체로 변환하기 위한 메서드
    public static ChatMessageDto fromEntity(ChatMessage chatMessage, String nickname) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .senderNickname(nickname)
                .createdTime(chatMessage.getCreatedTime().toString())
                .channel(chatMessage.getChannel())
                .build();
    }
}
