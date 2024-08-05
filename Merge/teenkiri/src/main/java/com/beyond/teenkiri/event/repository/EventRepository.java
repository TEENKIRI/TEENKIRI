package com.beyond.teenkiri.event.repository;

import com.beyond.teenkiri.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
