package com.beyond.teenkiri.notice.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notice.domain.Notice;
import com.beyond.teenkiri.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
    Page<Notice> findByDelYN(DelYN delYN, Pageable pageable);

    Page<Notice> findByTitleContainingIgnoreCaseAndDelYN(String title, DelYN delYN, Pageable pageable);
    Page<Notice> findByUserNicknameContainingIgnoreCaseAndDelYN(String userNickname, DelYN delYN, Pageable pageable);
    Page<Notice> findByTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCaseAndDelYN(String title, String nickname, DelYN delYN, Pageable pageable);
}