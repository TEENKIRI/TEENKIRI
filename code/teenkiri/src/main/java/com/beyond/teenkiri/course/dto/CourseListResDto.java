package com.beyond.teenkiri.course.dto;

import com.beyond.teenkiri.course.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseListResDto {
    private Long id;
    private String title;
}
