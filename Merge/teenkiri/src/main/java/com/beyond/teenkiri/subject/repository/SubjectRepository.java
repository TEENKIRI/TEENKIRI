package com.beyond.teenkiri.subject.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.subject.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBydelYN(DelYN delYN, Pageable pageable);
    Page<Subject> findAllBydelYNOrderByRatingDesc(DelYN delYN, Pageable pageable);
    List<Subject> findBydelYN(DelYN delYN);
    // 제목으로 Subject를 찾는 메서드
    Optional<Subject> findByTitle(String title);
}
