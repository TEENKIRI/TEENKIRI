package com.beyond.teenkiri.main.controller;

import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final SubjectService subjectService;

    @Autowired
    public MainController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<SubjectDetResDto> recommendedSubjects = subjectService.getRandomSubjects(3);
        model.addAttribute("recommendedSubjects", recommendedSubjects);
        return "main/main";
    }
}
