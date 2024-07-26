package com.beyond.teenkiri.lecture.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class LectureController {

    //    강의 리스트 페이지
    @GetMapping("/lecture/list/")
    public void lectureListView(){

    }

    //    강의 ((((강좌 그룹별)))) 리스트 페이지
    @GetMapping("/subject/lecture/list/{subject_id}")
    public void lectureGroupBySubjectListView(){

    }


    //    강의 상세 페이지
    @GetMapping("/lecture/detail/{id}")
    public void lectureDetailView(){

    }


    //    강의 생성
    @PostMapping("/lecture/create")
    public void lectureCreate(){

    }


    //    강의 업데이트
    @PatchMapping("/lecture/update/{id}")
    public void lectureUpdate(){

    }


    //    강의 삭제
    @DeleteMapping("/lecture/delete/{id}")
    public void lectureDelete(){

    }


}
