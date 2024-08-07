package com.beyond.teenkiri.post.controller;

import com.beyond.teenkiri.comment.service.CommentService;
import com.beyond.teenkiri.common.CommonResDto;
import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.post.domain.Post;
import com.beyond.teenkiri.post.dto.PostDetailDto;
import com.beyond.teenkiri.post.dto.PostListResDto;
import com.beyond.teenkiri.post.dto.PostSaveReqDto;
import com.beyond.teenkiri.post.dto.PostUpdateDto;
import com.beyond.teenkiri.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    // 게시물 생성 화면을 보여줍니다.
    @GetMapping("create")
    public String postCreateScreen() {
        return "/board/post/create";
    }

    // 게시물을 생성합니다.
    @PostMapping("create")
    public ResponseEntity<?> postCreatePost(@RequestBody PostSaveReqDto dto) {
        try {
            Post post = postService.postCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "게시글(자유게시판)이 성공적으로 등록되었습니다.",post.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (SecurityException | EntityNotFoundException e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    // 모든 게시물 목록을 보여줍니다.
    @GetMapping("list")
    public ResponseEntity<?> postListResDtosList(@PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListResDto> postList = postService.postList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "자유게시판 목록을 조회합니다.",postList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    // 특정 게시물의 상세 정보를 보여줍니다.
    @GetMapping("detail/{id}")
    public ResponseEntity<?> postDetailDto(@PathVariable Long id) {
        try{
            PostDetailDto postDetail = postService.postDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"자유게시판 상세정보를 조회합니다.", postDetail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    // 게시물을 업데이트합니다.
    @PostMapping("update/{id}")
    public ResponseEntity<?> postUpdate(@PathVariable Long id, @RequestBody PostUpdateDto dto) {
        try{
            postService.postUpdate(id, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"자유게시판에 작성하신 글이 성공적으로 업데이트 되었습니다.",id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    // 게시물을 삭제합니다.
    @GetMapping("delete/{id}")
    public ResponseEntity<?> postDelete(@PathVariable Long id) {
        try {
            postService.postDelete(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"자유게시판에 작성하신 글이 성공적으로 삭제되었습니다.",id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND,e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }
}
