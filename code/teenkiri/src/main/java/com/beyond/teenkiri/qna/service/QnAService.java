// QnAService.java
package com.beyond.teenkiri.qna.service;

import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.*;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class QnAService {

    private final QnARepository qnARepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    //rtrt
    @Autowired
    public QnAService(QnARepository qnARepository, UserService userService, CommentRepository commentRepository) {
        this.qnARepository = qnARepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public QnA createQuestion(QnASaveReqDto dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        QnA qnA = dto.toEntity(user);
        return qnARepository.save(qnA);
    }

    public Page<QnAListResDto> qnaList(Pageable pageable) {
        Page<QnA> qnAS = qnARepository.findByDelYN(DelYN.N, pageable);
        return qnAS.map(QnA::listFromEntity);
    }

    public QnA getQuestionDetail(Long id) {
        return qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
    }

    @Transactional
    public QnA answerQuestion(Long id, QnAAnswerReqDto dto) {
        User answeredBy = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (answeredBy == null || answeredBy.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        QnA qna = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        qna = dto.toEntity(answeredBy, qna);
        return qnARepository.save(qna);
    }

    @Transactional
    public void QnAQUpdate(Long id, QnAQtoUpdateDto dto) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        if (qnA.getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            qnA.QnAQUpdate(dto);
        }else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        qnARepository.save(qnA);
    }

    @Transactional
    public void QnAAUpdate(Long id, QnAAtoUpdateDto dto) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User answeredBy = userService.findByEmail(userEmail);
        if (answeredBy == null || answeredBy.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        if (qnA.getAnswerer().getEmail().equals(userEmail)) {
            qnA.QnAAUpdate(dto);
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        qnARepository.save(qnA);
    }

    @Transactional
    public QnA qnaDelete(Long id) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        qnA.updateDelYN(DelYN.Y);
        return qnA;
    }
}
