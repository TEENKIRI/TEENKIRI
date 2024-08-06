package com.beyond.teenkiri.review.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.repository.LectureRepository;
import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.review.dto.ReviewSaveReqDto;
import com.beyond.teenkiri.review.dto.ReviewListResDto;
import com.beyond.teenkiri.review.dto.ReviewUpdateReqDto;
import com.beyond.teenkiri.review.repository.ReviewRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.repository.UserSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final UserSubjectRepository userSubjectRepository;
//    private final

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, LectureRepository lectureRepository, UserSubjectRepository userSubjectRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.lectureRepository = lectureRepository;
        this.userSubjectRepository = userSubjectRepository;
    }



    @Transactional
    public Review createReview(ReviewSaveReqDto dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Lecture lecture = lectureRepository.findById(dto.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));

        UserSubject userSubject = userSubjectRepository.findBySubjectIdAndUserId(lecture.getSubject().getId(), user.getId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수강입니다."));

        if (lecture.getProgress() == 100) {
            Review review = dto.toEntity(user, lecture);
            return reviewRepository.save(review);
        } else {
            throw new IllegalArgumentException("강의 진행률이 " + lecture.getProgress() + "% 입니다.");
        }
    }



    public Page<ReviewListResDto> getReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByDelYn(DelYN.N, pageable);
        return reviews.map(a -> a.listFromEntity());
    }

    public Review getReviewDetail(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
    }

    @Transactional
    public void updateReview(Long id, ReviewUpdateReqDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        review.toUpdate(dto);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        review.updateDelYN(DelYN.Y);
    }
}
