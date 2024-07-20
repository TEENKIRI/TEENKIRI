package com.beyond.board_demo.notice.service;

import com.beyond.board_demo.notice.domain.Notice;
import com.beyond.board_demo.notice.dto.NoticeListResDto;
import com.beyond.board_demo.notice.dto.NoticeSaveReqDto;
import com.beyond.board_demo.notice.dto.NoticeDetailDto;
import com.beyond.board_demo.notice.repository.NoticeRepository;
import com.beyond.board_demo.user.domain.Role;
import com.beyond.board_demo.user.domain.User;
import com.beyond.board_demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
    public Notice createNotice(NoticeSaveReqDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }

        Notice notice = dto.toEntity(user);
        return noticeRepository.save(notice);
    }

    public List<NoticeListResDto> getAllNotices() {
        List<Notice> notices = noticeRepository.findAll();
        List<NoticeListResDto> noticeList = new ArrayList<>();

        for(Notice notice : notices){
            noticeList.add(notice.fromListEntity());
        }
        return noticeList;
    }

    public NoticeDetailDto getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        return notice.fromDetailEntity();
    }
}
