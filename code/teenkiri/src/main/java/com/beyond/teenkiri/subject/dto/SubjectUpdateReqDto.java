package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.subject.domain.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectUpdateReqDto {
    private String userTeacherEmail; // 연결되어있는 선생님 email

    private String title;

    @Enumerated(EnumType.STRING)
    private Grade grade; // 학년
    private Long courseId; // 과목 ID;

    private String description;
//    private MultipartFile subjectThum; // Controller 상에서 작업

    private Boolean isMainSubject;

}
