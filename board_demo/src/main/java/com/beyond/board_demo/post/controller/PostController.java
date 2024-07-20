package com.beyond.board_demo.post.controller;

import com.beyond.board_demo.post.dto.PostSaveReqDto;
import com.beyond.board_demo.post.dto.PostUpdateDto;
import com.beyond.board_demo.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시물 생성 화면을 보여줍니다.
    @GetMapping("create")
    public String postCreateScreen() {
        return "post/create";  // 템플릿 경로 수정
    }

    // 게시물을 생성합니다.
    @PostMapping("create")
    public String postCreatePost(@ModelAttribute PostSaveReqDto dto, Model model) {
        try {
            postService.postCreate(dto);
            return "redirect:/post/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "post/create";  // 오류가 발생하면 다시 생성 화면으로 돌아갑니다.
        }
    }

    // 모든 게시물 목록을 보여줍니다.
    @GetMapping("list")
    public String postListResDtosList(Model model) {
        model.addAttribute("postList", postService.postList());
        return "post/list";  // 템플릿 경로 수정
    }

    // 특정 게시물의 상세 정보를 보여줍니다.
    @GetMapping("detail/{id}")
    public String postDetailDto(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.postDetail(id));
        return "post/detail";
    }

    // 게시물을 업데이트합니다.
    @PostMapping("update/{id}")
    public String postUpdate(@PathVariable Long id, @ModelAttribute PostUpdateDto dto, Model model) {
        postService.postUpdate(id, dto);
        return "redirect:/post/detail/" + id;
    }

    // 게시물을 삭제합니다.
    @GetMapping("delete/{id}")
    public String postDelete(@PathVariable Long id, Model model) {
        postService.postDelete(id);
        return "redirect:/post/list";
    }
}
