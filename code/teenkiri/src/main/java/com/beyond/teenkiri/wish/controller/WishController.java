package com.beyond.teenkiri.wish.controller;

import com.beyond.teenkiri.common.dto.CommonResDto;
import com.beyond.teenkiri.wish.dto.WishDto;
import com.beyond.teenkiri.wish.service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wish")
public class WishController {

    @Autowired
    private WishService wishService;

    @PostMapping("/{subjectId}")
    public ResponseEntity<?> addWish(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long subjectId) {
        try {
            WishDto wishDto = wishService.addWish(userDetails.getUsername(), subjectId);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 추가 성공", wishDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "찜 추가 실패: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<?> removeWish(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long subjectId) {
        try {
            wishService.removeWish(userDetails.getUsername(), subjectId);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "찜 삭제 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "찜 삭제 실패: " + e.getMessage(), null));
        }
    }
}


