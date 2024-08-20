package com.beyond.teenkiri.notification.repository;

import com.beyond.teenkiri.notification.dto.NotificationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDto, Long> {
    List<NotificationDto> findByUserEmail(String userEmail);
}

