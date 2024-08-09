package com.beyond.teenkiri.event.controller;

import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.event.dto.EventDetailDto;
import com.beyond.teenkiri.event.dto.EventListResDto;
import com.beyond.teenkiri.event.dto.EventSaveReqDto;
import com.beyond.teenkiri.event.dto.EventUpdateDto;
import com.beyond.teenkiri.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createEvent(EventSaveReqDto dto,
                                         @RequestPart(value="image", required = false) MultipartFile imageSsr) {
        try {
            Event event = eventService.createEvent(dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "이벤트가 성공적으로 등록되었습니다.", event.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (SecurityException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST,e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("list")
    public ResponseEntity<?> getAllEvents(@PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<EventListResDto> eventList = eventService.eventList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"이벤트 목록을 조회합니다.", eventList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<?> getEventDetail(@PathVariable Long id) {
        try {
            EventDetailDto eventDetail = eventService.getEventDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"이벤트 상세 정보를 조회합니다.", eventDetail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
         e.printStackTrace();
         CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
         return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> EventUpdate(@PathVariable Long id, EventUpdateDto dto,
                                         @RequestPart(value="image", required = false) MultipartFile imageSsr){
        try {
            eventService.eventUpdate(id, dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"이벤트가 성공적으로 수정되었습니다.",id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("delete/{id}")
    public ResponseEntity<?> EventDelete(@PathVariable Long id){
        try {
            eventService.eventDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "이벤트가 성공적으로 삭제되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
         e.printStackTrace();
         CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND,e.getMessage());
         return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

}
