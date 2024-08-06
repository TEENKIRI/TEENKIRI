package com.beyond.teenkiri.course.service;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseDetResDto;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.sevice.UserService;
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

}

