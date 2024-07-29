package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.common.DelYN;
import com.beyond.teenkiri.subject.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDetResDto {
    private Long id;
    private String title;
    private Grade grade;
    private String userTeacherName;
    private String courseTitle;
    private String description;
    private Float rating;
    private DelYN delYN;
    private Boolean isSubscribe;
}
