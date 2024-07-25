package com.beyond.board_demo.qna.service;

import com.beyond.board_demo.comment.domain.Comment;
import com.beyond.board_demo.comment.dto.CommentSaveReqDto;
import com.beyond.board_demo.comment.repository.CommentRepository;
import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.post.domain.Post;
import com.beyond.board_demo.post.dto.PostUpdateDto;
import com.beyond.board_demo.qna.domain.QnA;
import com.beyond.board_demo.qna.dto.*;
import com.beyond.board_demo.qna.repository.QnARepository;
import com.beyond.board_demo.user.domain.User;
import com.beyond.board_demo.user.repository.UserRepository;
import com.beyond.board_demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class QnAService {

    private final QnARepository qnARepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public QnAService(QnARepository qnARepository, UserRepository userRepository, UserService userService, CommentRepository commentRepository) {
        this.qnARepository = qnARepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public QnA createQuestion(QnASaveReqDto dto) {
        User user = userService.findByEmail(dto.getUserEmail());
        QnA qnA = dto.toEntity(user);
        return qnARepository.save(qnA);
    }

    public Page<QnAListResDto> qnaList(Pageable pageable) {
        Page<QnA> qnAS = qnARepository.findAll(pageable);
        Page<QnAListResDto> qnAListResDtos = qnAS.map(a -> a.listFromEntity());
        return qnAListResDtos;
    }

    public QnA getQuestionDetail(Long id) {
        return qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional
    public QnA answerQuestion(Long id, QnAAnswerReqDto dto) {
        QnA qna = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        User answeredBy = userService.findByEmail(dto.getAnswererEmail());
        if (answeredBy == null) {
            throw new EntityNotFoundException("Answerer not found");
        }

        qna.setAnswerText(dto.getAnswerText());
        qna.setAnsweredBy(answeredBy);
        qna.setAnsweredAt(LocalDateTime.now());
        return qnARepository.save(qna);
    }

    @Transactional
    public void QnAQUpdate(Long id, QnAQtoUpdateDto dto){
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QnA is not found"));
        qnA.QnAQUpdate(dto);
        qnARepository.save(qnA);
    }

    @Transactional
    public void QnAAUpdate(Long id, QnAAtoUpdateDto dto){
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QnA is not found"));
        qnA.QnAAUpdate(dto);
        qnARepository.save(qnA);
    }

    @Transactional
    public void qnaDelete(Long id) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        qnARepository.delete(qnA);
    }
}
