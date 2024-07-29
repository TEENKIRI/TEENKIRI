package com.beyond.teenkiri.course.controller;

import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.service.CourseService;
import com.beyond.teenkiri.subject.dto.SubjectSaveReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    //    과목 리스트
    @GetMapping("/course/list")
    public ResponseEntity<?> courseListView(@PageableDefault(page = 0, size=10, sort = "id",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<CourseListResDto> courseListResDtos = courseService.courseList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject list",courseListResDtos);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


    // 과목 생성
    @PostMapping("/course/create")
    public ResponseEntity<?> courseCreate(@RequestBody CourseSaveReqDto dto){
        Course course = courseService.courseCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 생성이 완료되었습니다.",course.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


//    해당 과목에 연결된 강좌 보기


}
