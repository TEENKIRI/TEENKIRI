package com.beyond.board_demo.notice.controller;

import com.beyond.board_demo.notice.dto.NoticeDetailDto;
import com.beyond.board_demo.notice.dto.NoticeListResDto;
import com.beyond.board_demo.notice.dto.NoticeSaveReqDto;
import com.beyond.board_demo.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("create")
    public String createNoticeForm() {
        return "notice/create";
    }

    @PostMapping("create")
    public String createNotice(@ModelAttribute NoticeSaveReqDto dto, Model model) {
        try {
            noticeService.createNotice(dto, dto.getUserEmail());
            return "redirect:/notice/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "notice/create";
        }
    }

    @GetMapping("list")
    public String getAllNotices(Model model) {
        List<NoticeListResDto> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);
        return "notice/list";
    }

    @GetMapping("detail/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        NoticeDetailDto noticeDetail = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", noticeDetail);
        return "notice/detail";
    }
}
