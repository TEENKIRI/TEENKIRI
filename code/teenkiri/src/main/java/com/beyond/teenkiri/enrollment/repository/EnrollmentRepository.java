package com.beyond.teenkiri.enrollment.repository;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
//    Page<Enrollment> findAllBySubjectId(Long subjectId, Pageable pageable);

//    Optional<Enrollment> findByLectureIdAndUserId(Long userId, Long lectureId);
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.lecture.id = :lectureId")
    Optional<Enrollment> findByLectureIdAndUserId(@Param("userId") Long userId, @Param("lectureId") Long lectureId);

    Integer countByLectureId(Long lectureId);
}
