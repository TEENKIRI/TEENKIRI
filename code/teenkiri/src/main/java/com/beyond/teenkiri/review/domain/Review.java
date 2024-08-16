package com.beyond.teenkiri.review.domain;

import com.beyond.teenkiri.common.domain.BaseTimeEntity;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.review.dto.ReviewListResDto;
import com.beyond.teenkiri.review.dto.ReviewUpdateReqDto;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
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
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // userSubject.getSubject.getId를 사용하였음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private UserSubject userSubject;


    private int rating;

    private String reviewText;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYn = DelYN.N;

    public ReviewListResDto listFromEntity() {
        return ReviewListResDto.builder()
                .id(this.id)
                .nickname(this.user.getNickname())
                .subjectTitle(this.userSubject.getSubject().getTitle())
                .subjectId(this.userSubject.getSubject().getId())
                .rating(this.rating)
                .reviewText(this.reviewText)
                .createdTime(this.getCreatedTime())
                .updatedTime(this.getUpdatedTime())
                .build();
    }

    public void toUpdate(ReviewUpdateReqDto dto) {
        this.reviewText = dto.getReviewText();
    }

    public void updateDelYN(DelYN delYN){
        this.delYn = delYN;
    }
}
