package com.beyond.teenkiri.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDetResDto {
    private Long id;
    private Long subjectId;
    private String title;
    private String imageUrl;
    private String videoUrl;

    private LocalDateTime updatedTime;
    private LocalDateTime createdTime;
}
