package com.beyond.teenkiri.subject.repository;

import com.beyond.teenkiri.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
}
