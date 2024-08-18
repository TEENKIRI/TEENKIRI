package com.beyond.teenkiri.subject.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.subject.domain.Grade;
import com.beyond.teenkiri.subject.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Page<Subject> findByDelYN(DelYN delYN, Pageable pageable);
    Page<Subject> findAllByDelYNOrderByRatingDesc(DelYN delYN, Pageable pageable);  // 수정된 부분
    Page<Subject> findByCourseIdAndDelYN(Long courseId, DelYN delYN, Pageable pageable);
    Page<Subject> findByCourseIdAndGradeInAndDelYN(Long courseId, List<Grade> grade, DelYN delYN, Pageable pageable);
    Page<Subject> findByIsMainSubjectAndDelYN(Boolean isMainSubject, DelYN delYN, Pageable pageable);

    Page<Subject> findByTitleContainingAndDelYN(String title, DelYN delYN, Pageable pageable);
    Page<Subject> findByUserTeacherNameContainingAndDelYN(String name, DelYN delYN, Pageable pageable);

    Page<Subject> findAllByDelYNOrderByCreatedTimeDesc(DelYN delYN, Pageable pageable); // 최신순
    Page<Subject> findByDelYNOrderByCreatedTimeDesc(DelYN delYN, Pageable pageable); // 최신순 정렬 메서드

}



