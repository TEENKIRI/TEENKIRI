package com.beyond.teenkiri.comment.service;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.comment.dto.CommentDetailDto;
import com.beyond.teenkiri.comment.dto.CommentSaveReqDto;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.repository.UserRepository;
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
        user user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
            return commentRepository.save(dto.PostToEntity(user, post));
        } else if (dto.getQnaId() != null) {
            QnA qna = qnaRepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
            return commentRepository.save(dto.QnAToEntity(user, qna));
        } else {
            throw new IllegalArgumentException("댓글이 달릴 게시글 또는 QnA ID가 필요합니다.");
        }
    }

    public List<CommentDetailDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .userEmail(comment.getUser().getEmail())
                        .createdTime(comment.getCreatedTime())
                        .updatedTime(comment.getUpdatedTime())
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
                        .updatedTime(comment.getUpdatedTime())
                        .build())
                .collect(Collectors.toList());
    }
}

