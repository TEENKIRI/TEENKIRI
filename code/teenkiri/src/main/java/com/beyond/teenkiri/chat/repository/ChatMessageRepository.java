package com.beyond.teenkiri.chat.repository;

import com.beyond.teenkiri.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByCreatedTimeAfter(LocalDateTime since);
}
