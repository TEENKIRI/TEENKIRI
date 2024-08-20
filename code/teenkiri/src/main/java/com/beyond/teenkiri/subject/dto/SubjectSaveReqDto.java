package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.subject.domain.Grade;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectSaveReqDto {
    private String userTeacherEmail; // 연결되어있는 선생님 email

    private String title;
    private Grade grade; // 학년
    private Long courseId; // 과목 ID;

    private String description;
//    private MultipartFile subjectThum;

    private Boolean isMainSubject;

    public Subject toEntity(User userTeacher, Course course){
        return Subject.builder()
                .title(this.title)
                .grade(this.grade)
                .description(this.description)
                .userTeacher(userTeacher)
                .course(course)
                .build();
    }
}
