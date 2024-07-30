package com.beyond.teenkiri.User.sevice;

import com.beyond.teenkiri.User.config.JwtTokenProvider;
import com.beyond.teenkiri.User.domain.User;
import com.beyond.teenkiri.User.dto.*;
import com.beyond.teenkiri.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    public String login(UserLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("잘못된 이메일/비밀번호 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 이메일/비밀번호 입니다.");
        }

        return jwtTokenProvider.createToken(user.getEmail(), List.of(user.getUserType().name()));
    }

    public String findId(UserFindIdDto findIdDto) {
        User user = userRepository.findByUsernameAndPhone(findIdDto.getUsername(), findIdDto.getPhone())
                .orElseThrow(() -> new RuntimeException("없는 사용자 입니다."));

        String email = user.getEmail();
        return email.substring(0, 4) + "******" + email.substring(email.indexOf("@"));
    }

    public void sendPasswordResetLink(UserFindPasswordDto findPasswordDto) {
        User user = userRepository.findByUsernameAndPhoneAndEmail(findPasswordDto.getUsername(), findPasswordDto.getPhone(), findPasswordDto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인해주세요."));

        String resetToken = jwtTokenProvider.createToken(user.getEmail(), List.of(user.getUserType().name()));
        redisService.saveVerificationCode(findPasswordDto.getEmail(), resetToken);

        String resetLink = "http://localhost:8081/api/reset-password?token=" + resetToken; // /api 경로 추가
        emailService.sendSimpleMessage(findPasswordDto.getEmail(), "비밀번호 재설정", "비밀번호 재설정 링크: " + resetLink);
    }

    public void resetPassword(PasswordResetDto passwordResetDto) {
        String email = jwtTokenProvider.getEmailFromToken(passwordResetDto.getToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인해주세요."));

        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getConfirmPassword())) {
            throw new RuntimeException("동일하지 않은 비밀번호 입니다.");
        }

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        userRepository.save(user);
    }

    public void register(UserSaveReqDto saveReqDto) {
        if (userRepository.existsByEmail(saveReqDto.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }

        User user = User.builder()
                .username(saveReqDto.getUsername())
                .email(saveReqDto.getEmail())
                .password(passwordEncoder.encode(saveReqDto.getPassword()))
                .nickname(saveReqDto.getNickname())
                .address(saveReqDto.getAddress())
                .phone(saveReqDto.getPhone())
                .userType(User.UserType.STUDENT)
                .isVerified(false)
                .build();

        System.out.println("사용자 저장 전: " + user);
        userRepository.save(user);
        System.out.println("사용자 저장 완료: " + user);
    }

    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        redisService.saveVerificationCode(email, code);
        System.out.println("보낸 인증 코드: " + code);
        emailService.sendSimpleMessage(email, "이메일 인증 코드", "인증 코드: " + code);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000 + 1) + 1000;
        return String.valueOf(code);
    }

    public boolean verifyEmail(String email, String code) {
        System.out.println("검증할 이메일: " + email + ", 코드: " + code);
        boolean isCodeValid = redisService.verifyCode(email, code);
        if (isCodeValid) {
            System.out.println("인증 코드 유효: " + code);
            return true;
        } else {
            System.out.println("코드 검증 실패. 이메일: " + email + ", 코드: " + code);
            throw new RuntimeException("인증 코드가 유효하지 않습니다.");
        }
    }

    public boolean checkNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}