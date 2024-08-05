package com.beyond.teenkiri.enrollment.dto;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollUpdateReqDto {
    private Long id; // enrollmentId
    private Float progress;
    private Boolean isCompleted;

}
