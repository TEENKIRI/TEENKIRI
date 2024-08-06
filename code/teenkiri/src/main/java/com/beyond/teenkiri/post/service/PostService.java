package com.beyond.teenkiri.post.service;

import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostSaveReqDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.post.repository.PostRepository;
import com.beyond.teenkiri.user_board.domain.Role;
import com.beyond.teenkiri.user_board.domain.user;
import com.beyond.teenkiri.user_board.repository.UserRepository;
import com.beyond.teenkiri.user_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post postCreate(PostSaveReqDto dto) {
        //권한관리
        user user = userRepository.findByEmail(dto.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (user.getRole() != Role.ADMIN){
            throw new SecurityException("권한이 없습니다.");
        }
        Post post = dto.toEntity(user);
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
    public void postUpdate(Long id, PostUpdateDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글 입니다."));
        //게시글 작성자만 수정 가능할 수 있게함
        if (post.getUser().getEmail().equals(dto.getUserEmail())){
            post.toUpdate(dto);
        }else {
            throw new IllegalArgumentException("작성자 본인만 수정할 수 있습니다.");
        }
        postRepository.save(post);
    }

    @Transactional
    public void postDeleteDeep(Long id) {
        postRepository.deleteById(id);
    }
//    public Ordering orderCancel(Long id) {
//        Ordering ordering = orderingRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));
//        ordering.updateStatus(OrderStatus.CANCELED);
//        return ordering;
//    }
    @Transactional
    public Post postDelete(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글입니다."));
        post.updateDelYN(DelYN.Y);
        return post;
    }
}
