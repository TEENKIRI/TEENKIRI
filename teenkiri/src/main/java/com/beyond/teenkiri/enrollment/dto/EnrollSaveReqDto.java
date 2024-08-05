package com.beyond.teenkiri.enrollment.dto;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollSaveReqDto {
    private String userEmail; // 🚨

    private Long lectureId;

    public Enrollment toEntity(Lecture lecture, User user) {
        return Enrollment.builder()
                .subject(lecture.getSubject())
                .lecture(lecture)
                .user(user)
                .build();
    }
}
