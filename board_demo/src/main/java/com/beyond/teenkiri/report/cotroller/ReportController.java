package com.beyond.teenkiri.report.controller;

import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("report")
public class ReportController {

    private final ReportService reportService;
    private final QnARepository qnaRepository;

    @Autowired
    public ReportController(ReportService reportService, QnARepository qnaRepository) {
        this.reportService = reportService;
        this.qnaRepository = qnaRepository;
    }

    @GetMapping("create")
    public String reportCreateScreen(@RequestParam("qnaId") Long qnaId, Model model) {
        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
        model.addAttribute("suspectEmail", qna.getUser().getEmail());
        model.addAttribute("qnaId", qnaId);
        return "/report/create";
    }

    @PostMapping("create")
    public String reportCreatePost(@ModelAttribute ReportSaveReqDto dto, Model model){
        try {
            reportService.reportCreate(dto);
            return "redirect:/report/list";
        }catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/report/list";
        }
    }

    @GetMapping("list")
    public String reportList(Model model, @PageableDefault(size = 10, sort = "createdTime") Pageable pageable){
        model.addAttribute("reportList", reportService.reportList(pageable));
        return "report/list";
    }
}
