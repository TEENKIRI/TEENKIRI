package com.beyond.teenkiri.qna.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findAll(Pageable pageable);
    Page<QnA> findByDelYN(DelYN delYN, Pageable pageable);
}