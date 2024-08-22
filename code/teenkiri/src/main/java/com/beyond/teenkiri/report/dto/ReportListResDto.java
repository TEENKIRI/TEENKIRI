package com.beyond.teenkiri.report.dto;

import com.beyond.teenkiri.report.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportListResDto {
    private Long id;
    private String reportEmail;
    private String suspectEmail;
    private Reason reason;
    private String details;
    private Long qnaId;
    private Long postId;
    private Long commentId;
    private Long commentPostId;
    private Long commentQnaId;
<<<<<<< HEAD
=======
    private Long chatMessageId;
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
    private LocalDateTime createdTime;
}
