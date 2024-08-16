package com.beyond.teenkiri.comment.service;

import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.comment.dto.CommentDetailDto;
import com.beyond.teenkiri.comment.dto.CommentSaveReqDto;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notification.controller.SseController;
import com.beyond.teenkiri.notification.dto.NotificationDto;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
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
    private final SseController sseController;
    private final NotificationRepository notificationRepository;


    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, QnARepository qnaRepository, UserRepository userRepository, SseController sseController, NotificationRepository notificationRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.qnaRepository = qnaRepository;
        this.userRepository = userRepository;
        this.sseController = sseController;
        this.notificationRepository = notificationRepository;
    }


    @Transactional
    public Comment saveComment(CommentSaveReqDto dto) {
        Long userId = dto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        String nickname = user.getNickname();
        String userEmail = user.getEmail();

        Comment savedComment;
        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
            savedComment = commentRepository.save(dto.PostToEntity(user, post, nickname));

            // 댓글 저장 후 게시글 작성자에게 알림 전송
            NotificationDto notificationDto = new NotificationDto(null, post.getId(), null, userEmail,"게시글에 새로운 댓글이 달렸습니다.");
            notificationRepository.save(notificationDto);
            sseController.publishMessage(notificationDto, post.getUser().getEmail());

        } else if (dto.getQnaId() != null) {
            QnA qna = qnaRepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
            savedComment = commentRepository.save(dto.QnAToEntity(user, qna, nickname));

            // 댓글 저장 후 QnA 작성자에게 알림 전송
            NotificationDto notificationDto = new NotificationDto(qna.getId(), null, null, userEmail, "QnA에 새로운 댓글이 달렸습니다.");
            notificationRepository.save(notificationDto);
            sseController.publishMessage(notificationDto, qna.getUser().getEmail());
        } else {
            throw new IllegalArgumentException("댓글이 달릴 게시글 또는 QnA ID가 필요합니다.");
        }

        return savedComment;
    }


    public List<CommentDetailDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndDelYN(postId, DelYN.N);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .userEmail(comment.getUser().getEmail())
                        .nickname(comment.getNickname())
                        .createdTime(comment.getCreatedTime())
                        .updatedTime(comment.getUpdatedTime())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CommentDetailDto> getCommentsByQnaId(Long qnaId) {
        List<Comment> comments = commentRepository.findByQnaIdAndDelYN(qnaId, DelYN.N);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .userEmail(comment.getUser().getEmail())
                        .nickname(comment.getNickname())
                        .createdTime(comment.getCreatedTime())
                        .updatedTime(comment.getUpdatedTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Comment commentDelete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
        comment.updateDelYN(DelYN.Y);
        return comment;
    }
}
