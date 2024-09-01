package com.beyond.teenkiri.notification.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notification.domain.Notification;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService{
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

//    public List<Notification> getNotificationsByEmail() {
//        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        return notificationRepository.findByUserEmail(userEmail);
//    }
    public List<Notification> getNotificationsByEmail() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> notifications = notificationRepository.findByUserEmail(userEmail);

        // Id 순으로 내림차순 정렬
        return notifications.stream()
                .sorted(Comparator.comparingLong(Notification::getId).reversed())
                .collect(Collectors.toList());
    }

    public void updateDelYN(Long id) {
        Notification dto = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        dto.updateDelYN(DelYN.Y);
    }
}
