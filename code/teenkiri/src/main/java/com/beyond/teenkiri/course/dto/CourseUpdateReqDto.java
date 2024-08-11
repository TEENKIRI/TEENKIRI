package com.beyond.teenkiri.course.dto;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseUpdateReqDto {
    private String title;
}
