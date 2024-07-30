package com.beyond.teenkiri.User.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto {
    private String email;
    private String password;
}
