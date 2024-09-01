
package com.beyond.teenkiri.notification.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationListDto {
    private Long id;

    private Long qnaId;

    private Long postId;

    private Long reportId;

    private String message;
    private String userEmail;

    private LocalDateTime createdTime;
}
