package com.beyond.teenkiri.notification.repository;

import com.beyond.teenkiri.notification.dto.NotificationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDto, Long> {
}
