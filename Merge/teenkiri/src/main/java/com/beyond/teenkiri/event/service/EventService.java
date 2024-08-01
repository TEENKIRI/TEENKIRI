package com.beyond.teenkiri.event.service;

import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.event.dto.EventDetailDto;
import com.beyond.teenkiri.event.dto.EventListResDto;
import com.beyond.teenkiri.event.dto.EventSaveReqDto;
import com.beyond.teenkiri.event.dto.EventUpdateDto;
import com.beyond.teenkiri.event.repository.EventRepository;
import com.beyond.teenkiri.user_board.domain.Role;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Event createEvent(EventSaveReqDto dto) {
        user user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        Event Event = dto.toEntity(user);
        return eventRepository.save(Event);
    }

    public Page<EventListResDto> eventList(Pageable pageable) {
        Page<Event> Events = eventRepository.findAll(pageable);
        return Events.map(a->a.listFromEntity());
    }

    public EventDetailDto getEventDetail(Long id) {
        Event Event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        return Event.fromDetailEntity();
    }

    @Transactional
    public void eventUpdate(Long id, EventUpdateDto dto){
        Event Event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        Event.toUpdate(dto);
        eventRepository.save(Event);
    }

    @Transactional
    public void eventDelete(Long id) {
        Event Event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        eventRepository.delete(Event);
    }
}
