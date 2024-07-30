package com.beyond.teenkiri.User.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserFindPasswordDto {
    private String username;
    private String phone;
    private String email;
}
