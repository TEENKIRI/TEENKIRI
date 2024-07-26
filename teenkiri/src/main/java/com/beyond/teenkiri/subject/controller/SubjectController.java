package com.beyond.teenkiri.subject.controller;

import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService){
        this.subjectService = subjectService;
    }


//    강좌 리스트 페이지
    @GetMapping("/subject/list")
    public void subjectListView(){

    }


//    강좌 상세 페이지
    @GetMapping("/subject/detail/{id}")
    public void subjectDetailView(){

    }


//    강좌 생성
    @PostMapping("/subject/create")
    public ResponseEntity<?> subjectCreate(@ModelAttribute SubjectSaveReqDto dto){
        Long subjectId = subjectService.subjectCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 생성이 완료되었습니다.",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


//    강좌 업데이트
    @PatchMapping("/subject/update/{id}")
    public void subjectUpdate(){

    }


//    강좌 삭제
    @DeleteMapping("/subject/delete/{id}")
    public void subjectDelete(){

    }



}
