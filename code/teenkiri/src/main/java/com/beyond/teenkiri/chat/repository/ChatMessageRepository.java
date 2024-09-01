package com.beyond.teenkiri.chat.repository;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 시간 이후의 메시지를 찾기 위한 메서드
    List<ChatMessage> findByCreatedTimeAfter(LocalDateTime since);
}
