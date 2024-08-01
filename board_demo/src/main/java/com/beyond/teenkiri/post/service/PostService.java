package com.beyond.teenkiri.post.service;

import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostSaveReqDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Post postCreate(PostSaveReqDto dto) {
        User user = userService.findByEmail(dto.getEmail());
        Post post = dto.toEntity(user);
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    public Page<PostListResDto> postList(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(a -> a.listFromEntity());
    }

    public PostDetailDto postDetail(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        return post.detailFromEntity();
    }

    @Transactional
    public void postUpdate(Long id, PostUpdateDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("post is not found"));
        post.toUpdate(dto);
        postRepository.save(post);
    }

    @Transactional
    public void postDelete(Long id) {
        postRepository.deleteById(id);
    }
}
