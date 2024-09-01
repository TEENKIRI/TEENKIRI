// QnAService.java
package com.beyond.teenkiri.qna.service;

import com.beyond.teenkiri.comment.dto.CommentDetailDto;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.comment.service.CommentService;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.notification.controller.SseController;
import com.beyond.teenkiri.notification.domain.Notification;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.*;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.subject.domain.Subject;
import com.beyond.teenkiri.subject.repository.SubjectRepository;
import com.beyond.teenkiri.subject.service.SubjectService;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QnAService {

    private final QnARepository qnARepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final UploadAwsFileService uploadAwsFileService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final SseController sseController;
    private final SubjectRepository subjectRepository;
    private final SubjectService subjectService;

    @Autowired
    public QnAService(QnARepository qnARepository, UserService userService, CommentRepository commentRepository, UploadAwsFileService uploadAwsFileService, CommentService commentService, UserRepository userRepository, NotificationRepository notificationRepository, SseController sseController, SubjectRepository subjectRepository, SubjectService subjectService) {
        this.qnARepository = qnARepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.uploadAwsFileService = uploadAwsFileService;
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.sseController = sseController;
        this.subjectRepository = subjectRepository;
        this.subjectService = subjectService;
    }

    @Transactional
    public QnA createQuestion(QnASaveReqDto dto, MultipartFile imageSsr) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        // subjectId를 통해 Subject를 조회
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("해당 강좌가 존재하지 않습니다."));

        MultipartFile image = (imageSsr == null) ? dto.getQImage() : imageSsr;
        QnA qnA = dto.toEntity(user, subject);  // Subject를 설정하여 QnA 생성

        qnA = qnARepository.save(qnA);
        try {
            if (image != null && !image.isEmpty()) {
                String bgImagePathFileName = qnA.getId() + "_question_" + image.getOriginalFilename();
                byte[] bgImagePathByte = image.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                qnA.qUpdateImagePath(s3ImagePath);  // 질문 이미지 경로 업데이트
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }

        Notification notification = new Notification();
        notification = notification.saveDto(qnA.getId(), null, null, qnA.getSubject().getUserTeacher().getEmail(), qnA.getTitle()+ " 강좌에 대한 질문이 달렸습니다.");
        notificationRepository.save(notification);
        sseController.publishMessage(notification);
        return qnARepository.save(qnA);
    }

//    public Page<QnAListResDto> qnaList(Pageable pageable) {
//        Page<QnA> qnAS = qnARepository.findByDelYN(DelYN.N, pageable);
//        return qnAS.map(QnA::listFromEntity);
//    }

    public Page<QnAListResDto> qnaListByGroup(Long subjectId, Pageable pageable){
        Subject subject = subjectService.findSubjectById(subjectId);
        Page<QnA> qnAS = qnARepository.findAllBySubjectIdAndDelYN(subject.getId(), DelYN.N, pageable );
        Page<QnAListResDto> qnAListResDtos = qnAS.map(a->a.listFromEntity());
        return qnAListResDtos;
    }

    public Page<QnAListResDto> qnaListWithSearch(Pageable pageable, String searchCategory, String searchQuery) {
        if (searchCategory != null && searchQuery != null) {
            switch (searchCategory) {
                case "제목":
                    return qnARepository.findByTitleContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(QnA::listFromEntity);
                case "작성자":
                    return qnARepository.findByUser_NicknameContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(QnA::listFromEntity);
                case "강좌명":
                    return qnARepository.findBySubjectTitleContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(QnA::listFromEntity);
                case "all":  // 제목, 작성자, 강좌명을 모두 검색
                    return qnARepository.findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCaseOrSubjectTitleContainingIgnoreCaseAndDelYN(
                                    searchQuery, searchQuery, searchQuery, DelYN.N, pageable)
                            .map(QnA::listFromEntity);
                default:
                    return qnARepository.findByDelYN(DelYN.N, pageable).map(QnA::listFromEntity);
            }
        } else {
            return qnARepository.findByDelYN(DelYN.N, pageable).map(QnA::listFromEntity);
        }
    }


    //사용자가 작성한 qna 목록 보기
    public List<QnAListResDto> getUserQnAs() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        List<QnA> qnAs = qnARepository.findByUser(user);
        return qnAs.stream().map(QnA::listFromEntity).collect(Collectors.toList()); // DTO로 변환
    }

    public QnADetailDto getQuestionDetail(Long id) {
        QnA qna = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 댓글 목록 가져오기
        List<CommentDetailDto> comments = commentService.getCommentsByQnaId(id);

        // QnA와 댓글을 사용해 QnADetailDto 생성
        QnADetailDto qnADetailDto = QnADetailDto.fromEntity(qna, comments);
        return qnADetailDto;
    }
    @Transactional
    public QnA answerQuestion(Long id, QnAAnswerReqDto dto, MultipartFile imageSsr) {
        User answeredBy = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (answeredBy == null || answeredBy.getRole().equals("ADMIN") || answeredBy.getRole().equals("TEACHER")) {
            throw new SecurityException("권한이 없습니다.");
        }
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));

        // 답변 내용 업데이트
        qnA.answerQuestion(dto.getAnswerText(), answeredBy);

        MultipartFile image = (imageSsr == null) ? dto.getAImage() : imageSsr;
        try {
            if (image != null && !image.isEmpty()) {
                String bgImagePathFileName =  qnA.getId() + "_answer_" + image.getOriginalFilename();
                byte[] bgImagePathByte = image.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                qnA.aUpdateImagePath(s3ImagePath);  // 답변 이미지 경로 업데이트
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // 댓글 저장 후 게시글 작성자에게 알림 전송


        Notification notification = new Notification();
        notification = notification.saveDto(qnA.getId(), null, null, qnA.getUser().getEmail(), qnA.getTitle()+ " 질문에 대한 답변이 달렸습니다.");
        notificationRepository.save(notification);
        sseController.publishMessage(notification);
        return qnARepository.save(qnA);
    }

    // 질문 수정
    @Transactional
    public void QnAQUpdate(Long id, QnAQtoUpdateDto dto, MultipartFile imageSsr) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getQImage();

        if (qnA.getUser().getEmail().equals(userEmail)) {
            try {
                String s3ImagePath = qnA.getAImageUrl(); // 기존 이미지 경로

                if (image != null && !image.isEmpty()) {
                    // 이미지가 존재할 때만 새로운 이미지 처리
                    String bgImagePathFileName = qnA.getId() + "_question_" + image.getOriginalFilename();
                    byte[] bgImagePathByte = image.getBytes();
                    s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    qnA.QnAQUpdate(dto, s3ImagePath);
                }else {
                    qnA.QnAQUpdate(dto, qnA.getQImageUrl());
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.", e);
            }
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        Notification notification = new Notification();
        notification = notification.saveDto(qnA.getId(), null, null, qnA.getSubject().getUserTeacher().getEmail(), qnA.getTitle()+ " 강좌에 대한 질문이 수정되었습니다.");
        notificationRepository.save(notification);
        sseController.publishMessage(notification);

        qnARepository.save(qnA);
    }



    @Transactional
    public void QnAAUpdate(Long id, QnAAtoUpdateDto dto, MultipartFile imageSsr) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User answeredBy = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 선생님 또는 관리자입니다. 고객센터에 문의하세요"));
        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getAImage();

        System.out.println(answeredBy.getEmail() + answeredBy.getRole());
        if (answeredBy.getRole() == Role.ADMIN || answeredBy.getRole() == Role.TEACHER) {
            try {
                if (image != null && !image.isEmpty()) {
                    String bgImagePathFileName = qnA.getId() + "_answer_" + image.getOriginalFilename();
                    byte[] bgImagePathByte = image.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    qnA.QnAAUpdate(dto, s3ImagePath);
                } else {
                    // 이미지가 비어 있거나 null이면 이미지 경로 없이 다른 정보만 업데이트
                    qnA.QnAAUpdate(dto, qnA.getAImageUrl());
                }
                qnARepository.save(qnA);
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.", e);
            }
        } else {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
        Notification notification = new Notification();
        notification = notification.saveDto(qnA.getId(), null, null, qnA.getUser().getEmail(), qnA.getTitle()+ " 질문에 대한 답변이 수정되었습니다.");
        notificationRepository.save(notification);
        sseController.publishMessage(notification);
    }


    @Transactional
    public QnA qnaDelete(Long id) {
        QnA qnA = qnARepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        qnA.updateDelYN(DelYN.Y);
        return qnA;
    }
}
