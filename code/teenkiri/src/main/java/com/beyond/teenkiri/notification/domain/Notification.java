package com.beyond.teenkiri.notification.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notification.dto.NotificationListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Notification extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long qnaId;

    private Long postId;

    private Long reportId;

    private String message;
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private DelYN delYN;


    public Notification saveDto(Long qnaId, Long postId, Long reportId, String userEmail, String message) {
        return Notification.builder()
                .id(this.id)
                .qnaId(qnaId)
                .postId(postId)
                .reportId(reportId)
                .message(message)
                .userEmail(userEmail)
                .delYN(DelYN.N)
                .build();
    }

    public NotificationListDto listFromEntity() {
        return NotificationListDto.builder()
                .id(this.id)
                .qnaId(qnaId)
                .postId(postId)
                .reportId(reportId)
                .message(message)
                .userEmail(userEmail)
                .createdTime(this.getCreatedTime())
                .build();
    }


    public void updateDelYN(DelYN delYN) {
        this.delYN = delYN;
    }
}
