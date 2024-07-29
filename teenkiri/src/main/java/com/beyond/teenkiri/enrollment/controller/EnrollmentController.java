package com.beyond.teenkiri.enrollment.controller;

import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.enrollment.domain.Enrollment;
import com.beyond.teenkiri.enrollment.dto.EnrollDetResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollListResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollSaveReqDto;
import com.beyond.teenkiri.enrollment.dto.EnrollUpdateReqDto;
import com.beyond.teenkiri.enrollment.service.EnrollmentService;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

//    진행률 리스트
    @GetMapping("/enroll/list")
    public ResponseEntity<?> subjectListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<EnrollListResDto> enrollListResDto = enrollmentService.enrollList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return enrollment list",enrollListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

//    진행률 (((강좌별))) 리스트
    @GetMapping("/enroll/{subjectId}/list")
    public ResponseEntity<?> subjectListViewByGroup(@PathVariable Long subjectId, @PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<EnrollListResDto> enrollListResDto = enrollmentService.enrollListByGroup(subjectId, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return enrollment list by subject",enrollListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @GetMapping("/enroll/detail/{id}")
    public ResponseEntity<?> subjectListViewByGroup(@PathVariable Long id){
        EnrollDetResDto enrollDetResDto = enrollmentService.enrollDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return enrollment detail",enrollDetResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

//    진행률 생성
    @PostMapping("/enroll/create")
    public ResponseEntity<?> subjectCreate(@RequestBody EnrollSaveReqDto dto){
        Enrollment enrollment = enrollmentService.enrollCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "진행률 생성 완료",enrollment.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }

    @PatchMapping("/enroll/update")
    public ResponseEntity<?> subjectUpdate(EnrollUpdateReqDto dto){
        Enrollment enrollment = enrollmentService.enrollUpdate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "진행률 업데이트 완료",enrollment.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

}
