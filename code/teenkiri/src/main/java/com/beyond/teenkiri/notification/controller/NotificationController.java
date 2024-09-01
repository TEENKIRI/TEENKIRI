package com.beyond.teenkiri.notification.controller;

import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.notification.domain.Notification;
import com.beyond.teenkiri.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/list")
    public List<Notification> getNotifications() {
        return notificationService.getNotificationsByEmail();
    }

    @GetMapping("update/{id}")
    public ResponseEntity<?> NotificationUpdate(@PathVariable Long id){
        try {
            notificationService.updateDelYN(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "알림을 확인하였습니다..", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND,e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }


}
