package com.beyond.teenkiri.mypage.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.enrollment.dto.EnrollListResDto;
import com.beyond.teenkiri.mypage.service.MypageService;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.subject.domain.Subject;

import com.beyond.teenkiri.user.dto.UserUpdateReqDto;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final UserRepository userRepository;

    @Autowired
    public MypageController(MypageService mypageService, UserRepository userRepository) {
        this.mypageService = mypageService;
        this.userRepository = userRepository;
    }



    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {

        return "mypage/mypage_update";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long id, @RequestBody UserUpdateReqDto userUpdateReqDto) {
        try {
            userUpdateReqDto.setId(id);
            mypageService.updateUserInfo(userUpdateReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "업데이트 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "업데이트 실패: " + e.getMessage(), null));
        }
    }

    @GetMapping("/lecture/{userId}")
    public String getUserLectures(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<EnrollListResDto> enrollments = mypageService.getUserEnrollments(userId, pageable);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("userId", userId);
        return "mypage/lectures";
    }

    @GetMapping("/favorites/{userId}")
    public String getUserFavorites(@PathVariable Long userId, Model model) {
        List<Subject> favorites = mypageService.getUserFavorites(userId);
        model.addAttribute("favorites", favorites);
        model.addAttribute("userId", userId);
        return "mypage/favorites";
    }

    @GetMapping("/qna/{userId}")
    public String getUserQuestions(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<QnAListResDto> questions = mypageService.getUserQuestions(userId, pageable);
        model.addAttribute("questions", questions);
        model.addAttribute("userId", userId);
        return "mypage/questions";
    }
}
