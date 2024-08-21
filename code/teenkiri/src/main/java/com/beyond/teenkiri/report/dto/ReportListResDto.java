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
    private Long chatMessageId;
    private LocalDateTime createdTime;
}
