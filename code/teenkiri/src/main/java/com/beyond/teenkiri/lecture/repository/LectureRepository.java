package com.beyond.teenkiri.lecture.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.lecture.domain.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    Page<Lecture> findBydelYN(DelYN delYN, Pageable pageable);
    Page<Lecture> findBysubjectIdAndDelYN(DelYN delYN, Long subjectId, Pageable pageable);
    Page<Lecture> findAllBySubjectId(Long subjectId, Pageable pageable);
}
