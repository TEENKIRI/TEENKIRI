package com.beyond.teenkiri.lecture.controller;

import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.lecture.dto.*;
import com.beyond.teenkiri.lecture.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/ssr")
public class LectureSsrController {
    private final LectureService lectureService;

    @Autowired
    public LectureSsrController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    //    강의 리스트 페이지
    @GetMapping("/lecture/list/all")
    public String lectureListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<LectureListResDto> lectureListResDtos = lectureService.lectureList(pageable);
        return "lecture/list/all";
    }

    //    강의 ((((강좌 그룹별)))) 리스트 페이지
    @GetMapping("/lecture/list/{subjectId}")
    public String lectureGroupBySubjectListView(@PathVariable("subjectId") Long subjectId, @PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<LectureListResDto> lectureListResDtos = lectureService.lectureListByGroup(subjectId, pageable);
        model.addAttribute("lectureList",lectureListResDtos);
        model.addAttribute("subjectId",subjectId);
        System.out.println(model);
        return "lecture/list";
    }


    //    강의 상세 페이지
    @GetMapping("/lecture/detail/{id}")
    public String lectureDetailView(@PathVariable Long id,Model model){
        LectureDetResDto lectureDetResDto = lectureService.lectureDetail(id);
        model.addAttribute("lecture",lectureDetResDto);
        return "lecture/detail";
    }

    //    유저별 강의 수강 상세 페이지
    @GetMapping("/user/lecture/detail/{id}/{userEmail}")
    public String lectureStudy(@PathVariable Long id, @PathVariable String userEmail, Model model){
        LectureDetPerUserResDto lectureDetPerUserResDto = lectureService.lectureDetailPerUser(id);
        model.addAttribute("lecture",lectureDetPerUserResDto);
        model.addAttribute("userEmail",userEmail);
        return "lecture/user_detail";
    }

//    강의 생성 페이지
    @GetMapping("/lecture/create/{subjectId}")
    public String lectureCreateView(@PathVariable("subjectId") Long subjectId,Model model){
        model.addAttribute("subjectId",subjectId);
        return "lecture/create";
    }

    //    강의 생성
    @PostMapping("/lecture/create")
    public String lectureCreate(LectureSaveReqDto dto,
                                @RequestPart(value="video") MultipartFile videoSsr,
                                @RequestPart(value="image") MultipartFile imageSsr, Model model){
        try {
            lectureService.lectureCreate(dto, videoSsr, imageSsr);
            return "redirect:/ssr/lecture/list/"+ dto.getSubjectId();
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "lecture/create";
        }
    }


    //    강의 업데이트
//    @PatchMapping("/lecture/update/{id}")
//    public String lectureUpdate(LectureUpdateReqDto dto){
//        Lecture lecture = lectureService.lectureUpdate(dto);
//        return "lecture/detail/" + lecture.getId();
//    }


    //    강의 삭제
    @DeleteMapping("/lecture/delete/{id}")
    public String lectureDelete(@PathVariable Long id){
        Lecture lecture = lectureService.lectureDelete(id);
        return "redirect:/ssr/lecture/list";
    }

    //    강의 DB상 완전 삭제
    @DeleteMapping("/lecture/delete/deep/{id}")
    public String lectureDeleteDeep(@PathVariable Long id){
        Long lectureDeleteId = lectureService.lectureDeleteDeep(id);
        return "redirect:/ssr/lecture/list";
    }


//    ----------




}
