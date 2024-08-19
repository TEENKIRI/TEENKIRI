package com.beyond.teenkiri.event.repository;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);
    Page<Event> findByDelYN(DelYN delYN, Pageable pageable);

    Page<Event> findByTitleContainingIgnoreCaseAndDelYN(String title, DelYN delYN, Pageable pageable);
    Page<Event> findByUserNicknameContainingIgnoreCaseAndDelYN(String userNickname, DelYN delYN, Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCaseAndDelYN(String title, String nickname, DelYN delYN, Pageable pageable);
}
