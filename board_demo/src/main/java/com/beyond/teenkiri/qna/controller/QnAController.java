package com.beyond.teenkiri.qna.controller;

import com.beyond.teenkiri.comment.dto.CommentSaveReqDto;
import com.beyond.teenkiri.comment.service.CommentService;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.*;
import com.beyond.teenkiri.qna.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("qna")
public class QnAController {

    private final QnAService qnAService;
    private final CommentService commentService;

    @Autowired
    public QnAController(QnAService qnAService, CommentService commentService) {
        this.qnAService = qnAService;
        this.commentService = commentService;
    }

    @GetMapping("create")
    public String createQuestionForm() {
        return "qna/create";
    }

    @PostMapping("create")
    public String createQuestion(@ModelAttribute QnASaveReqDto dto, Model model) {
        try {
            qnAService.createQuestion(dto);
            return "redirect:/qna/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "qna/create";
        }
    }

    @GetMapping("list")
    public String getAllQuestions(Model model, @PageableDefault(size = 10, sort = "createdTime") Pageable pageable) {
        model.addAttribute("qnaList", qnAService.qnaList(pageable));
        return "qna/list";
    }

    @GetMapping("detail/{id}")
    public String getQuestionDetail(@PathVariable Long id, Model model) {
        QnADetailDto question = QnADetailDto.fromEntity(qnAService.getQuestionDetail(id));
        model.addAttribute("question", question);
        model.addAttribute("comments", commentService.getCommentsByQnaId(id));
        return "qna/detail";
    }

    @PostMapping("comment/create")
    public String createQnaComment(@ModelAttribute CommentSaveReqDto dto, RedirectAttributes redirectAttributes) {
        commentService.saveComment(dto);
        redirectAttributes.addAttribute("id", dto.getQnaId());
        return "redirect:/qna/detail/{id}";
    }

    @GetMapping("answer/{id}")
    public String answerQuestionForm(@PathVariable Long id, Model model) {
        QnADetailDto questionDetail = QnADetailDto.fromEntity(qnAService.getQuestionDetail(id));
        model.addAttribute("question", questionDetail);
        return "qna/answer";
    }

    @PostMapping("answer/{id}")
    public String answerQuestion(@PathVariable Long id, @ModelAttribute QnAAnswerReqDto dto, Model model) {
        try {
            qnAService.answerQuestion(id, dto);
            model.addAttribute("question", dto);
            return "redirect:/qna/detail/" + id;
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "qna/answer";
        }
    }

    @PostMapping("update/question/{id}")
    public String qnaQUpdate(@PathVariable Long id, @ModelAttribute QnAQtoUpdateDto dto, Model model) {
        try {
            qnAService.QnAQUpdate(id, dto);
            return "redirect:/qna/detail/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/qna/detail/" + id;
        }
    }

    @PostMapping("update/answer/{id}")
    public String qnaAUpdate(@PathVariable Long id, @ModelAttribute QnAAtoUpdateDto dto, Model model) {
        try {
            qnAService.QnAAUpdate(id, dto);
            return "redirect:/qna/detail/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/qna/detail/" + id;
        }
    }

    @GetMapping("delete/{id}")
    public String qnaDelete(@PathVariable Long id, Model model) {
        try {
            qnAService.qnaDelete(id);
            return "redirect:/qna/list";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/qna/list";
        }
    }

}