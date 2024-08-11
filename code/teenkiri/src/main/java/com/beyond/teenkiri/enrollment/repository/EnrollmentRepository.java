package com.beyond.teenkiri.enrollment.repository;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
//    Page<Enrollment> findAllBySubjectId(Long subjectId, Pageable pageable);

    Optional<Enrollment> findByLectureIdAndUserId(Long userId, Long lectureId);

    Integer countByLectureId(Long lectureId);
}
