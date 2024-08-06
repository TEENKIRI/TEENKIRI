package com.beyond.teenkiri.user.dto;

import com.beyond.teenkiri.enrollment.dto.EnrollSaveReqDto;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSubjectSaveReqDto {
//    private String userEmail;
    private Long subjectId;

    public UserSubject toEntity(Subject subject, User user) {
        return UserSubject.builder()
                .user(user)
                .subject(subject)
                .build();
    }

    public EnrollSaveReqDto toEnrollEntity(User user,Lecture lecture) {
        return EnrollSaveReqDto.builder()
                .userEmail(user.getEmail())
                .lectureId(lecture.getId())
                .build();
    }
}
