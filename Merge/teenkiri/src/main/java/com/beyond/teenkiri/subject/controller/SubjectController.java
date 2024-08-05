package com.beyond.teenkiri.subject.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.subject.domain.Subject;
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


//    강좌 리스트 페이지 :: delyn n인 것만
    @GetMapping("/subject/list")
    public ResponseEntity<?> subjectListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

//    강좌 순위별 리스트 페이지 :: delyn n인 것만
    @GetMapping("/subject/rating/list")
    public ResponseEntity<?> subjectRatingListView(@PageableDefault(page = 0, size=4 ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectRatingList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


//    강좌 상세 페이지
    @GetMapping("/subject/detail/{id}")
    public ResponseEntity<?> subjectDetailView(@PathVariable(value = "id") Long id){
        SubjectDetResDto subjectDetResDto = subjectService.subjectDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject detail",subjectDetResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


//    강좌 생성
    @PostMapping("/subject/create")
    public ResponseEntity<?> subjectCreate(@RequestBody SubjectSaveReqDto dto){
        Subject subject = subjectService.subjectCreate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 생성이 완료되었습니다.",subject.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


//    강좌 업데이트
    @PatchMapping("/subject/update")
    public ResponseEntity<?> subjectUpdate(SubjectUpdateReqDto dto){
        Long subjectId = subjectService.subjectUpdate(dto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject update",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }


//    강좌 삭제
    @DeleteMapping("/subject/delete/{id}")
    public ResponseEntity<?> subjectDelete(@PathVariable(value = "id") Long id){
        Long subjectId = subjectService.subjectDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject delete",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }



}
