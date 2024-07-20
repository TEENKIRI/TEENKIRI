package com.beyond.board_demo.comment.service;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.comment.dto.CommentResDto;
import com.beyond.board_demo.comment.dto.CommentSaveReqDto;
import com.beyond.board_demo.comment.repository.CommentRepository;
import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.notice.repository.NoticeRepository;
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
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final QnARepository qnaRepository;
    private final NoticeRepository noticeRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, QnARepository qnaRepository, NoticeRepository noticeRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.qnaRepository = qnaRepository;
        this.noticeRepository = noticeRepository;
    }

    @Transactional
    public CommentResDto createCommentForPost(CommentSaveReqDto dto, Long postId) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = dto.toEntity(user, post, null, null);
        Comment savedComment = commentRepository.save(comment);

        return CommentResDto.fromEntity(savedComment);
    }

    @Transactional
    public CommentResDto createCommentForQnA(CommentSaveReqDto dto, Long qnaId) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("QnA not found"));

        Comment comment = dto.toEntity(user, null, qna, null);
        Comment savedComment = commentRepository.save(comment);

        return CommentResDto.fromEntity(savedComment);
    }

    @Transactional
    public CommentResDto createCommentForNotice(CommentSaveReqDto dto, Long noticeId) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        Comment comment = dto.toEntity(user, null, null, notice);
        Comment savedComment = commentRepository.save(comment);

        return CommentResDto.fromEntity(savedComment);
    }

    public List<CommentResDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        return commentRepository.findByPost(post).stream()
                .map(CommentResDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentResDto> getCommentsByQnaId(Long qnaId) {
        QnA qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new EntityNotFoundException("QnA not found"));

        return commentRepository.findByQna(qna).stream()
                .map(CommentResDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CommentResDto> getCommentsByNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("Notice not found"));

        return commentRepository.findByNotice(notice).stream()
                .map(CommentResDto::fromEntity)
                .collect(Collectors.toList());
    }
}
