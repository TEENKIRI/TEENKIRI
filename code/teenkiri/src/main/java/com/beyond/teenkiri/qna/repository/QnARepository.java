package com.beyond.teenkiri.qna.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user_board.domain.user;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnARepository extends JpaRepository<QnA, Long> {
    Page<QnA> findAll(Pageable pageable);
    Page<QnA> findByDelYN(DelYN delYN, Pageable pageable);
    List<QnA> findByUser(User user);
}