package com.beyond.teenkiri.review.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSaveReqDto {
    private String userEmail;
    private Long lectureId;
    private int rating;
    private String reviewText;
    @Builder.Default
    private DelYN delYN = DelYN.N;

    public Review toEntity(User user, Lecture lecture) {
        return Review.builder()
                .user(user)
                .lecture(lecture)
                .rating(this.rating)
                .reviewText(this.reviewText)
                .delYn(this.delYN)
                .build();
    }
}