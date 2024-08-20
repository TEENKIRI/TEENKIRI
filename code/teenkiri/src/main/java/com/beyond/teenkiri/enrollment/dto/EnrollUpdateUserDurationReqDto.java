package com.beyond.teenkiri.enrollment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollUpdateUserDurationReqDto {
//    private String userEmail;

//    private Long id; // enrollmentId
    private Integer userLectureDuration;
}
