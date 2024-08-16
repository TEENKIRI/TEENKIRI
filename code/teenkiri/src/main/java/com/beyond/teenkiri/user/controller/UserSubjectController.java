package com.beyond.teenkiri.user.controller;

import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.user.domain.UserSubject;
import com.beyond.teenkiri.user.dto.UserSubjectListResDto;
import com.beyond.teenkiri.user.dto.UserSubjectSaveReqDto;
import com.beyond.teenkiri.user.service.UserSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSubjectController {
    private final UserSubjectService userSubjectService;

    @Autowired
    public UserSubjectController(UserSubjectService userSubjectService){
        this.userSubjectService = userSubjectService;
    }


//    강좌 수강하기
    @PostMapping("/my/subject/create")
    public ResponseEntity<?> subjectCreate(@RequestBody UserSubjectSaveReqDto dto){
        UserSubject userSubject = userSubjectService.userSubjectSign(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 수강 신청이 완료되었습니다.",userSubject.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }

    @GetMapping("/my/subject/{id}")
    public ResponseEntity<?> getUserSubjects() {
        try {
            UserSubjectListResDto userSubjectListResDto = userSubjectService.getUserSubjects();
            return new ResponseEntity<>(userSubjectListResDto, HttpStatus.OK);
        } catch (Exception e) {
            CommonErrorDto errorDto = new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
