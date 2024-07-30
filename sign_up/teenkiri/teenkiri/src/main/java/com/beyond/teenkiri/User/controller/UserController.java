package com.beyond.teenkiri.User.controller;

import com.beyond.teenkiri.Common.dto.CommonResDto;
import com.beyond.teenkiri.User.dto.*;
import com.beyond.teenkiri.User.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/api/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/api/find-id")
    public String findIdPage() {
        return "find-id";
    }

    @GetMapping("/api/find-password")
    public String findPasswordPage() {
        return "find-password";
    }

    @GetMapping("/api/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @GetMapping("/api/welcome")
    public String welcomePage() {
        return "welcome";
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        try {
            String token = userService.login(loginDto);
            return ResponseEntity.ok(new CommonResDto<>(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 이메일/비밀번호 입니다.");
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody UserSaveReqDto saveReqDto) {
        try {
            userService.register(saveReqDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/api/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailVerificationDto verificationDto) {
        userService.sendVerificationEmail(verificationDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationDto verificationDto) {
        try {
            boolean isVerified = userService.verifyEmail(verificationDto.getEmail(), verificationDto.getCode());
            if (isVerified) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/api/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody UserSaveReqDto saveReqDto) {
        boolean isAvailable = userService.checkNickname(saveReqDto.getNickname());
        if (isAvailable) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다.");
        }
    }

    @PostMapping("/api/find-id")
    public ResponseEntity<?> findId(@RequestBody UserFindIdDto findIdDto) {
        try {
            String maskedEmail = userService.findId(findIdDto);
            return ResponseEntity.ok(new CommonResDto<>(maskedEmail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("없는 사용자 입니다.");
        }
    }

    @PostMapping("/api/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserFindPasswordDto findPasswordDto) {
        try {
            userService.sendPasswordResetLink(findPasswordDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보를 확인해주세요.");
        }
    }

    @PostMapping("/api/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        try {
            userService.resetPassword(passwordResetDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 재설정에 실패했습니다: " + e.getMessage());
        }
    }
}