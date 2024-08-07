package com.beyond.teenkiri.user.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.course.dto.CourseListResDto;
import com.beyond.teenkiri.qna.dto.QnAListResDto;
import com.beyond.teenkiri.qna.service.QnAService;
import com.beyond.teenkiri.subject.dto.SubjectListResDto;
import com.beyond.teenkiri.user.domain.User;
import com.beyond.teenkiri.user.dto.*;
import com.beyond.teenkiri.user.service.UserService;
import com.beyond.teenkiri.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WishService wishService;

    @Autowired
    private QnAService qnaService;

    @GetMapping("/edit-info")
    public ResponseEntity<?> getEditUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            User user = userService.getUserFromToken(token);
            UserEditInfoDto userEditInfoDto = UserEditInfoDto.fromEntity(user);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보 수정 조회 성공", userEditInfoDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @PostMapping("/edit-info")
    public ResponseEntity<?> editUserInfo(@RequestHeader("Authorization") String token, @RequestBody UserEditReqDto editReqDto) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            userService.updateUserInfo(token, editReqDto);
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
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "로그인 성공", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "잘못된 이메일/비밀번호 입니다.", null));
        }
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

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailVerificationDto verificationDto) {
        userService.sendVerificationEmail(verificationDto.getEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송 성공", null));
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

//    @PostMapping("/check-nickname")
//    public ResponseEntity<?> checkNickname(@RequestBody UserSaveReqDto saveReqDto) {
//        try {
//            boolean isAvailable = userService.checkNickname(saveReqDto.getNickname());
//            if (isAvailable) {
//                return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "닉네임 사용 가능", null));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.", null));
//            }
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null));
//        }
//    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody UserFindIdDto findIdDto) {
        try {
            String maskedEmail = userService.findId(findIdDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "아이디 찾기 성공", maskedEmail));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "없는 사용자 입니다.", null));
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

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            String email = userService.getEmailFromToken(token);
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보 조회 성공", user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getUserWishlist(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            User user = userService.getUserFromToken(token);
            List<SubjectListResDto> wishlist = wishService.getUserWishList(user);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 목록 조회 성공", wishlist));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }

    @DeleteMapping("/wishlist/{subjectId}")
    public ResponseEntity<?> removeWish(@RequestHeader("Authorization") String token, @PathVariable Long subjectId) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            User user = userService.getUserFromToken(token);
            wishService.removeWish(user, subjectId);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 삭제 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "찜 삭제 실패: " + e.getMessage(), null));
        }
    }


    @GetMapping("/qna")
    public ResponseEntity<?> getUserQnAList(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            String email = userService.getEmailFromToken(token);
            List<QnAListResDto> qnaList = userService.getUserQnAList(email);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "질문 목록을 성공적으로 가져왔습니다", qnaList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResDto(HttpStatus.UNAUTHORIZED, "Invalid token", null));
        }
    }
}
