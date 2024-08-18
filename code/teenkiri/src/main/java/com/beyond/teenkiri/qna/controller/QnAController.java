package com.beyond.teenkiri.qna.controller;

import com.beyond.teenkiri.comment.dto.CommentSaveReqDto;
import com.beyond.teenkiri.comment.service.CommentService;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.lecture.dto.LectureListResDto;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.*;
import com.beyond.teenkiri.qna.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("qna")
public class QnAController {

    private final QnAService qnAService;
    private final CommentService commentService;

    @Autowired
    public QnAController(QnAService qnAService, CommentService commentService) {
        this.qnAService = qnAService;
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuestion(QnASaveReqDto dto,
                                            @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            QnA qna = qnAService.createQuestion(dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "질문이 성공적으로 등록되었습니다.", qna.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllQuestions(
            @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String searchCategory,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) Long subjectId) {

        Page<QnAListResDto> qnaList;

        if (subjectId != null) {
            qnaList = qnAService.qnaListByGroup(subjectId, pageable);
        } else {
            qnaList = qnAService.qnaListWithSearch(pageable, searchCategory, searchQuery);
        }

        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "질문 목록을 조회합니다.", qnaList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getQuestionDetail(@PathVariable Long id) {
        try {
            // QnADetailDto를 반환하는 서비스 메서드 호출
            QnADetailDto questionDetail = qnAService.getQuestionDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "질문 상세 정보를 조회합니다.", questionDetail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/comment/create")
    public ResponseEntity<?> createQnaComment(@RequestBody CommentSaveReqDto dto) {
        commentService.saveComment(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "댓글이 성공적으로 등록되었습니다.", dto.getQnaId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/answer/{id}")
    public ResponseEntity<?> answerQuestion(@PathVariable Long id, QnAAnswerReqDto dto,
                                            @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            qnAService.answerQuestion(id, dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "질문에 대한 답변이 성공적으로 등록되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (SecurityException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/question/{id}")
    public ResponseEntity<?> qnaQUpdate(@PathVariable Long id, QnAQtoUpdateDto dto,
                                        @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            qnAService.QnAQUpdate(id, dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "질문이 성공적으로 업데이트되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/answer/{id}")
    public ResponseEntity<?> qnaAUpdate(@PathVariable Long id, QnAAtoUpdateDto dto,
                                        @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            qnAService.QnAAUpdate(id, dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "답변이 성공적으로 업데이트되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> qnaDelete(@PathVariable Long id) {
        try {
            qnAService.qnaDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "질문이 성공적으로 삭제되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/subject/{subjectId}/qna/list")
    public ResponseEntity<?> lectureGroupBySubjectListView(@PathVariable("subjectId") Long subjectId,
                                                           @PageableDefault(page = 0, size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<QnAListResDto> qnAListResDtos = qnAService.qnaListByGroup(subjectId, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "해당 강좌의 QnA 목록을 조회합니다.", qnAListResDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyQuestions() {
        List<QnAListResDto> qnaList = qnAService.getUserQnAs();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "사용자 작성 질문 목록을 조회합니다.", qnaList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}

