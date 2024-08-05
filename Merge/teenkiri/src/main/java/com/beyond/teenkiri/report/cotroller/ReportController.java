package com.beyond.teenkiri.report.cotroller;

import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PostRepository postRepository;

    @Autowired
    public ReportController(ReportService reportService, QnARepository qnaRepository, PostRepository postRepository) {
        this.reportService = reportService;
        this.qnaRepository = qnaRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("create")
    public String reportCreateScreen(@RequestParam(value = "qnaId", required = false) Long qnaId,
                                     @RequestParam(value = "postId", required = false) Long postId, Model model) {
        if (qnaId != null) {
            QnA qna = qnaRepository.findById(qnaId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
            model.addAttribute("suspectEmail", qna.getUser().getEmail());
            model.addAttribute("qnaId", qnaId);
        } else if (postId != null) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Post입니다."));
            model.addAttribute("suspectEmail", post.getUser().getEmail());
            model.addAttribute("postId", postId);
        } else {
            throw new IllegalArgumentException("QnA ID 또는 Post ID가 필요합니다.");
        }
        return "/board/report/create";
    }

    @PostMapping("create")
    public String reportCreatePost(@ModelAttribute ReportSaveReqDto dto, Model model) {
        try {
            reportService.reportCreate(dto);
            return "redirect:/board/report/list";
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/board/report/list";
        }
    }

    @GetMapping("list")
    public String reportList(@RequestParam(value = "type", required = false) String type, Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("reportList", reportService.reportList(pageable, type));
        model.addAttribute("filterType", type != null ? type : "all");
        return "board/report/list";
    }
}
