package com.beyond.board_demo.notice.repository;

import com.beyond.board_demo.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
