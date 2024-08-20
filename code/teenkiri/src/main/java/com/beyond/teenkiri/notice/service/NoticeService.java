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
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.dto.PostListResDto;
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

import javax.persistence.Entity;
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

        // 이미지가 전달되지 않았으면 DTO에서 가져옴
        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;

        // Notice 엔티티 생성
        Notice notice = dto.toEntity();

        // 이미지가 존재하고 비어있지 않을 경우 처리
        if (image != null && !image.isEmpty()) {
            try {
                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    String bgImagePathFileName = notice.getId() + "_" + originalFilename;
                    byte[] bgImagePathByte = image.getBytes();

                    // S3에 파일 업로드 후 경로 설정
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    notice.updateImagePath(s3ImagePath);
                } else {
                    throw new IllegalArgumentException("이미지 파일 이름이 유효하지 않습니다.");
                }
            } catch (IOException e) {
                throw new RuntimeException("파일 저장에 실패했습니다.", e);
            }
        }

        // Notice 엔티티에 작성자 설정
        notice.setUser(user);

        // Notice 엔티티를 데이터베이스에 저장
        return noticeRepository.save(notice);
    }


    public Page<NoticeListResDto> noticeListWithSearch(Pageable pageable, String searchType, String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            switch (searchType) {
                case "title":
                    return noticeRepository.findByTitleContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(Notice::listFromEntity);
                case "userNickname":
                    return noticeRepository.findByUserNicknameContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(Notice::listFromEntity);
                case "all":
                    return noticeRepository.findByTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCaseAndDelYN(
                                    searchQuery, searchQuery, DelYN.N, pageable)
                            .map(Notice::listFromEntity);
                default:
                    return noticeRepository.findByDelYN(DelYN.N, pageable).map(Notice::listFromEntity);
            }
        } else {
            return noticeRepository.findByDelYN(DelYN.N, pageable).map(Notice::listFromEntity);
        }
    }


    public NoticeDetailDto getNoticeDetail(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));
        return notice.fromDetailEntity();
    }

    @Transactional
    public void noticeUpdate(Long id, NoticeUpdateDto dto, MultipartFile imageSsr) {
        // 공지사항 조회
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        // 현재 로그인된 사용자의 이메일을 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 이미지 처리: imageSsr이 null이 아니면 imageSsr 사용, 그렇지 않으면 dto의 image 사용
        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getImage();

        User loginUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 관리자입니다."));
        // 로그인된 사용자가 작성자인지 확인
        if (loginUser.getRole() == Role.ADMIN) {
            try {
                if (image != null && !image.isEmpty()) {  // 이미지가 null이 아니고 비어있지 않을 때만 처리
                    String bgImagePathFileName = notice.getId() + "_" + image.getOriginalFilename();
                    byte[] bgImagePathByte = image.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    notice.toUpdate(dto, s3ImagePath);
                } else {
                    // 이미지가 없으면 기존 이미지를 유지한 채로 제목과 내용만 업데이트
                    notice.toUpdate(dto, notice.getImageUrl());
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.", e);
            }
        } else {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }

        // 변경된 공지사항 저장
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
