package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.subject.domain.Grade;

public class SubjectUpdateReqDto {
    private String userTeacherEmail; // 연결되어있는 선생님 email

    private String title;
    private Grade grade; // 학년
    private Long courseId; // 과목 ID;

    private String description;
//    private MultipartFile subjectThum; // Controller 상에서 작업

}
