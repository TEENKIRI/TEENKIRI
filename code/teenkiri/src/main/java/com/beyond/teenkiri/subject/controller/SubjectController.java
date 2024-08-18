package com.beyond.teenkiri.subject.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.subject.domain.Grade;
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
import org.springframework.web.multipart.MultipartFile;


@RestController
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService){
        this.subjectService = subjectService;
    }


    // 검색 기능 추가
    @GetMapping("/subject/list")
    public ResponseEntity<?> subjectListView(
            @PageableDefault(page = 0, size=10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType) {  // sortType 추가

        Page<SubjectListResDto> subjectListResDto = subjectService.subjectList(pageable, search, searchType, sortType);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject list", subjectListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    //    강좌 과목별 리스트 페이지
    @GetMapping("/subject/{courseId}/list")
    public ResponseEntity<?> subjectCourseListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable,
                                                   @PathVariable(value = "courseId") Long courseId){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectPerCourseList(pageable, courseId);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    //    강좌 과목별, 학년별 리스트 페이지
    @GetMapping("/subject/{courseId}/{grade_enum}/list")
    public ResponseEntity<?> subjectCourseAndGradeListView(@PageableDefault(page = 0, size=10, sort = "createdTime",
            direction = Sort.Direction.DESC ) Pageable pageable,
                                                   @PathVariable(value = "courseId") Long courseId,
                                                   @PathVariable(value = "grade_enum") String grades){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectPerCourseAndGradeList(pageable, courseId, grades);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    //    강좌 순위별 리스트 페이지 :: delyn n인 것만
    @GetMapping("/subject/rating/list")
    public ResponseEntity<?> subjectRatingListView(@PageableDefault(page = 0, size=4 ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectRatingList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    //    강좌 상단 노출용 리스트 페이지
    @GetMapping("/subject/main/list")
    public ResponseEntity<?> subjectMainListView(@PageableDefault(page = 0, size=10 ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectMainList(pageable);
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
    public ResponseEntity<?> subjectCreate(@ModelAttribute SubjectSaveReqDto dto,
                                           @RequestPart(value = "subjectThum", required = false) MultipartFile subjectThum){
        Subject subject = subjectService.subjectCreate(dto,subjectThum);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 생성이 완료되었습니다.",subject.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }


    //    강좌 업데이트
    @PatchMapping("/subject/update/{id}")
    public ResponseEntity<?> subjectUpdate(@PathVariable(value = "id") Long id, SubjectUpdateReqDto dto,
                                           @RequestPart(value = "subjectThum", required = false) MultipartFile subjectThum){
        Long subjectId = subjectService.subjectUpdate(id, dto, subjectThum);
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

    //    강좌 실제 DB상 삭제
    @DeleteMapping("/subject/delete/deep/{id}")
    public ResponseEntity<?> subjectDeleteDeep(@PathVariable(value = "id") Long id){
        Long subjectId = subjectService.subjectDeleteDeep(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject deep delete",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }



}
