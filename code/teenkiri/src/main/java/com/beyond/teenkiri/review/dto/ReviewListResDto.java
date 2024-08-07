package com.beyond.teenkiri.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListResDto {
    private Long id;
    private String userName;
    private String lectureTitle;
    private Long lectureId;
    private int rating;
    private String reviewText;
}
