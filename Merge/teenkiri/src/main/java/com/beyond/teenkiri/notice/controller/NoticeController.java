package com.beyond.teenkiri.notice.controller;

import com.beyond.teenkiri.notice.dto.NoticeDetailDto;
import com.beyond.teenkiri.notice.dto.NoticeSaveReqDto;
import com.beyond.teenkiri.notice.dto.NoticeUpdateDto;
import com.beyond.teenkiri.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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
        return "board/notice/create";
    }

    @PostMapping("create")
    public String createNotice(@ModelAttribute NoticeSaveReqDto dto, Model model) {
        try {
            noticeService.createNotice(dto);
            return "redirect:/notice/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "board/notice/create";
        }
    }

    @GetMapping("list")
    public String getAllNotices(Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("noticeList", noticeService.noticeList(pageable));
        return "board/notice/list";
    }

    @GetMapping("detail/{id}")
    public String getNoticeDetail(@PathVariable Long id, Model model) {
        NoticeDetailDto noticeDetail = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", noticeDetail);
        return "board/notice/detail";
    }

    @PostMapping("update/{id}")
    public String noticeUpdate(@PathVariable Long id, @ModelAttribute NoticeUpdateDto dto){
        noticeService.noticeUpdate(id, dto);
        return "redirect:/notice/detail/" + id;
    }

    @GetMapping("delete/{id}")
    public String noticeDelete(@PathVariable Long id, Model model){
        noticeService.noticeDelete(id);
        return "redirect:/notice/list";
    }
}
