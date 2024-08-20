package com.beyond.teenkiri.comment.controller;

import com.beyond.teenkiri.comment.dto.CommentDetailDto;
import com.beyond.teenkiri.comment.dto.CommentSaveReqDto;
import com.beyond.teenkiri.comment.service.CommentService;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentSaveReqDto dto) {
        try {
            commentService.saveComment(dto);
            CommonResDto commonResDto;

            if (dto.getPostId() != null) {
                commonResDto = new CommonResDto(HttpStatus.CREATED, "댓글이 성공적으로 등록되었습니다.", dto.getPostId());
                return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
            } else if (dto.getQnaId() != null) {
                commonResDto = new CommonResDto(HttpStatus.CREATED, "댓글이 성공적으로 등록되었습니다.", dto.getQnaId());
                return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
            } else {
                throw new IllegalArgumentException("Either postId or qnaId must be provided.");
            }
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글 목록 조회 API 추가
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
        try {
            List<CommentDetailDto> comments = commentService.getCommentsByPostId(postId);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "댓글 목록 조회 성공", comments);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 목록 조회에 실패했습니다.");
            return new ResponseEntity<>(commonErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 댓글 삭제
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> CommentDelete(@PathVariable Long id){
        try {
            commentService.commentDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "댓글이 삭제되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND,e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }
}