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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService){
        this.subjectService = subjectService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> subjectListView(
            @PageableDefault(page = 0, size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "sortType", required = false, defaultValue = "latest") String sortType,
            @RequestParam(value = "grades", required = false) String grades,
            @RequestParam(value = "courseId", required = false) Long courseId) {

        Page<SubjectListResDto> subjectListResDto = subjectService.subjectList(pageable, search, searchType, sortType, grades, courseId);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject list", subjectListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }



    @GetMapping("/{courseId}/list")
    public ResponseEntity<?> subjectCourseListView(
            @PageableDefault(page = 0, size=10, sort = "createdTime", direction = Sort.Direction.DESC ) Pageable pageable,
            @PathVariable(value = "courseId") Long courseId,
            @RequestParam(value = "grades", required = false) String grades) {

        Page<SubjectListResDto> subjectListResDto = subjectService.subjectPerCourseAndGradeList(pageable, courseId, grades);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list", subjectListResDto);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/rating/list")
    public ResponseEntity<?> subjectRatingListView(@PageableDefault(page = 0, size=4 ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectRatingList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject rating list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @GetMapping("/main/list")
    public ResponseEntity<?> subjectMainListView(@PageableDefault(page = 0, size=10 ) Pageable pageable){
        Page<SubjectListResDto> subjectListResDto = subjectService.subjectMainList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject main list",subjectListResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> subjectDetailView(@PathVariable(value = "id") Long id){
        SubjectDetResDto subjectDetResDto = subjectService.subjectDetail(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "return subject detail",subjectDetResDto);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> subjectCreate(@ModelAttribute SubjectSaveReqDto dto,
                                           @RequestPart(value = "subjectThum", required = false) MultipartFile subjectThum){
        Subject subject = subjectService.subjectCreate(dto,subjectThum);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"강좌 생성이 완료되었습니다.",subject.getId());
        return new ResponseEntity<>(commonResDto,HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> subjectUpdate(@PathVariable(value = "id") Long id, SubjectUpdateReqDto dto,
                                           @RequestPart(value = "subjectThum", required = false) MultipartFile subjectThum){
        Long subjectId = subjectService.subjectUpdate(id, dto, subjectThum);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject update",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> subjectDelete(@PathVariable(value = "id") Long id){
        Long subjectId = subjectService.subjectDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject delete",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }

    @DeleteMapping("/delete/deep/{id}")
    public ResponseEntity<?> subjectDeleteDeep(@PathVariable(value = "id") Long id){
        Long subjectId = subjectService.subjectDeleteDeep(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "complete subject deep delete",subjectId);
        return new ResponseEntity<>(commonResDto,HttpStatus.OK);
    }
}
