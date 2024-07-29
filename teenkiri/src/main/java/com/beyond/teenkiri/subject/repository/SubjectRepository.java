package com.beyond.teenkiri.subject.repository;

import com.beyond.teenkiri.common.DelYN;
import com.beyond.teenkiri.subject.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Page<Subject> findBydelYN(DelYN delYN, Pageable pageable);
}
