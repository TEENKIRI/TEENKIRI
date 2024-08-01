package com.beyond.teenkiri.course.service;

import com.beyond.teenkiri.course.domain.Course;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.course.dto.CourseSaveReqDto;
import com.beyond.teenkiri.course.repository.CourseRepository;
import com.beyond.teenkiri.user_board.domain.Role;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


//    과목 리스트
    public Page<CourseListResDto> courseList(Pageable pageable){
        Page<Course> courses = courseRepository.findAll(pageable);
        Page<CourseListResDto> courseListResDtos = courses.map(a->a.fromEntity());
        return courseListResDtos;
    }

//    과목 생성
    public Course courseCreate(CourseSaveReqDto dto){
//        🚨추후 멤버..
        user user = userRepository.findById(dto.getUserId()).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다."));

        if(!user.getRole().equals(Role.ADMIN)){ // 관리자 레벨만 강좌를 생성할 수 있도록 권한설정
            throw new IllegalArgumentException("권한이 부족합니다.");
        }

        Course course = dto.toEntity();
        courseRepository.save(course);

        return course; // save된 subject return;
    }

}
