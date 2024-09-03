package com.beyond.teenkiri.chat.domain;

import com.beyond.teenkiri.chat.dto.ChatMessageDto;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String channel;

    @CreationTimestamp
    private LocalDateTime createdTime;

    public static ChatMessageDto fromEntity(Chat chat) {
        return ChatMessageDto.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .userEmail(chat.getUser().getEmail())
                .createdTime(chat.getCreatedTime().toString())
                .channel(chat.getChannel())
                .build();
    }
}
