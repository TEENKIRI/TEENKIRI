package com.beyond.teenkiri.review.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.review.dto.ReviewSaveReqDto;
import com.beyond.teenkiri.review.dto.ReviewListResDto;
import com.beyond.teenkiri.review.dto.ReviewUpdateReqDto;
import com.beyond.teenkiri.review.repository.ReviewRepository;
import com.beyond.teenkiri.subject.domain.Subject;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserSubjectRepository userSubjectRepository;
    private final UserSubjectRepository subjectRepository;
//    private final

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, UserSubjectRepository userSubjectRepository, UserSubjectRepository subjectRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.userSubjectRepository = userSubjectRepository;
        this.subjectRepository = subjectRepository;
    }



    @Transactional
    public Review createReview(ReviewSaveReqDto dto) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 사용자가 수강한 과목을 조회 및 검증
        UserSubject userSubject = userSubjectRepository.findByUserEmail(userEmail)
                .stream()
                .filter(us -> us.getSubject().getId().equals(dto.getSubjectId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 수강과목입니다."));

        boolean existingReview = reviewRepository.existsByUserSubject(userSubject);
        if (existingReview) {
            throw new IllegalStateException("이미 이 과목에 대한 리뷰를 남겼습니다.");
        }


        Review review = dto.toEntity(user, userSubject);

        return reviewRepository.save(review);
    }

    public Page<ReviewListResDto> reviewListResDtos(Long subjectId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByUserSubject_Subject_Id(subjectId, pageable);
        return reviews.map(review -> review.listFromEntity());
    }

//    public Review getReviewDetail(Long id) {
//        return reviewRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
//    }

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
