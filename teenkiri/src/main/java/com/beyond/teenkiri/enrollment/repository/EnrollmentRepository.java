package com.beyond.teenkiri.enrollment.repository;

import com.beyond.teenkiri.enrollment.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
}
