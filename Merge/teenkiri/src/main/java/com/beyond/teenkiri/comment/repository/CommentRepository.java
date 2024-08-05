package com.beyond.teenkiri.comment.repository;

import com.beyond.teenkiri.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    List<Comment> findByQnaId(Long qnaId);
}
