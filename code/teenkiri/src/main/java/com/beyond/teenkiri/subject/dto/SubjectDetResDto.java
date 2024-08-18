package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.subject.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Boolean isSubscribe; //로그인한 유저가 찜 해놓았는지
    private Boolean isRegistered; //로그인한 유저가 수강신청을 했는지

    private String subjectThumUrl;

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;
}
