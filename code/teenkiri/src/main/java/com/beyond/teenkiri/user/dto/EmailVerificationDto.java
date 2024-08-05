package com.beyond.teenkiri.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerificationDto {
    private String email;
    private String code;
}
