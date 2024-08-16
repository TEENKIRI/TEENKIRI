package com.beyond.teenkiri.notification.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.notification.dto.NotificationDto;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class NotificationService{
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDto> getNotificationsByEmail() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return notificationRepository.findByUserEmail(userEmail);
    }

    public NotificationDto updateDelYN(Long id) {
        NotificationDto dto = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        dto.updateDelYN(DelYN.Y);
        return dto;
    }
}
