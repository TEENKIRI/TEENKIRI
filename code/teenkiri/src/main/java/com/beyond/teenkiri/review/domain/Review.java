package com.beyond.teenkiri.review.domain;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.review.dto.ReviewListResDto;
import com.beyond.teenkiri.review.dto.ReviewUpdateReqDto;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "rating", nullable = false)
    private int rating;

    private String reviewText;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYn = DelYN.N;

    public ReviewListResDto listFromEntity() {
        return ReviewListResDto.builder()
                .userName(this.user.getName())
                .lectureTitle(this.lecture.getTitle())
                .lectureId(this.lecture.getId())
                .rating(this.rating)
                .reviewText(this.reviewText)
                .build();
    }

    public void toUpdate(ReviewUpdateReqDto dto) {
        this.rating = dto.getRating();
        this.reviewText = dto.getReviewText();
    }

    public void updateDelYN(DelYN delYN){
        this.delYn = delYN;
    }
}
