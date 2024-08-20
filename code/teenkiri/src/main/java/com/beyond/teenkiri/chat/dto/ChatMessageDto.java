package com.beyond.teenkiri.chat.dto;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageDto {
    private Long id;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String createdTime;

    public static ChatMessageDto fromEntity(ChatMessage chatMessage, String nickname) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .senderNickname(nickname)
                .createdTime(chatMessage.getCreatedTime().toString())
                .build();
    }
}
