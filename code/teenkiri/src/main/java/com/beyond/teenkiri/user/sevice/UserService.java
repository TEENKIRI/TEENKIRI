package com.beyond.teenkiri.user.sevice;

import com.beyond.teenkiri.chatting.service.ChatService;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.user.config.JwtTokenProvider;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.*;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Random;

@Service("userService")
public class UserService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ChatService chatService; // ChatService 주입

    public String login(UserLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("잘못된 이메일/비밀번호 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 이메일/비밀번호 입니다.");
        }

        return jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
    }

    public String getEmailFromToken(String token) {
        return jwtTokenProvider.getEmailFromToken(token);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    public String findId(UserFindIdDto findIdDto) {
        User user = userRepository.findByNameAndPhone(findIdDto.getUsername(), findIdDto.getPhone())
                .orElseThrow(() -> new RuntimeException("없는 사용자 입니다."));

        String email = user.getEmail();
        return email.substring(0, 4) + "******" + email.substring(email.indexOf("@"));
    }

    public void sendPasswordResetLink(UserFindPasswordDto findPasswordDto) {
        User user = userRepository.findByNameAndPhoneAndEmail(findPasswordDto.getUsername(), findPasswordDto.getPhone(), findPasswordDto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인해주세요."));

        String resetToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name());
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

        if (passwordResetDto.getNewPassword().length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (passwordEncoder.matches(passwordResetDto.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("이전과 동일한 비밀번호로 설정할 수 없습니다.");
        }

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        userRepository.save(user);
    }

    public void register(UserSaveReqDto saveReqDto) {
        if (saveReqDto.getPassword().length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }
        if (userRepository.existsByEmail(saveReqDto.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }

        // 닉네임 비속어 필터링 추가
        String filteredNickname = chatService.filterMessage(saveReqDto.getNickname());
        if (!filteredNickname.equals(saveReqDto.getNickname())) {
            throw new RuntimeException("비속어는 닉네임으로 설정할 수 없습니다.");
        }
        User user = saveReqDto.toEntity();
        userRepository.save(user);
        System.out.println("사용자 저장 전: " + user);
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
        String filteredNickname = chatService.filterMessage(nickname);
        if (!filteredNickname.equals(nickname)) {
            throw new RuntimeException("비속어는 닉네임으로 설정할 수 없습니다.");
        }

        return !userRepository.existsByNickname(nickname);
    }


    public void deleteAccount(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인할 수 없습니다: " + email));

        user.setDelYN(DelYN.Y);
        user.setNickname("<알수없음>" + System.currentTimeMillis());
        userRepository.save(user);
    }

    public User getUserFromToken(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
