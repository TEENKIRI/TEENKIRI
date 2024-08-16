package com.beyond.teenkiri.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseListResDto {
    private Long id;
    private String title;

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;
}
