package com.beyond.teenkiri.chat.repository;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByCreatedTimeAfter(LocalDateTime since);
}
