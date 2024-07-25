package com.beyond.board_demo.event.repository;

import com.beyond.board_demo.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
