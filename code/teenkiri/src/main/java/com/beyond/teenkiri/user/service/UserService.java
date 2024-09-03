package com.beyond.teenkiri.user.service;

import com.beyond.teenkiri.chat.service.ChatService;
import com.beyond.teenkiri.common.domain.DelYN;
import com.beyond.teenkiri.notification.domain.Notification;
import com.beyond.teenkiri.notification.dto.NotificationListDto;
import com.beyond.teenkiri.notification.repository.NotificationRepository;
import com.beyond.teenkiri.qna.domain.QnA;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.repository.QnARepository;

import com.beyond.teenkiri.user.config.JwtTokenprovider;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.*;
import com.beyond.teenkiri.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("userService")
public class UserService {

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    private JwtTokenprovider jwtTokenprovider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private QnARepository qnARepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public UserService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String login(UserLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("잘못된 이메일/비밀번호 입니다."));

        if (user.getReportCount() >= 5) {
            user.setDelYN(DelYN.Y);
            userRepository.save(user);
            throw new RuntimeException("해당 계정은 비활성화 상태입니다.");
        }

        if (!user.getDelYN().equals(DelYN.N)) {
            throw new RuntimeException("해당 계정은 비활성화 상태입니다.");
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 이메일/비밀번호 입니다.");
        }

        return jwtTokenprovider.createToken(user.getEmail(), user.getRole().name(), user.getId());
    }


    public String getEmailFromToken(String token) {
        return jwtTokenprovider.getEmailFromToken(token);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다.11"));
    }

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);

    }

    public String findId(UserFindIdDto findIdDto) {
        User user = userRepository.findByNameAndPhone(findIdDto.getName(), findIdDto.getPhone())
                .orElseThrow(() -> new EntityNotFoundException("없는 사용자 입니다."));

        return maskEmail(user.getEmail());
    }

    public User findByEmailReturnNull(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private String maskEmail(String email) {
        return email.substring(0, 4) + "******" + email.substring(email.indexOf("@"));
    }

    public void sendPasswordResetLink(UserFindPasswordDto findPasswordDto) {
        User user = userRepository.findByNameAndPhoneAndEmail(findPasswordDto.getName(), findPasswordDto.getPhone(), findPasswordDto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인해주세요."));

        // userId를 추가 인자로 전달하여 토큰 생성
        String resetToken = jwtTokenprovider.createToken(user.getEmail(), user.getRole().name(), user.getId());

        // Redis에 저장 (필요한 경우)
        redisService.saveVerificationCode(findPasswordDto.getEmail(), resetToken);

        // 이메일 전송
        String resetLink = "http://localhost:8082/user/reset-password?token=" + resetToken;
        emailService.sendSimpleMessage(findPasswordDto.getEmail(), "비밀번호 재설정", "비밀번호 재설정 링크: " + resetLink);
    }


    public void resetPassword(PasswordResetDto passwordResetDto) {
        String email = jwtTokenprovider.getEmailFromToken(passwordResetDto.getToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인해주세요."));

        validatePassword(passwordResetDto.getNewPassword(), passwordResetDto.getConfirmPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(passwordResetDto.getNewPassword()));
        userRepository.save(user);
    }

    private void validatePassword(String newPassword, String confirmPassword, String currentPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("동일하지 않은 비밀번호 입니다.");
        }

        if (newPassword.length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new RuntimeException("이전과 동일한 비밀번호로 설정할 수 없습니다.");
        }
    }

    public void register(UserSaveReqDto saveReqDto) {
        validateRegistration(saveReqDto);

        User user = saveReqDto.toEntity(passwordEncoder.encode(saveReqDto.getPassword()));
        userRepository.save(user);
    }

    private void validateRegistration(UserSaveReqDto saveReqDto) {
        if (saveReqDto.getPassword().length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (userRepository.existsByEmail(saveReqDto.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }

        String filteredNickname = chatService.filterMessage(saveReqDto.getNickname());
        if (!filteredNickname.equals(saveReqDto.getNickname())) {
            throw new RuntimeException("비속어는 닉네임으로 설정할 수 없습니다.");
        }
    }

    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        redisService.saveVerificationCode(email, code);
        emailService.sendSimpleMessage(email, "이메일 인증 코드", "인증 코드: " + code);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000 + 1) + 1000;
        return String.valueOf(code);
    }

    public boolean verifyEmail(String email, String code) {
        if (redisService.verifyCode(email, code)) {
            return true;
        } else {
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

    public void checkreportcount(User user) {
        if (user.getReportCount() >= 5) {
            user.setDelYN(DelYN.Y);
            userRepository.save(user);

            messagingTemplate.convertAndSend("/topic/logout", user.getEmail());

            deleteAccount(user.getEmail());
        }
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 확인할 수 없습니다: " + email));

        user.setDelYN(DelYN.Y);
        user.setNickname("<알수없음>" + System.currentTimeMillis());
        userRepository.save(user);
    }

    public User getUserFromToken(String token) {
        String email = jwtTokenprovider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }


    public void updateUserDetails(User user, UserEditReqDto editReqDto) {
        User savedUser = findByEmail(user.getEmail());

        if (editReqDto.getPassword() != null && !editReqDto.getPassword().isEmpty()) {
            validatePassword(editReqDto.getPassword(), editReqDto.getConfirmPassword(), user.getPassword());
            savedUser.setPassword(passwordEncoder.encode(editReqDto.getPassword()));
        }

        if (editReqDto.getNickname() != null && !editReqDto.getNickname().isEmpty()) {
            String filteredNickname = chatService.filterMessage(editReqDto.getNickname());
            if (!filteredNickname.equals(editReqDto.getNickname())) {
                throw new RuntimeException("비속어는 닉네임으로 설정할 수 없습니다.");
            }
            // 닉네임이 변경된 경우에만 중복 검사를 수행
            if (!savedUser.getNickname().equals(editReqDto.getNickname())) {
                if (userRepository.existsByNickname(editReqDto.getNickname())) {
                    throw new RuntimeException("이미 사용 중인 닉네임입니다.");
                }
                savedUser.setNickname(editReqDto.getNickname());
            }
        }

        if (editReqDto.getAddress() != null) {
            savedUser.setAddress(editReqDto.getAddress());
        }
        savedUser.setName(editReqDto.getName());
        userRepository.save(savedUser);
    }

    public List<QnAListResDto> getUserQnAList(String userEmail) {
        User user = findByEmail(userEmail);
        List<QnA> qnaList = qnARepository.findByUser(user);
        List<QnAListResDto> qnaListResDtos = new ArrayList<>();

        for (QnA qna : qnaList) {
            qnaListResDtos.add(qna.listFromEntity());
        }

        return qnaListResDtos;
    }

    public List<NotificationListDto> getNotificationList() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> notifications = notificationRepository.findByUserEmail(userEmail);
        List<NotificationListDto> notificationListDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            notificationListDtos.add(notification.listFromEntity());
        }
        return notificationListDtos;
    }


//    public void updateUserInfo(String username, UserEditReqDto editReqDto) {
//        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        user = updateUserDetails(user, editReqDto);
//        userRepository.save(user);
//    }

    public Page<UserListDto> userList(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if (!user.getRole().toString().equals("ADMIN")) {
            System.out.println("접근권한없음");
            throw new SecurityException("접근권한이 없습니다.");
        }
        Page<User> users = userRepository.findAll(pageable);
        return users.map(a -> a.listFromEntity());
    }

    public String getNicknameByUserId(Long userId) {
        return userRepository.findNicknameById(userId);
    }
}
