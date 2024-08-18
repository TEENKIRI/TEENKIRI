package com.beyond.teenkiri.report.dto;


import com.beyond.teenkiri.report.domain.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDto {
    private Long id;
    private String reportEmail;
    private String suspectEmail;
    private Reason reason;
    private Long qnaId;
    private String details;
}
