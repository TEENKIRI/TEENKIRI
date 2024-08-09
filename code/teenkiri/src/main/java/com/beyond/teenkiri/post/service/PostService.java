package com.beyond.teenkiri.post.service;


import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.common.service.CommonMethod;
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

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        if (user.getRole() != Role.ADMIN) {
            throw new SecurityException("권한이 없습니다.");
        }
        MultipartFile image = (imageSsr == null) ? dto.getImage() : imageSsr;
        Post post = dto.toEntity();
        try {
            MultipartFile imageFile = image;
            if (!imageFile.isEmpty()) {
                String bgImagePathFileName = post.getId() + "_" + imageFile.getOriginalFilename();
                byte[] bgImagePathByte = imageFile.getBytes();
                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                post.updateImagePath(s3ImagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다.");
        }
        post.setUser(user);
        return postRepository.save(post);
    }

    public Page<PostListResDto> postList(Pageable pageable) {
        Page<Post> posts = postRepository.findByDelYN(DelYN.N, pageable);
        return posts.map(a -> a.listFromEntity());
    }

    public PostDetailDto postDetail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        return post.detailFromEntity();
    }

    @Transactional
    public void postUpdate(Long id, PostUpdateDto dto, MultipartFile imageSsr) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글 입니다."));

        MultipartFile image = (imageSsr != null) ? dto.getImage() : imageSsr;
        if (post.getUser().getEmail().equals(userEmail)) {
            try {
                MultipartFile imageFile = image;
                if (!imageFile.isEmpty()) {
                    String bgImagePathFileName = post.getId() + "_" + imageFile.getOriginalFilename();
                    byte[] bgImagePathByte = imageFile.getBytes();
                    String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, bgImagePathByte);
                    post.toUpdate(dto, s3ImagePath);
//                post.updateImagePath(s3ImagePath);
                }
            } catch (IOException e) {
                throw new RuntimeException("게시글 수정에 실패했습니다.");
            }
        } else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
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
