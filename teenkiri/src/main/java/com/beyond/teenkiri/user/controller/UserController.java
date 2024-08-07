package com.beyond.teenkiri.user_board.controller;

import com.beyond.teenkiri.user_board.dto.UserDetailDto;
import com.beyond.teenkiri.user_board.dto.UserSaveReqDto;
import com.beyond.teenkiri.user_board.dto.UserUpdateDto;
import com.beyond.teenkiri.user_board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 생성 화면을 보여줍니다.
    @GetMapping("create")
    public String userCreate() {
        return "user/create";
    }

    // 사용자 생성 요청을 처리
    @PostMapping("create")
    public String userCreatePost(@ModelAttribute UserSaveReqDto dto, Model model) {
        try {
            userService.userCreate(dto);
            return "redirect:/user/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/create";  // 오류가 발생하면 다시 생성 화면으로 돌아갑니다.
        }
    }

    // 모든 사용자 목록을 보여줍니다.
    @GetMapping("list")
    public String userListPage(Model model, @PageableDefault(size = 10, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("userList", userService.userList(pageable));
        return "user/list";
    }

    // 특정 사용자 상세 정보를 보여줍니다.
    @GetMapping("detail/{id}")
    public String userDetailDto(@PathVariable Long id, Model model) {
        UserDetailDto userDetail = userService.userDetail(id);
        model.addAttribute("user", userDetail);
        return "user/detail";
    }

    // 사용자 정보를 업데이트합니다.
    @PostMapping("update/{id}")
    public String userUpdate(@PathVariable Long id, @ModelAttribute UserUpdateDto dto, Model model) {
        userService.userUpdate(id, dto);
        return "redirect:/user/detail/" + id;
    }

    // 사용자를 삭제합니다.
    @GetMapping("delete/{id}")
    public String userDelete(@PathVariable Long id) {
        userService.userDelete(id);
        return "redirect:/user/list";
    }
}
