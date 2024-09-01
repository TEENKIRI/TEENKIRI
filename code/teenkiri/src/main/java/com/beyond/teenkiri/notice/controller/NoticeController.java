package com.beyond.teenkiri.notice.controller;

import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.notice.domain.Notice;
import com.beyond.teenkiri.notice.dto.NoticeDetailDto;
import com.beyond.teenkiri.notice.dto.NoticeListResDto;
import com.beyond.teenkiri.notice.dto.NoticeSaveReqDto;
import com.beyond.teenkiri.notice.dto.NoticeUpdateDto;
import com.beyond.teenkiri.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("board/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createNotice(NoticeSaveReqDto dto,
                                          @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            Notice notice = noticeService.createNotice(dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "공지사항이 성공적으로 등록되었습니다.", notice.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (SecurityException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> postListView(
            @PageableDefault(page = 0, size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "searchType", required = false) String searchType) {

        Page<NoticeListResDto> postListResDto = noticeService.noticeListWithSearch(pageable, searchType, searchQuery);
        com.beyond.teenkiri.common.CommonResDto commonResDto = new com.beyond.teenkiri.common.CommonResDto(HttpStatus.OK, "Return post list", postListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable Long id) {
        try {
            NoticeDetailDto noticeDetail = noticeService.getNoticeDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "공지사항 상세 정보를 조회합니다.", noticeDetail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public ResponseEntity<?> noticeUpdate(@PathVariable Long id, NoticeUpdateDto dto,
                                          @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            noticeService.noticeUpdate(id, dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "공지사항이 성공적으로 업데이트되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> noticeDelete(@PathVariable Long id) {
        try {
            noticeService.noticeDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "공지사항이 성공적으로 삭제되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }
}
