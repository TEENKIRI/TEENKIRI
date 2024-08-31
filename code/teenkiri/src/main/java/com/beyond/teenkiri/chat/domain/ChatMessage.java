package com.beyond.teenkiri.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    // 기본 키. 자동 생성됨
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메시지 내용
    @Column(nullable = false)
    private String content;

    // 메시지를 보낸 사람의 ID
    @Column(nullable = false)
    private Long senderId;

    // 메시지가 생성된 시간. 자동으로 현재 시간이 설정됨
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdTime;
}
