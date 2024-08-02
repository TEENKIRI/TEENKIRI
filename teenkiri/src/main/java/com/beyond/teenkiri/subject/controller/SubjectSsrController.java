package com.beyond.teenkiri.subject.controller;

import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.service.CourseService;
import com.beyond.teenkiri.subject.dto.SubjectDetResDto;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import com.beyond.teenkiri.subject.dto.SubjectUpdateReqDto;
import com.beyond.teenkiri.subject.service.SubjectService;
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
public class SubjectSsrController {
    private final SubjectService subjectService;
    private final CourseService courseService;

    @Autowired
    public SubjectSsrController(SubjectService subjectService, CourseService courseService){
        this.subjectService = subjectService;
        this.courseService = courseService;
    }


//    강좌 리스트 페이지 :: delyn n인 것만
    @GetMapping("/subject/list")
    public String subjectListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectList(pageable);
        model.addAttribute("subjectList", subjectListResDto);
        return "subject/list";
    }

//    강좌 순위별 리스트 페이지 :: delyn n인 것만
    @GetMapping("/subject/rating/list")
    public String subjectRatingListView(@PageableDefault(page = 0, size=4 ) Pageable pageable, Model model){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectRatingList(pageable);
        model.addAttribute("subjectList", subjectListResDto);
        return "subject/rating/list";
    }


//    강좌 상세 페이지
    @GetMapping("/subject/detail/{id}")
    public String subjectDetailView(@PathVariable(value = "id") Long id, Model model){
        SubjectDetResDto subjectDetResDto = subjectService.subjectDetail(id);
        model.addAttribute("subject", subjectDetResDto);
        return "subject/detail";
    }

//    강좌 생성 페이지
    @GetMapping("/subject/create")
    public String subjectCreateView(Model model, Pageable pageable){
        Page<CourseListResDto> courses = courseService.courseList(pageable);
        model.addAttribute("courseList",courses);
        return "subject/create";
    }

//    강좌 생성
    @PostMapping("/subject/create")
    public String subjectCreate(SubjectSaveReqDto dto,
                                Model model){
        System.out.println("22222222222222222222222222222222");
       try {
            subjectService.subjectCreate(dto,null);
            return "redirect:/ssr/subject/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "subject/create";
        }
    }


//    강좌 업데이트
    @PatchMapping("/subject/update")
    public String subjectUpdate(SubjectUpdateReqDto dto, Model model){
        Long subjectId = subjectService.subjectUpdate(dto);
        return "redirect:/subject/detail/" + subjectId;
    }


//    강좌 삭제
    @DeleteMapping("/subject/delete/{id}")
    public String subjectDelete(@PathVariable(value = "id") Long id, Model model){
        Long subjectId = subjectService.subjectDelete(id);
        return "redirect:/ssr/subject/list";
    }



}
