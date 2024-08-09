package com.beyond.teenkiri.notice.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.event.domain.Event;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final UploadAwsFileService uploadAwsFileService;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository, UserRepository userRepository, UploadAwsFileService uploadAwsFileService) {
        this.noticeRepository = noticeRepository;
        this.userRepository = userRepository;
        this.uploadAwsFileService = uploadAwsFileService;
    }

    @Transactional
    public Notice createNotice(NoticeSaveReqDto dto, MultipartFile imageSsr) {
        // 현재 인증된 사용자의 이메일을 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 이메일로 사용자 정보 조회
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 사용자가 ADMIN 권한이 있는지 확인
        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;
        Notice notice = dto.toEntity();
        try {
            MultipartFile imageFile = image;
            if (!imageFile.isEmpty()) {
                String bgImagePathFileName = notice.getId() + "_" + imageFile.getOriginalFilename();
                byte[] bgImagePathByte = imageFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                notice.updateImagePath(s3ImagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.");
        }
        // Notice 엔티티 생성 및 User 설정
        notice.setUser(user);
        // Notice 엔티티를 데이터베이스에 저장
        return noticeRepository.save(notice);
    }



    public Page<NoticeListResDto> noticeList(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findByDelYN(DelYN.N, pageable);
        return notices.map(a->a.listFromEntity());
    }

    public NoticeDetailDto getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        return notice.fromDetailEntity();
    }

    @Transactional
    public void noticeUpdate(Long id, NoticeUpdateDto dto, MultipartFile imageSsr){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        MultipartFile image = (imageSsr != null) ? dto.getImage() : imageSsr;
        if (notice.getUser().getEmail().equals(userEmail)) {
            try {
                MultipartFile imageFile = image;
                if (!imageFile.isEmpty()) {
                    String bgImagePathFileName = notice.getId() + "_" + imageFile.getOriginalFilename();
                    byte[] bgImagePathByte = imageFile.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    notice.toUpdate(dto, s3ImagePath);
//                post.updateImagePath(s3ImagePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.");
            }
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        noticeRepository.save(notice);
    }

    @Transactional
    public Notice noticeDelete(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        notice.updateDelYN(DelYN.Y);
        return notice;
    }
}
