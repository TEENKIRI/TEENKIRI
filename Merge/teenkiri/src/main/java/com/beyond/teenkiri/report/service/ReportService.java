package com.beyond.teenkiri.report.service;

import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.report.domain.Report;
import com.beyond.teenkiri.report.dto.ReportListResDto;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.repository.ReportRepository;
import com.beyond.teenkiri.user_board.domain.User;
import com.beyond.teenkiri.user_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserService userService;
    private final QnARepository qnARepository;
    private final PostRepository postRepository;

    @Autowired
    public ReportService(ReportRepository repository, UserService userService, QnARepository qnARepository, PostRepository postRepository) {
        this.reportRepository = repository;
        this.userService = userService;
        this.qnARepository = qnARepository;
        this.postRepository = postRepository;
    }


    @Transactional
    public Report reportCreate(ReportSaveReqDto dto){
        User user = userService.findByEmail(dto.getReportEmail());
        QnA qnA = null;
        Post post = null;

        if (dto.getQnaId() != null) {
            qnA = qnARepository.findById(dto.getQnaId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
        } else if (dto.getPostId() != null) {
            post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Post입니다."));
        }

        Report report = dto.toEntity(user, qnA, post);
        return reportRepository.save(report);
    }

    public Page<ReportListResDto> reportList(Pageable pageable, String type) {
        Page<Report> reports;
        if ("qna".equals(type)) {
            reports = reportRepository.findByQnaIsNotNull(pageable);
        } else if ("post".equals(type)) {
            reports = reportRepository.findByPostIsNotNull(pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }
        return reports.map(Report::listFromEntity);
    }
}
