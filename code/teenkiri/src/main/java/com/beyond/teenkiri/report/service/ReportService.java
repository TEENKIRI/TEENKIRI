package com.beyond.teenkiri.report.service;

<<<<<<< HEAD
=======
import com.beyond.teenkiri.chat.domain.ChatMessage;
import com.beyond.teenkiri.chat.repository.ChatMessageRepository;
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.notification.controller.SseController;
import com.beyond.teenkiri.notification.dto.NotificationDto;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.report.domain.Report;
import com.beyond.teenkiri.report.dto.ReportListResDto;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.repository.ReportRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReportService {
    private final UserService userService;
    private final ReportRepository reportRepository;
    private final QnARepository qnARepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
<<<<<<< HEAD
=======
    private final ChatMessageRepository chatMessageRepository;
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
    private final NotificationRepository notificationRepository;
    private final SseController sseController;

    @Autowired
<<<<<<< HEAD
    public ReportService(ReportRepository reportRepository, UserService userService, QnARepository qnARepository, PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, NotificationRepository notificationRepository, SseController sseController) {
=======
    public ReportService(ReportRepository reportRepository, UserService userService, QnARepository qnARepository, PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, ChatMessageRepository chatMessageRepository, NotificationRepository notificationRepository, SseController sseController) {
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.qnARepository = qnARepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
<<<<<<< HEAD
=======
        this.chatMessageRepository = chatMessageRepository;
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
        this.notificationRepository = notificationRepository;
        this.sseController = sseController;
    }

    @Transactional
    public Report reportCreate(ReportSaveReqDto dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(userEmail);
        QnA qnA = null;
        Post post = null;
        Comment comment = null;
<<<<<<< HEAD
=======
        ChatMessage chatMessage = null; // ChatMessage 변수 추가
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583

        if (dto.getCommentId() != null) {
            comment = commentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Comment입니다."));
            post = comment.getPost();
            qnA = comment.getQna();
        } else if (dto.getPostId() != null) {
            post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Post입니다."));
        } else if (dto.getQnaId() != null) {
            qnA = qnARepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
<<<<<<< HEAD
        }

        // Report 엔티티 생성
        Report report = dto.toEntity(user, qnA, post, comment);
=======
        } else if (dto.getChatMessageId() != null) { // ChatMessage 처리 추가
            chatMessage = chatMessageRepository.findById(dto.getChatMessageId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 ChatMessage입니다."));
        }

        // Report 엔티티 생성
        Report report = dto.toEntity(user, qnA, post, comment, chatMessage); // ChatMessage 포함
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
        report = reportRepository.save(report);

        List<String> adminEmails = userRepository.findAllAdminEmails(); // 모든 관리자 이메일을 가져오는 메서드

        // 이메일이 'admin'으로 시작하는 관리자 필터링
        List<String> filteredAdminEmails = adminEmails.stream()
                .filter(email -> email.startsWith("admin"))
                .collect(Collectors.toList());

<<<<<<< HEAD
//         필터링된 이메일 목록에 대해 알림 전송
        for (String email : filteredAdminEmails) {
            NotificationDto notificationDto = new NotificationDto();

=======
        // 필터링된 이메일 목록에 대해 알림 전송
        for (String email : filteredAdminEmails) {
            NotificationDto notificationDto = new NotificationDto();
>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
            notificationDto = notificationDto.saveDto(null, null, report.getId(), email, report.getReason() + "으로 신고가 접수되었습니다.");
            notificationRepository.save(notificationDto);
            sseController.publishMessage(notificationDto);
        }
<<<<<<< HEAD
        // Report 엔티티를 저장
=======

>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
        return report;
    }

    public Page<ReportListResDto> reportList(Pageable pageable, String type) {
        Page<Report> reports;
        if ("qna".equals(type)) {
            reports = reportRepository.findByQnaIsNotNull(pageable);
        } else if ("post".equals(type)) {
            reports = reportRepository.findByPostIsNotNull(pageable);
        } else if ("comment".equals(type)) {
            reports = reportRepository.findByCommentIsNotNull(pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }
        return reports.map(Report::listFromEntity);
    }
}
<<<<<<< HEAD
=======



>>>>>>> e6061af56a74160608279f4e5af578e9a1bb9583
