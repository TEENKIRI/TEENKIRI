package com.beyond.teenkiri.course.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseDetResDto;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.dto.CourseUpdateReqDto;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.lecture.domain.Lecture;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;

    public CourseService(CourseRepository courseRepository, UserService userService) {
        this.courseRepository = courseRepository;
        this.userService = userService;
    }


    //    과목 리스트
    public Page<CourseListResDto> courseList(Pageable pageable){
        Page<Course> courses = courseRepository.findAll(pageable);
        Page<CourseListResDto> courseListResDtos = courses.map(a->a.fromListEntity());
        return courseListResDtos;
    }

    //    과목 상세
    public CourseDetResDto courseDetail(Long id){
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("없는 과목입니다."));
        CourseDetResDto courseDetResDto = course.fromDetEntity();
        return courseDetResDto;
    }

    //    과목 생성
    public Course courseCreate(CourseSaveReqDto dto){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);

        if(!user.getRole().equals(Role.ADMIN)){ // 관리자 레벨만 강좌를 생성할 수 있도록 권한설정
            throw new IllegalArgumentException("권한이 부족합니다.");
        }

        Course course = dto.toEntity(user);
        courseRepository.save(course);

        return course; // save된 subject return;
    }

//    과목 업데이트
    public Course courseUpdate(Long id, CourseUpdateReqDto dto){
        Course course = courseRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("존재하지 않는 과목입니다."));
        course.updateTitle(dto);
        return course;
    }

//    과목 삭제
    public Course courseDelete(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("존재하지 않는 과목입니다."));
        course.updateDelYn(DelYN.Y);
        return course;
    }

//    과목 DB상 삭제
    public Long courseDeleteDeep(Long id){
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 과목입니다."));


        if (course.getSubjects().isEmpty()) { // 연결되어있는 강좌가 없는 경우
            courseRepository.deleteById(course.getId());
            return id;
        } else { // 연결되어있는 강좌가 존재하는 경우
            throw new RuntimeException("연결되어있는 강좌가 존재하여 삭제하실 수 없습니다.");
        }
    }


//    ==============
    public Course findByIdRequired(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 과목입니다."));
    }

    public Course findByIdReturnNull(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

}

