package com.beyond.teenkiri.subscribe.repository;

import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subscribe.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    // 제목으로 Subject를 찾는 메서드
    Optional<Subject> findByTitle(String title);
}
