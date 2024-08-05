package com.beyond.teenkiri.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto {
    private String email;
    private String password;
}
