package com.beyond.teenkiri.report.service;

import com.beyond.teenkiri.chat.domain.Chat;
import com.beyond.teenkiri.chat.repository.ChatRepository;
import com.beyond.teenkiri.comment.domain.Comment;
import com.beyond.teenkiri.comment.repository.CommentRepository;
import com.beyond.teenkiri.notification.controller.SseController;
import com.beyond.teenkiri.notification.domain.Notification;
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
    private final ChatRepository chatRepository;
    private final NotificationRepository notificationRepository;
    private final SseController sseController;

    @Autowired
    public ReportService(ReportRepository reportRepository, UserService userService, QnARepository qnARepository, PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository, ChatRepository chatRepository, NotificationRepository notificationRepository, SseController sseController) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.qnARepository = qnARepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
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
        Chat chat = null;
        String suspectEmail = null;

        if (dto.getCommentId() != null) {
            comment = commentRepository.findById(dto.getCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Comment입니다."));
            post = comment.getPost();
            qnA = comment.getQna();
            suspectEmail = comment.getUser().getEmail();
        } else if (dto.getPostId() != null) {
            post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Post입니다."));
            suspectEmail = post.getUser().getEmail();
        } else if (dto.getQnaId() != null) {
            qnA = qnARepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
            suspectEmail = qnA.getUser().getEmail();
        } else if (dto.getChatMessageId() != null) {
                chat = chatRepository.findById(dto.getChatMessageId())
                        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Chat입니다."));
                if (chat.getUser() == null) {
                    throw new IllegalStateException("Chat에 연관된 User가 없습니다.");
                }
                suspectEmail = chat.getUser().getEmail();
                System.out.println("피신고자 이메일 (채팅): " + suspectEmail);
            }


        Report report = dto.toEntity(user, qnA, post, comment, chat);
        report = reportRepository.save(report);

        List<String> adminEmails = userRepository.findAllAdminEmails();

        List<String> filteredAdminEmails = adminEmails.stream()
                .filter(email -> email.startsWith("admin"))
                .collect(Collectors.toList());

        for (String email : filteredAdminEmails) {
            Notification notification = new Notification();
            notification = notification.saveDto(null, null, report.getId(), email, report.getReason() + "으로 신고가 접수되었습니다.");
            notificationRepository.save(notification);
            sseController.publishMessage(notification);
        }

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
