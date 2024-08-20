package com.beyond.teenkiri.post.service;


import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.UploadAwsFileService;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostSaveReqDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.repository.UserRepository;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UploadAwsFileService uploadAwsFileService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, UserRepository userRepository, UploadAwsFileService uploadAwsFileService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.uploadAwsFileService = uploadAwsFileService;
    }

    @Transactional
    public Post postCreate(PostSaveReqDto dto, MultipartFile imageSsr) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자를 이메일로 조회, 없을 경우 예외 발생
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 이미지가 전달되지 않았으면 DTO에서 가져옴
        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;

        // Post 엔티티 생성
        Post post = dto.toEntity();

        // 이미지가 존재하고 비어있지 않을 경우 처리
        if (image != null && !image.isEmpty()) {
            try {
                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    String bgImagePathFileName = post.getId() + "_" + originalFilename;
                    byte[] bgImagePathByte = image.getBytes();

                    // S3에 파일 업로드 후 경로 설정
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    post.updateImagePath(s3ImagePath);
                } else {
                    throw new IllegalArgumentException("이미지 파일 이름이 유효하지 않습니다.");
                }
            } catch (IOException e) {
                throw new RuntimeException("파일 저장에 실패했습니다.", e);
            }
        }

        // 작성자 설정 후 저장
        post.setUser(user);
        return postRepository.save(post);
    }

    public Page<PostListResDto> postListWithSearch(Pageable pageable, String searchType, String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            switch (searchType) {
                case "title":
                    return postRepository.findByTitleContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(Post::listFromEntity);
                case "userNickname":
                    return postRepository.findByUserNicknameContainingIgnoreCaseAndDelYN(searchQuery, DelYN.N, pageable)
                            .map(Post::listFromEntity);
                case "all":
                    return postRepository.findByTitleContainingIgnoreCaseOrUserNicknameContainingIgnoreCaseAndDelYN(
                                    searchQuery, searchQuery, DelYN.N, pageable)
                            .map(Post::listFromEntity);
                default:
                    return postRepository.findByDelYN(DelYN.N, pageable).map(Post::listFromEntity);
            }
        } else {
            return postRepository.findByDelYN(DelYN.N, pageable).map(Post::listFromEntity);
        }
    }


//    public Page<PostListResDto> postList(Pageable pageable) {
//        Page<Post> posts = postRepository.findByDelYN(DelYN.N, pageable);
//        return posts.map(a -> a.listFromEntity());
//    }

    public PostDetailDto postDetail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        return post.detailFromEntity();
    }

    @Transactional
    public void postUpdate(Long id, PostUpdateDto dto, MultipartFile imageSsr) {
        // 현재 로그인된 사용자의 이메일을 가져옴
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // 게시글 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글 입니다."));

        // 이미지 처리: imageSsr이 null이 아니면 imageSsr 사용, 그렇지 않으면 dto의 image 사용
        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getImage();

        // 로그인된 사용자가 작성자인지 확인
        if (post.getUser().getEmail().equals(userEmail)) {
            try {
                if (image != null && !image.isEmpty()) {  // 이미지가 null이 아니고 비어있지 않을 때만 처리
                    String bgImagePathFileName = post.getId() + "_" + image.getOriginalFilename();
                    byte[] bgImagePathByte = image.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    post.toUpdate(dto, s3ImagePath);
                } else {
                    // 이미지가 없으면 기존 이미지를 유지한 채로 제목과 내용만 업데이트
                    post.toUpdate(dto, post.getImageUrl());
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.", e);
            }
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }

        // 변경된 게시글 저장
        postRepository.save(post);
    }


    @Transactional
    public void postDeleteDeep(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Post postDelete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        post.updateDelYN(DelYN.Y);
        return post;
    }
}
