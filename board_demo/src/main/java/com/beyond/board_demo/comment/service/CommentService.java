package com.beyond.board_demo.comment.service;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.comment.dto.CommentDetailDto;
import com.beyond.board_demo.comment.dto.CommentSaveReqDto;
import com.beyond.board_demo.comment.repository.CommentRepository;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.post.repository.PostRepository;
import com.beyond.board_demo.qna.domain.QnA;
import com.beyond.board_demo.qna.repository.QnARepository;
import com.beyond.board_demo.user.domain.User;
import com.beyond.board_demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final QnARepository qnaRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, QnARepository qnaRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.qnaRepository = qnaRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Comment saveComment(CommentSaveReqDto dto) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);

        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));
            comment.setPost(post);
        } else if (dto.getQnaId() != null) {
            QnA qna = qnaRepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("QnA not found"));
            comment.setQna(qna);
        } else {
            throw new IllegalArgumentException("Either postId or qnaId must be provided");
        }

        return commentRepository.save(comment);
    }

    public List<CommentDetailDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .userEmail(comment.getUser().getEmail())
                        .createdTime(comment.getCreatedTime())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CommentDetailDto> getCommentsByQnaId(Long qnaId) {
        List<Comment> comments = commentRepository.findByQnaId(qnaId);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .userEmail(comment.getUser().getEmail())
                        .createdTime(comment.getCreatedTime())
                        .build())
                .collect(Collectors.toList());
    }
}

