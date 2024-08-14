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


        Event event = dto.toEntity();

        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;

        if (image != null && !image.isEmpty()) {
            try {

                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    String bgImagePathFileName = event.getId() + "_" + originalFilename;
                    byte[] bgImagePathByte = image.getBytes();


                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    event.updateImagePath(s3ImagePath);
                } else {

                    throw new IllegalArgumentException("이미지 파일 이름이 유효하지 않습니다.");
                }
            } catch (IOException e) {
                throw new RuntimeException("파일 저장에 실패했습니다.", e);
            }
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

    @Transactional
    public void eventUpdate(Long id, EventUpdateDto dto, MultipartFile imageSsr) {
        // 기존 이벤트를 조회합니다.
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        // 현재 로그인된 사용자의 이메일을 가져옵니다.
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 로그인된 사용자가 게시글의 작성자인지 확인합니다.
        if (!event.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }

        // 새로 받은 이미지가 없으면, DTO의 이미지를 사용합니다. DTO의 이미지도 없으면 이미지를 건너뜁니다.
        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getImage();

        try {
            if (image != null && !image.isEmpty()) {
                // 이미지가 비어있지 않다면, S3에 이미지를 업로드하고 경로를 업데이트합니다.
                String bgImagePathFileName = event.getId() + "_" + image.getOriginalFilename();
                byte[] bgImagePathByte = image.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                event.toUpdate(dto, s3ImagePath); // 이미지를 포함하여 업데이트합니다.
            } else {
                // 이미지 없이 제목과 내용만 업데이트합니다.
                event.toUpdate(dto, event.getImageUrl());
            }
        } catch (IOException e) {
            throw new RuntimeException("게시글 수정에 실패했습니다.");
        }

        // 변경된 이벤트 정보를 저장합니다.
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
