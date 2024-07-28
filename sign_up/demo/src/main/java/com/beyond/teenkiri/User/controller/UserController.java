package com.beyond.teenkiri.User.controller;

import com.beyond.teenkiri.User.dto.UserDto;
import com.beyond.teenkiri.User.service.EmailService;
import com.beyond.teenkiri.User.service.RedisService;
import com.beyond.teenkiri.User.service.UserService;
import com.beyond.teenkiri.User.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final RedisService redisService;

    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @GetMapping("/find-id")
    public String findIdForm() {
        return "find-id";
    }

    @GetMapping("/find-password")
    public String findPasswordForm() {
        return "find-password";
    }


    @PostMapping("/register")
    public void registerUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String emailCode,
                             @RequestParam String password,
                             @RequestParam String phone,
                             @RequestParam(required = false) String address,
                             @RequestParam String nickname,
                             HttpServletResponse response) throws IOException {
        try {
            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

            if (password == null) {
                throw new IllegalArgumentException("Password cannot be null");
            }

            UserDto userDto = UserDto.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .phone(phone)
                    .address(address)
                    .nickname(nickname)
                    .build();

            userService.registerUser(userDto);
            response.sendRedirect("/api/user/login");
        } catch (Exception e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> loginUser(@RequestParam String email, @RequestParam String password) {
        UserDto loggedInUser = userService.loginUser(email, password);
        String token = jwtUtil.generateToken(email);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-verification-code")
    @ResponseBody
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String code = emailService.sendVerificationEmail(email);
        redisService.saveVerificationCode(email, code);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    @ResponseBody
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = redisService.verifyCode(email, code);
        return ResponseEntity.ok(isVerified ? "인증 성공" : "인증 실패");
    }

    @GetMapping("/check-duplicate")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestParam String field, @RequestParam String value) {
        boolean isDuplicate = false;
        if (field.equals("email")) {
            isDuplicate = userService.isEmailDuplicate(value);
        } else if (field.equals("nickname")) {
            isDuplicate = userService.isNicknameDuplicate(value);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find-id")
    @ResponseBody
    public ResponseEntity<String> findId(@RequestParam String username, @RequestParam String phone) {
        try {
            String email = userService.findUserEmail(username, phone);
            if (email == null) {
                return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
            }
            String maskedEmail = maskEmail(email);
            return ResponseEntity.ok(maskedEmail);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("아이디 찾기 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/find-password")
    @ResponseBody
    public ResponseEntity<String> findPassword(@RequestParam String username, @RequestParam String email) {
        try {
            boolean isUserExist = userService.verifyUser(username, email);
            if (!isUserExist) {
                return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
            }
            String token = jwtUtil.generateToken(email);
            String resetLink = "http://localhost:8081/api/user/reset-password?token=" + URLEncoder.encode(token, "UTF-8");
            emailService.sendPasswordResetEmail(email, resetLink);
            return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("비밀번호 찾기 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam("token") String token, Model model) {
        try {
            String decodedToken = URLDecoder.decode(token, "UTF-8");
            String email = jwtUtil.extractEmail(decodedToken);
            model.addAttribute("email", email);
            model.addAttribute("token", decodedToken);
            return "reset-password";
        } catch (Exception e) {
            return "error"; // 에러 페이지로 리디렉션
        }
    }

    @PostMapping("/do-reset-password")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
            }
            String decodedToken = URLDecoder.decode(token, "UTF-8");
            String email = jwtUtil.extractEmail(decodedToken);
            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
        } catch (Exception e) {
            System.err.println("비밀번호 재설정 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("비밀번호 재설정 중 오류가 발생했습니다.");
        }
    }



    private String maskEmail(String email) {
        int index = email.indexOf("@");
        if (index > 4) {
            String prefix = email.substring(0, 4);
            String domain = email.substring(index);
            return prefix + "******" + domain;
        }
        return email;
    }
}
