package com.beyond.teenkiri.subject.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.subject.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBydelYN(DelYN delYN, Pageable pageable);
    Page<Subject> findAllBydelYNOrderByRatingDesc(DelYN delYN, Pageable pageable);
    Page<Subject> findByCourseIdAndDelYN(Long courseId, DelYN delYN, Pageable pageable);
    Page<Subject> findByIsMainSubjectAndDelYN(Boolean isMainSubject, DelYN delYN, Pageable pageable);

}
