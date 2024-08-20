package com.beyond.teenkiri.chat.repository;

import com.beyond.teenkiri.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
