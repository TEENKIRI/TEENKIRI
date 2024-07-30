package com.beyond.teenkiri.notice.repository;

import com.beyond.teenkiri.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
