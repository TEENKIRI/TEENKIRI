package com.beyond.teenkiri.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureListResDto {
    private Long id;
    private String title;
    private String imageUrl;
    private Float progress;
}
