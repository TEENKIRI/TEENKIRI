package com.beyond.teenkiri.chat.dto;

import com.beyond.teenkiri.chat.domain.Chat;
import com.beyond.teenkiri.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageDto {
    private Long id;
    private String content;
    private String email;
    private String senderNickname;
    private String createdTime;
    private String channel;

    public Chat toEntity(User user) {
        return Chat.builder()
                .content(this.content)
                .user(user)
                .channel(this.channel)
                .build();
    }

    public static ChatMessageDto fromEntity(Chat chat) {
        return ChatMessageDto.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .email(chat.getUser().getEmail())
                .senderNickname(chat.getUser().getNickname())
                .createdTime(chat.getCreatedTime().toString())
                .channel(chat.getChannel())
                .build();
    }
}
