package com.beyond.teenkiri.qna.service;

import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.*;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.user_board.domain.Role;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class QnAService {

    private final QnARepository qnARepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Autowired
    public QnAService(QnARepository qnARepository, UserService userService, CommentRepository commentRepository) {
        this.qnARepository = qnARepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public QnA createQuestion(QnASaveReqDto dto) {
        user user = userService.findByEmail(dto.getUserEmail());
        QnA qnA = dto.toEntity(user);
        return qnARepository.save(qnA);
    }

    public Page<QnAListResDto> qnaList(Pageable pageable) {
        Page<QnA> qnAS = qnARepository.findByDelYN(DelYN.N, pageable);
        return qnAS.map(QnA::listFromEntity);
    }

    public QnA getQuestionDetail(Long id) {
        return qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!ERROR"));
    }

    @Transactional
    public QnA answerQuestion(Long id, QnAAnswerReqDto dto) {
        user answeredBy = userService.findByEmail(dto.getAnswererBy());
//        User answeredBy = userService.findByEmail(dto.getAnswererEmail());
        if (answeredBy == null || answeredBy.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        QnA qna = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"));
        qna = dto.toEntity(answeredBy, qna);
        return qnARepository.save(qna);
    }

    @Transactional
    public void QnAQUpdate(Long id, QnAQtoUpdateDto dto) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QnA is not found"));
        qnA.QnAQUpdate(dto);
        qnARepository.save(qnA);
    }

    @Transactional
    public void QnAAUpdate(Long id, QnAAtoUpdateDto dto) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QnA is not found"));
        user answeredBy = userService.findByEmail(dto.getAnswererEmail());
        if (answeredBy == null || answeredBy.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        qnA.QnAAUpdate(dto);
        qnARepository.save(qnA);
    }

    @Transactional
    public QnA qnaDelete(Long id) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        qnA.updateDelYN(DelYN.Y);
        //qnARepository.delete(qnA);
        return qnA;
    }
}