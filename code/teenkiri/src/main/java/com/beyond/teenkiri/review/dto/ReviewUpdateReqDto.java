package com.beyond.teenkiri.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewUpdateReqDto {
    private int rating;
    private String reviewText;
}
