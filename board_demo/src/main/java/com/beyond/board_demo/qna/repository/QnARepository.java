package com.beyond.board_demo.qna.repository;

import com.beyond.board_demo.qna.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findAll(Pageable pageable);
}
