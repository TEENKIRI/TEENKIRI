package com.beyond.teenkiri.course.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.dto.CourseUpdateReqDto;
import com.beyond.teenkiri.course.service.CourseService;
import com.beyond.teenkiri.lecture.domain.Lecture;
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
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "과목 리스트",courseListResDtos);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    // 과목 생성
    @PostMapping("/course/create")
    public ResponseEntity<?> courseCreate(@RequestBody CourseSaveReqDto dto){
        Course course = courseService.courseCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"과목 생성이 완료되었습니다.",course.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


//    과목 업데이트
    @PatchMapping("/course/update/{id}")
    public ResponseEntity<?> courseUpdate(@PathVariable Long id, @RequestBody CourseUpdateReqDto dto){
        Course course = courseService.courseUpdate(id,dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "과목 업데이트 완료",course.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

//    과목 삭제
    @DeleteMapping("/course/delete/{id}")
    public ResponseEntity<?> courseDelete(@PathVariable Long id){
        Course course = courseService.courseDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "과목 삭제 완료", course.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

//    과목 실제 DB상 삭제
    @DeleteMapping("/course/delete/deep/{id}")
    public ResponseEntity<?> courseDeleteDeep(@PathVariable Long id){
        Long courseDeleteId = courseService.courseDeleteDeep(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "과목 DB상 실제 삭제 완료", courseDeleteId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

}
