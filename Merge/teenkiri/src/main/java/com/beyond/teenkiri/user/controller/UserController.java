package com.beyond.teenkiri.user.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.user.dto.*;
import com.beyond.teenkiri.user.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller("userController")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/api/register")
    public String registerPage() {
        return "user/register";
    }

    @GetMapping("/api/find-id")
    public String findIdPage() {
        return "user/find-id";
    }

    @GetMapping("/api/find-password")
    public String findPasswordPage() {
        return "user/find-password";
    }

    @GetMapping("/api/reset-password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "user/reset-password";
    }

    @GetMapping("/api/welcome")
    public String welcomePage() {
        return "user/welcome";
    }

    @GetMapping("/api/delete-account")
    public String deletePage() {
        return "user/delete-account";
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        try {
            String token = userService.login(loginDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "로그인 성공", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "잘못된 이메일/비밀번호 입니다.", null));
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody UserSaveReqDto saveReqDto) {
        try {
            userService.register(saveReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원가입 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/api/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailVerificationDto verificationDto) {
        userService.sendVerificationEmail(verificationDto.getEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송 성공", null));
    }

    @PostMapping("/api/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationDto verificationDto) {
        try {
            boolean isVerified = userService.verifyEmail(verificationDto.getEmail(), verificationDto.getCode());
            if (isVerified) {
                return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "이메일 인증 성공", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다.", null));
            }
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/api/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody UserSaveReqDto saveReqDto) {
        boolean isAvailable = userService.checkNickname(saveReqDto.getNickname());
        if (isAvailable) {
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "닉네임 사용 가능", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.", null));
        }
    }

    @PostMapping("/api/find-id")
    public ResponseEntity<?> findId(@RequestBody UserFindIdDto findIdDto) {
        try {
            String maskedEmail = userService.findId(findIdDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "아이디 찾기 성공", maskedEmail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "없는 사용자 입니다.", null));
        }
    }

    @PostMapping("/api/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserFindPasswordDto findPasswordDto) {
        try {
            userService.sendPasswordResetLink(findPasswordDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "비밀번호 재설정 링크 전송 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "사용자 정보를 확인해주세요.", null));
        }
    }

    @PostMapping("/api/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        try {
            userService.resetPassword(passwordResetDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "비밀번호 재설정 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "비밀번호 재설정에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.logout(token);
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "로그아웃 성공", null));
    }

    @PostMapping("/api/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token, @RequestBody String confirmation) {
        if (!"틴끼리 사이트 회원 탈퇴에 동의합니다".equals(confirmation)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 탈퇴 문구가 올바르지 않습니다.", null));
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.deleteAccount(token);
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 탈퇴 성공", null));
    }
}
