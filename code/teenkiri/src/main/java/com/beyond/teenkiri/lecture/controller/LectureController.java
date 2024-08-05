package com.beyond.teenkiri.lecture.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.dto.LectureDetResDto;
import com.beyond.teenkiri.lecture.dto.LectureListResDto;
import com.beyond.teenkiri.lecture.dto.LectureSaveReqDto;
import com.beyond.teenkiri.lecture.dto.LectureUpdateReqDto;
import com.beyond.teenkiri.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    //    강의 리스트 페이지
    @GetMapping("/lecture/list")
    public ResponseEntity<?> lectureListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<LectureListResDto> lectureListResDtos = lectureService.lectureList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return lecture list",lectureListResDtos);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    //    강의 ((((강좌 그룹별)))) 리스트 페이지
    @GetMapping("/subject/lecture/list/{subjectId}")
    public ResponseEntity<?> lectureGroupBySubjectListView(@PathVariable("subjectId") Long subjectId, @PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<LectureListResDto> lectureListResDtos = lectureService.lectureListByGroup(subjectId, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return lecture list by subject group", lectureListResDtos);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


    //    강의 상세 페이지
    @GetMapping("/lecture/detail/{id}")
    public ResponseEntity<?> lectureDetailView(@PathVariable Long id){
        LectureDetResDto lectureDetResDto = lectureService.lectureDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return lecture detail", lectureDetResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


    //    강의 생성
    @PostMapping("/lecture/create")
    public ResponseEntity<?> lectureCreate(@ModelAttribute LectureSaveReqDto dto){
        Lecture lecture = lectureService.lectureCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "강의 생성 완료", lecture.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


    //    강의 업데이트
    @PatchMapping("/lecture/update/{id}")
    public ResponseEntity<?> lectureUpdate(LectureUpdateReqDto dto){
        Lecture lecture = lectureService.lectureUpdate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "강의 업데이트 완료", lecture.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


    //    강의 삭제
    @DeleteMapping("/lecture/delete/{id}")
    public ResponseEntity<?> lectureDelete(@PathVariable Long id){
        Lecture lecture = lectureService.lectureDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "강의 삭제 완료", lecture.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    //    강의 DB상 완전 삭제
    @DeleteMapping("/lecture/delete/deep/{id}")
    public ResponseEntity<?> lectureDeleteDeep(@PathVariable Long id){
        Long lectureDeleteId = lectureService.lectureDeleteDeep(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "강의 삭제 완료", lectureDeleteId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


}
