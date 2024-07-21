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

    @PostMapping("create")
    public String createComment(@ModelAttribute CommentSaveReqDto dto, RedirectAttributes redirectAttributes) {
        commentService.saveComment(dto);
        redirectAttributes.addAttribute("id", dto.getPostId());
        return "redirect:/post/detail/{id}";
    }
}
