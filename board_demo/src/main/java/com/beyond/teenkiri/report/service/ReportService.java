package com.beyond.teenkiri.report.service;


import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.repository.QnARepository;
import com.beyond.teenkiri.report.domain.Reason;
import com.beyond.teenkiri.report.domain.Report;
import com.beyond.teenkiri.report.dto.ReportListResDto;
import com.beyond.teenkiri.report.dto.ReportSaveReqDto;
import com.beyond.teenkiri.report.repository.ReportRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.service.UserService;
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

    @Autowired
    public ReportService(ReportRepository repository, UserService userService, QnARepository qnARepository) {
        this.reportRepository = repository;
        this.userService = userService;
        this.qnARepository = qnARepository;
    }

    public Report reportCreate(ReportSaveReqDto dto){
        User user = userService.findByEmail(dto.getReportEmail());
        QnA qnA = qnARepository.findById(dto.getQnaId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 QnA입니다."));
        Report report = dto.toEntity(user, qnA);
        reportRepository.save(report);
        return report;
    }

    public Page<ReportListResDto> reportList(Pageable pageable){
        Page<Report> reports = reportRepository.findAll(pageable);
        return reports.map(a -> a.listFromEntity());
    }
}
