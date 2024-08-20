package com.beyond.teenkiri.comment.repository;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndDelYN(Long postId, DelYN delYN);
    List<Comment> findByQnaIdAndDelYN(Long qnaId, DelYN delYN);
    List<Comment> findByDelYN(DelYN delYN);

}
