package com.beyond.teenkiri.notice.service;

import com.beyond.teenkiri.notice.domain.Notice;
import com.beyond.teenkiri.notice.dto.NoticeListResDto;
import com.beyond.teenkiri.notice.dto.NoticeSaveReqDto;
import com.beyond.teenkiri.notice.dto.NoticeDetailDto;
import com.beyond.teenkiri.notice.dto.NoticeUpdateDto;
import com.beyond.teenkiri.notice.repository.NoticeRepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Notice createNotice(NoticeSaveReqDto dto) {
        User user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        Notice notice = dto.toEntity(user);
        return noticeRepository.save(notice);
    }

    public Page<NoticeListResDto> noticeList(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return notices.map(a->a.listFromEntity());
    }

    public NoticeDetailDto getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        return notice.fromDetailEntity();
    }

    @Transactional
    public void noticeUpdate(Long id, NoticeUpdateDto dto){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        notice.toUpdate(dto);
        noticeRepository.save(notice);
    }

    @Transactional
    public void noticeDelete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        noticeRepository.delete(notice);
    }
}
