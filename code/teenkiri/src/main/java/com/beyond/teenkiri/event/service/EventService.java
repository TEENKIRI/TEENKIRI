package com.beyond.teenkiri.event.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.event.domain.Event;
import com.beyond.teenkiri.event.dto.EventDetailDto;
import com.beyond.teenkiri.event.dto.EventListResDto;
import com.beyond.teenkiri.event.dto.EventSaveReqDto;
import com.beyond.teenkiri.event.dto.EventUpdateDto;
import com.beyond.teenkiri.event.repository.EventRepository;


import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UploadAwsFileService uploadAwsFileService;


    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, UploadAwsFileService uploadAwsFileService) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.uploadAwsFileService = uploadAwsFileService;
    }

    @Transactional
    public Event createEvent(EventSaveReqDto dto, MultipartFile imageSsr) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;
        Event event = dto.toEntity();
        try {
            MultipartFile imageFile = image;
            if (!imageFile.isEmpty()) {
                String bgImagePathFileName = event.getId() + "_" + imageFile.getOriginalFilename();
                byte[] bgImagePathByte = imageFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                event.updateImagePath(s3ImagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.");
        }
        event.setUser(user);
        return eventRepository.save(event);
    }

    public Page<EventListResDto> eventList(Pageable pageable) {
        Page<Event> Events = eventRepository.findByDelYN(DelYN.N, pageable);
        return Events.map(a -> a.listFromEntity());
    }

    public EventDetailDto getEventDetail(Long id) {
        Event Event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이벤트입니다."));
        return Event.fromDetailEntity();
    }

    // 이벤트 업데이트입니다.
    @Transactional
    public void eventUpdate(Long id, EventUpdateDto dto, MultipartFile imageSsr) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        MultipartFile image = (imageSsr != null) ? dto.getImage() : imageSsr;
        if (event.getUser().getEmail().equals(userEmail)) {
            try {
                MultipartFile imageFile = image;
                if (!imageFile.isEmpty()) {
                    String bgImagePathFileName = event.getId() + "_" + imageFile.getOriginalFilename();
                    byte[] bgImagePathByte = imageFile.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    event.toUpdate(dto, s3ImagePath);
//                post.updateImagePath(s3ImagePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.");
            }
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        eventRepository.save(event);
    }

    @Transactional
    public Event eventDelete(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        User user = event.getUser();
        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        event.updateDelYN(DelYN.Y);
        return event;
    }
}
