package com.beyond.teenkiri.subject.dto;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.user_board.domain.User;
import com.beyond.teenkiri.subject.domain.Grade;
import com.beyond.teenkiri.subject.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectSaveReqDto {
    private String userEmail; // 🚨 멤버 생성 시 삭제

    private String title;
    private Grade grade; // 학년
    private Long courseId; // 과목 ID;

    private String description;

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
