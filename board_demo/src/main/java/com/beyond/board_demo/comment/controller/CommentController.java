package com.beyond.board_demo.comment.controller;

import com.beyond.board_demo.comment.dto.CommentSaveReqDto;
import com.beyond.board_demo.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    공지사항은 추가하지 않음
    @PostMapping("create")
    public String createComment(@ModelAttribute CommentSaveReqDto dto, RedirectAttributes redirectAttributes) {
        commentService.saveComment(dto);
        if (dto.getPostId() != null) {
            redirectAttributes.addAttribute("id", dto.getPostId());
            return "redirect:/post/detail/{id}";
        } else if (dto.getQnaId() != null) {
            redirectAttributes.addAttribute("id", dto.getQnaId());
            return "redirect:/qna/detail/{id}";
        } else {
            throw new IllegalArgumentException("Either postId or qnaId must be provided for redirection");
        }
    }

}
