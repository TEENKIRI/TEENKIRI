package com.beyond.teenkiri.notification.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private String userEmail;
    private DelYN delYN;

    public NotificationDto(Long qnaId, Long postId, Long answerId, String userEmail, String message) {
        this.qnaId = qnaId;
        this.postId = postId;
        this.answerId = answerId;
        this.message = message;
        this.userEmail= userEmail;
        this.delYN = DelYN.N;
    }

    public void updateDelYN(DelYN delYN){
        this.delYN = delYN;
    }

}
