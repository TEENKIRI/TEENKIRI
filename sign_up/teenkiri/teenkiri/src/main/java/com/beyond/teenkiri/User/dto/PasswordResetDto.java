package com.beyond.teenkiri.User.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordResetDto {
    private String newPassword;
    private String confirmPassword;
    private String token;
}
