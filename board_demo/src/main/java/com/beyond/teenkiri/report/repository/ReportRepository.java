package com.beyond.teenkiri.report.repository;

import com.beyond.teenkiri.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByQnaIsNotNull(Pageable pageable);
    Page<Report> findByPostIsNotNull(Pageable pageable);
}
