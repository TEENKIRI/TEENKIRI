package com.beyond.teenkiri.lecture.repository;

import com.beyond.teenkiri.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
}
