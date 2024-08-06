package com.beyond.teenkiri.report.cotroller;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.service.ReportService;
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
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final QnARepository qnaRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReportController(ReportService reportService, QnARepository qnaRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.reportService = reportService;
        this.qnaRepository = qnaRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> reportCreatePost(@RequestBody ReportSaveReqDto dto) {
        try {
            reportService.reportCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "신고가 성공적으로 등록되었습니다.", dto.getReportEmail());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> reportList(@RequestParam(value = "type", required = false) String type,
                                        @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<?> reportList = reportService.reportList(pageable, type);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "신고 목록을 조회합니다.", reportList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> reportDetails(@RequestParam(value = "qnaId", required = false) Long qnaId,
                                           @RequestParam(value = "postId", required = false) Long postId,
                                           @RequestParam(value = "commentId", required = false) Long commentId) {
        try {
            String suspectEmail = null;
            if (commentId != null) {
                Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Comment입니다."));
                suspectEmail = comment.getUser().getEmail();
            } else if (postId != null) {
                Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Post입니다."));
                suspectEmail = post.getUser().getEmail();
            } else if (qnaId != null) {
                QnA qna = qnaRepository.findById(qnaId)
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
                suspectEmail = qna.getUser().getEmail();
            } else {
                throw new IllegalArgumentException("QnA ID, Post ID 또는 Comment ID가 필요합니다.");
            }

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "신고 세부 정보를 조회합니다.", suspectEmail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
