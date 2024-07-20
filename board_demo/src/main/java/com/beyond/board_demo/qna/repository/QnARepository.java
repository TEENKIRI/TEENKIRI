package com.beyond.board_demo.qna.repository;

import com.beyond.board_demo.qna.domain.QnA;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<QnA, Long> {
}
