package com.beyond.teenkiri.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollUpdateCompletedReqDto {
//    private String userEmail;
//    private Long id; // enrollmentId
    private Boolean isCompleted;

}
