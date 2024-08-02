package com.beyond.teenkiri.course.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseDetResDto;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/ssr")
public class CourseSsrController {
    private final CourseService courseService;

    @Autowired
    public CourseSsrController(CourseService courseService) {
        this.courseService = courseService;
    }


    //    과목 리스트
    @GetMapping("/course/list")
    public String courseListView(@PageableDefault(page = 0, size=10, sort = "id",
            direction = Sort.Direction.DESC ) Pageable pageable, Model model){
        Page<CourseListResDto> courseListResDtos = courseService.courseList(pageable);
        model.addAttribute("courseList", courseListResDtos);
        return "course/list";
    }

    // 과목 상세페이지 화면
    @GetMapping("/course/detail/{id}")
    public String courseDetail(@PathVariable Long id, Model model){
        CourseDetResDto courseDetResDto = courseService.courseDetail(id);
        model.addAttribute("course", courseDetResDto);
        return "course/detail";
    }

    // 과목 생성 화면
    @GetMapping("/course/create")
    public String courseCreateView(){
        return "course/create";
    }

    // 과목 생성
    @PostMapping("/course/create")
    public String courseCreate(CourseSaveReqDto dto, Model model){
        
        try {
            Course course = courseService.courseCreate(dto);
            return "redirect:/ssr/course/list";
        } catch (SecurityException | EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "course/create";
        }
    }


//    해당 과목에 연결된 강좌 보기


}
