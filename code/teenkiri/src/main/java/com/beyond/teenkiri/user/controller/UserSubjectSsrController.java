package com.beyond.teenkiri.user.controller;

import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.service.UserSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ssr")
public class UserSubjectSsrController {
    private final UserSubjectService userSubjectService;
    private final SubjectService subjectService;

    @Autowired
    public UserSubjectSsrController(UserSubjectService userSubjectService, SubjectService subjectService){
        this.userSubjectService = userSubjectService;
        this.subjectService = subjectService;
    }


//    강좌 수강하기
    @PostMapping("/my/subject/create")
    public String subjectCreate(@RequestBody UserSubjectSaveReqDto dto, Model model, HttpServletRequest request){
        String referer = request.getHeader("Referer"); // 헤더에서 이전 페이지를 읽는다.
        try {
            userSubjectService.userSubjectSign(dto);
            return "redirect:"+ referer; // 이전 페이지로 리다이렉트
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:"+ referer;
        }
    }




}
