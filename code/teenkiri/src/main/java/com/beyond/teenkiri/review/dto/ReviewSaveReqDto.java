package com.beyond.teenkiri.review.dto;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewSaveReqDto {
    private String userEmail;
    private Long subjectId;
    private String subjectTitle;
    private String reviewText;

    private int rating;

    @Builder.Default
    private DelYN delYN = DelYN.N;


    public Review toEntity(User user, UserSubject userSubject){
        return  Review.builder()
                .user(user)
                .userSubject(userSubject)
                .reviewText(this.reviewText)
                .rating(this.rating)
                .delYn(DelYN.N)
                .build();
    }

}