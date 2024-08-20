package com.beyond.teenkiri.course.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.course.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
    Page<Course> findByDelYN(DelYN delYN, Pageable pageable);
}
