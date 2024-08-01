package com.beyond.teenkiri.enrollment.dto;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.lecture.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollSaveReqDto {
    private Long userId; // 🚨

    private Long lectureId;

    public Enrollment toEntity(Lecture lecture) {
        return Enrollment.builder()
                .lecture(lecture)
                .build();
    }
}
