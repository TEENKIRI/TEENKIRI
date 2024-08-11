package com.beyond.teenkiri.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResDto {
    private Long id;
    private String userName;
    private String subjectTitle;
    private Long subjectId;
    private int rating;
    private String reviewText;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
