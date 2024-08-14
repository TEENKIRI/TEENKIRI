package com.beyond.teenkiri.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NotificationDto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long qnaId;
    private Long postId;
    private Long answerId;
    private String message;

    public NotificationDto(Long qnaId, Long postId, Long answerId, String message) {
        this.qnaId = qnaId;
        this.postId = postId;
        this.answerId = answerId;
        this.message = message;
    }
}
