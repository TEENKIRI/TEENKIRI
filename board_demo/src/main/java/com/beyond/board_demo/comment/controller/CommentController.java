package com.beyond.board_demo.comment.controller;

import com.beyond.board_demo.comment.dto.CommentSaveReqDto;
import com.beyond.board_demo.comment.dto.CommentResDto;
import com.beyond.board_demo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create/post/{postId}")
    public String createCommentForPost(@ModelAttribute CommentSaveReqDto dto, @PathVariable Long postId, Model model) {
        try {
            commentService.createCommentForPost(dto, postId);
            return "redirect:/post/detail/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/create/qna/{qnaId}")
    public String createCommentForQnA(@ModelAttribute CommentSaveReqDto dto, @PathVariable Long qnaId, Model model) {
        try {
            commentService.createCommentForQnA(dto, qnaId);
            return "redirect:/qna/detail/" + qnaId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/create/notice/{noticeId}")
    public String createCommentForNotice(@ModelAttribute CommentSaveReqDto dto, @PathVariable Long noticeId, Model model) {
        try {
            commentService.createCommentForNotice(dto, noticeId);
            return "redirect:/notice/detail/" + noticeId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/list/post/{postId}")
    public String getCommentsForPost(@PathVariable Long postId, Model model) {
        List<CommentResDto> comments = commentService.getCommentsByPostId(postId);
        model.addAttribute("comments", comments);
        return "post/detail";
    }

    @GetMapping("/list/qna/{qnaId}")
    public String getCommentsForQna(@PathVariable Long qnaId, Model model) {
        List<CommentResDto> comments = commentService.getCommentsByQnaId(qnaId);
        model.addAttribute("comments", comments);
        return "qna/detail";
    }

    @GetMapping("/list/notice/{noticeId}")
    public String getCommentsForNotice(@PathVariable Long noticeId, Model model) {
        List<CommentResDto> comments = commentService.getCommentsByNoticeId(noticeId);
        model.addAttribute("comments", comments);
        return "notice/detail";
    }
}
