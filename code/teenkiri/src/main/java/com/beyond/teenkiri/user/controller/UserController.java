package com.beyond.teenkiri.user.controller;

import com.beyond.teenkiri.common.dto.CommonErrorDto;
import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.notification.dto.NotificationListDto;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.service.QnAService;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.user.domain.Role;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.*;
import com.beyond.teenkiri.user.service.UserService;
import com.beyond.teenkiri.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WishService wishService;

    @Autowired
    private QnAService qnaService;



    // user-info와 같은 기능.
    @GetMapping("/edit-info")
    public ResponseEntity<?> getEditUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            UserEditInfoDto userEditInfoDto = UserEditInfoDto.fromEntity(user);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보 수정 조회 성공", userEditInfoDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @PostMapping("/edit-info")
    public ResponseEntity<?> editUserInfo(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserEditReqDto editReqDto) {
        try {
            userService.updateUserInfo(userDetails.getUsername(), editReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보 수정 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 정보 수정 실패: " + e.getMessage(), null));
        }
    }


    @PostMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody NicknameCheckDto nicknameCheckDto) {
        try {
            boolean isAvailable = userService.checkNickname(nicknameCheckDto.getNickname());
            if (isAvailable) {
                return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "닉네임 사용 가능", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.", null));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null));
        }
    }

    @PostMapping("/check-nickname-from-save")
    public ResponseEntity<?> checkNicknameFromSave(@RequestBody UserSaveReqDto saveReqDto) {
        return checkNickname(saveReqDto.toNicknameCheckDto());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        try {
            String token = userService.login(loginDto);
            System.out.println("Generated JWT Token: " + token);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"로그인성공",token);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.UNAUTHORIZED,"잘못된 이메일/비밀번호 입니다.");
            return new ResponseEntity<>(commonErrorDto, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailVerificationDto verificationDto) {
        userService.sendVerificationEmail(verificationDto.getEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송 성공", null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserSaveReqDto saveReqDto) {
        try {
            userService.register(saveReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원가입 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/verify-email")
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody UserFindIdDto findIdDto) {
        try {
            String maskedEmail = userService.findId(findIdDto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "아이디 찾기 성공",maskedEmail);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST,"없는 사용자 입니다.");
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody UserFindPasswordDto findPasswordDto) {
        try {
            userService.sendPasswordResetLink(findPasswordDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "비밀번호 재설정 링크 전송 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "사용자 정보를 확인해주세요.", null));
        }
    }


    @GetMapping("/reset-password")
    public ResponseEntity<?> showResetPasswordPage(@RequestParam("token") String token) {
        // 토큰 유효성 검사 등 추가 로직 수행 가능

        // 이 단계에서 Vue.js로의 페이지 렌더링을 의도
        // ResponseEntity는 JSON 응답을 반환하는 대신 Vue.js 페이지로 리디렉션합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/reset-password?token=" + token);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // POST 요청: 비밀번호를 실제로 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        try {
            userService.resetPassword(passwordResetDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "비밀번호 재설정 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "비밀번호 재설정에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String confirmation) {
        if (!"틴끼리 사이트 회원 탈퇴에 동의합니다".equals(confirmation)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 탈퇴 문구가 올바르지 않습니다.", null));
        }
        userService.deleteAccount(userDetails.getUsername());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 탈퇴 성공", null));
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보 조회 성공", user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getUserWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            List<SubjectListResDto> wishlist = wishService.getUserWishList(user);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 목록 조회 성공", wishlist));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @DeleteMapping("/wishlist/{subjectId}")
    public ResponseEntity<?> removeWish(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long subjectId) {
        try {
            User user = userService.findByEmail(userDetails.getUsername());
            // 아 이거 아닌디; <일단 보류 뭔 타입 에러여;;>
            wishService.removeWish(String.valueOf(user), subjectId);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 삭제 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "찜 삭제 실패: " + e.getMessage(), null));
        }
    }

    @GetMapping("/qna")
    public ResponseEntity<?> getUserQnAList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            List<QnAListResDto> qnaList = userService.getUserQnAList(email);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "질문 목록을 성공적으로 가져왔습니다", qnaList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @GetMapping("/notification")
    public ResponseEntity<?> getUserNotificationList(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<NotificationListDto> notificationListDtos = userService.getNotificationList();
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "알림 목록을 성공적으로 가져왔습니다.", notificationListDtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<Object> userList(Pageable pageable){
        Page<UserListDto> userListDtos = userService.userList(pageable);
        CommonResDto dto = new CommonResDto(HttpStatus.OK,"회원목록을 조회합니다.", userListDtos);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        try {
            List<User> teachers = userService.findAllByRole(Role.TEACHER);
            List<UserListDto> teacherDtos = teachers.stream()
                    .map(User::listFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "Teachers list", teacherDtos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching teachers", null));
        }
    }


//    @GetMapping("/login/google")
//    public void googleLogin(HttpServletResponse response) throws  IOException {
//        String state = generateRandomState(); // CSRF 또는 상태 보호를 위한 state 생성
//        saveStateInSession(state); // 세션 또는 다른 저장소에 state를 저장
//
//        String clientId = "84045606752-rgoj9d32kp1bqjkv5i833kh96r9io2o3.apps.googleusercontent.com";
//        String redirectUri = URLEncoder.encode("http://localhost:8088/login/oauth2/code/google", "UTF-8");
//        String scope = URLEncoder.encode("profile email", "UTF-8");
//
//        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
//                + "response_type=code&client_id=" + clientId
//                + "&scope=" + scope
//                + "&state=" + state
//                + "&redirect_uri=" + redirectUri;
//
//        response.sendRedirect(authUrl);
//    }
//
//    private String generateRandomState() {
//        // CSRF 방지 또는 상태 유지를 위한 고유한 문자열을 생성합니다.
//        return UUID.randomUUID().toString();
//    }
//
//    private void saveStateInSession(String state) {
//        // 실제로는 이 state를 사용자의 세션에 저장하여 인증 요청 시 이 값을 비교합니다.
//        // 예를 들어, HttpSession을 통해 세션에 저장할 수 있습니다.
//        // session.setAttribute("oauth2_state", state);
//    }

}
