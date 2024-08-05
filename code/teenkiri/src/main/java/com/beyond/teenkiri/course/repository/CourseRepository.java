package com.beyond.teenkiri.course.repository;

import com.beyond.teenkiri.course.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
