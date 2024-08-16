package com.beyond.teenkiri.review.controller;

import com.beyond.teenkiri.review.domain.Review;
import com.beyond.teenkiri.review.dto.ReviewListResDto;
import com.beyond.teenkiri.review.dto.ReviewSaveReqDto;
import com.beyond.teenkiri.review.dto.ReviewUpdateReqDto;
import com.beyond.teenkiri.review.service.ReviewService;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody ReviewSaveReqDto dto) {
        try {
            Review review = reviewService.createReview(dto);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "리뷰가 작성되었습니다.", review.getId()), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.CONFLICT, e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getReviews(@RequestParam Long subject_id,
                                        @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<ReviewListResDto> reviews = reviewService.reviewListResDtos(subject_id, pageable);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "리뷰 목록입니다.", reviews), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/detail/{id}")
//    public ResponseEntity<?> getReviewDetail(@PathVariable Long id) {
//        try {
//            Review review = reviewService.getReviewDetail(id);
//            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "리뷰 상세 조회입니다.", review), HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewUpdateReqDto dto) {
        try {
            reviewService.updateReview(id, dto);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "리뷰가 성공적으로 수정되었습니다.", id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "리뷰가 삭제되었습니다.", id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
