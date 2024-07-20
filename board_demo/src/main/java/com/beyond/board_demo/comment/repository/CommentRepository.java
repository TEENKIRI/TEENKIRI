package com.beyond.board_demo.comment.repository;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.qna.domain.QnA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByQna(QnA qna);
    List<Comment> findByNotice(Notice notice);
}
